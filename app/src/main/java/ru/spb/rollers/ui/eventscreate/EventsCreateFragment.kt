package ru.spb.rollers.ui.eventscreate

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.view.*
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.view.menu.MenuBuilder
import androidx.appcompat.view.menu.MenuPopupHelper
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.button.MaterialButton
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import ru.spb.rollers.*
import ru.spb.rollers.databinding.EventsCreateFragmentBinding
import ru.spb.rollers.models.Event
import ru.spb.rollers.models.Point
import ru.spb.rollers.models.Route
import java.util.*


class EventsCreateFragment : Fragment(), PopupMenu.OnMenuItemClickListener {

    private var _binding: EventsCreateFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: EventsCreateViewModel
    private var routeList: MutableList<Route> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this)[EventsCreateViewModel::class.java]
        _binding = EventsCreateFragmentBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.imageButtonBack.setOnClickListener{
            MAIN.appViewModel.route = Route()
            MAIN.appViewModel.points.clear()
            MAIN.appViewModel.event = Event()
            MAIN.onSupportNavigateUp()
        }

        binding.txvTitle.text = titleEvents

        val btnAddRoute: MaterialButton = view.findViewById(R.id.btnAddRoute)
        btnAddRoute.setOnClickListener{ addRoute() }

        val btnSaveRoute: MaterialButton = view.findViewById(R.id.btnSaveRoute)
        btnSaveRoute.setOnClickListener{

            if (binding.etEventName.text.isNullOrEmpty() || binding.etEventDate.text.isNullOrEmpty()||
                    binding.etEventTime.text.isNullOrEmpty() || binding.etEventDescription.text.isNullOrEmpty()||
                    binding.etEventCost.text.isNullOrEmpty() || binding.etEventStartLocation.text.isNullOrEmpty()||
                    binding.etEventEndLocation.text.isNullOrEmpty() || binding.etEventSpeed.text.isNullOrEmpty()||
                    binding.etEventDistance.text.isNullOrEmpty()) {
                Toast.makeText(activity, "Заполнены не все поля",
                    Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (MAIN.appViewModel.event.id == ""){
                val eventKey = REF_DATABASE_EVENT.push().key
                MAIN.appViewModel.event.id = eventKey.toString()
            }

            MAIN.appViewModel.event.managerId = MAIN.appViewModel.user.id
            MAIN.appViewModel.event.name = binding.etEventName.text.toString()
            MAIN.appViewModel.event.date = binding.etEventDate.text.toString()
            MAIN.appViewModel.event.time = binding.etEventTime.text.toString()
            MAIN.appViewModel.event.description = binding.etEventDescription.text.toString()
            MAIN.appViewModel.event.cost = binding.etEventCost.text.toString()
            MAIN.appViewModel.event.speed = binding.etEventSpeed.text.toString()

            REF_DATABASE_EVENT.child(MAIN.appViewModel.event.id).setValue(MAIN.appViewModel.event)

            REF_DATABASE_EVENT.child(MAIN.appViewModel.event.id).child("route")
                .setValue(MAIN.appViewModel.route)

            for (p in MAIN.appViewModel.points){
                REF_DATABASE_EVENT.child(MAIN.appViewModel.event.id)
                    .child("route")
                    .child("Points")
                    .child(p.id).setValue(p)
            }

            MAIN.appViewModel.route = Route()
            MAIN.appViewModel.points.clear()
            MAIN.appViewModel.event = Event()
            Toast.makeText(activity, "Мероприятие сохранено",
                Toast.LENGTH_SHORT).show()
            MAIN.onSupportNavigateUp()
        }

        binding.imageButtonDeleteEvent.setOnClickListener{
            val builderDeleteDialog: AlertDialog.Builder = AlertDialog.Builder(MAIN)
            builderDeleteDialog
                .setTitle("Вы уверены, что хотите удалить мероприятие?")
                .setCancelable(false)
                .setPositiveButton("Да") { _, _ ->
                    MAIN.onSupportNavigateUp()
                    MAIN.appViewModel.route = Route()
                    MAIN.appViewModel.points.clear()
                    MAIN.appViewModel.event = Event()
                    Toast.makeText(MAIN, "Мероприятие удалено", Toast.LENGTH_SHORT).show()
                }
                .setNegativeButton("Отмена"){dialog, _ ->
                    dialog.cancel()
                }
            val alertDialogDeletePhoto: AlertDialog = builderDeleteDialog.create()
            alertDialogDeletePhoto.show()
        }

        binding.ivEventPhoto.setOnClickListener{
            showPopup(binding.ivChangePhoto)
        }

        binding.etEventOrganizer.text = Editable.Factory.getInstance().newEditable(MAIN.appViewModel.user.schoolName)

        binding.etEventDate.setOnClickListener { setDate() }
        binding.etEventTime.setOnClickListener { setTime() }

        if (MAIN.appViewModel.route.id != ""){
            setRouteAndPoints(MAIN.appViewModel.route)
            changeVisibilityRoute()
        }

        initRouteList()
    }

    private fun changeImage(){
        CropImage.activity()
            .setAspectRatio(1, 1)
            .setRequestedSize(600, 600)
            .setCropShape(CropImageView.CropShape.OVAL)
            .start(MAIN, this)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE
            && resultCode == Activity.RESULT_OK && data != null){
            val uri = CropImage.getActivityResult(data).uri
            val path = REF_STORAGE_ROOT.child(FOLDER_EVENT_IMAGE)
            path.putFile(uri).addOnCompleteListener { task1 ->
                if (task1.isSuccessful) {
                    path.downloadUrl.addOnCompleteListener { task2 ->
                        if (task2.isSuccessful) {
                            val photoUrl = task2.result.toString()

                            if (MAIN.appViewModel.event.id == ""){
                                val eventKey = REF_DATABASE_EVENT.push().key
                                MAIN.appViewModel.event.id = eventKey.toString()
                            }

                            REF_DATABASE_EVENT.child(MAIN.appViewModel.event.id)
                                .child("photo").setValue(photoUrl)
                                .addOnCompleteListener {
                                    if (it.isSuccessful) {
                                        Picasso.get()
                                            .load(photoUrl)
                                            .placeholder(R.drawable.avatar)
                                            .into(binding.ivEventPhoto)
                                        MAIN.appViewModel.event.photo = photoUrl
                                    }
                                }
                        }
                    }
                }
            }
        }
    }

    private fun setDate() {
        val calendar: Calendar = Calendar.getInstance()
        val mYear = calendar.get(Calendar.YEAR)
        val mMonth = calendar.get(Calendar.MONTH)
        val mDay = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            MAIN,
            { _, year, monthOfYear, dayOfMonth ->
                val formattedDate = String.format("%02d.%02d.%04d", dayOfMonth, monthOfYear, year)
                binding.etEventDate.setText(formattedDate) },
            mYear,
            mMonth,
            mDay
        )
        datePickerDialog.show()
    }

    private fun setTime() {
        val calendar: Calendar = Calendar.getInstance()
        val mHour = calendar.get(Calendar.HOUR_OF_DAY)
        val mMinute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(
            MAIN,
            { _, hourOfDay, minute ->
                val formattedTime = String.format("%02d:%02d", hourOfDay, minute)
                binding.etEventTime.setText(formattedTime) },
            mHour,
            mMinute,
            true
        )
        timePickerDialog.show()
    }

    private fun initRouteList() {
        val ref = REF_DATABASE_ROUTE
            .child(MAIN.appViewModel.user.id)

        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val listRoute = snapshot.children.map { it.getRouteModel() }
                routeList = listRoute.toMutableList()
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }

    private fun addRoute(){
        val builder = AlertDialog.Builder(MAIN)
        builder.setTitle("Выберите машрут")
            .setSingleChoiceItems(routeList.map { route ->
                "${route.name} (${route.distance} км)"
            }.toTypedArray(), -1
            ) { _, _ -> }
            .setPositiveButton("OK"
            ) { dialog, _ ->
                if (routeList.isEmpty()){
                    Toast.makeText(activity, "Нет построенных маршрутов",
                        Toast.LENGTH_SHORT).show()
                }
                else{
                    changeVisibilityRoute()
                    val selectedPosition = (dialog as AlertDialog).listView.checkedItemPosition
                    if (selectedPosition != -1) {
                        MAIN.appViewModel.route = routeList[selectedPosition]
                        setRouteAndPoints(routeList[selectedPosition])
                        Toast.makeText(activity, "Маршрут обновлен",
                            Toast.LENGTH_SHORT).show()
                    }
                }
            }
        builder.create()
        builder.show()
    }

    private fun setRouteAndPoints(route: Route){
        val refMessages = REF_DATABASE_ROUTE
            .child(MAIN.appViewModel.user.id)
            .child(route.id!!)
            .child("Points")
        refMessages.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists())
                {
                    val points = snapshot.children.map { it.getPointModel() }
                    val firstPoint = points.first()
                    val lastPoint = points.last()
                    binding.etEventStartLocation.setText(firstPoint.displayName)
                    binding.etEventEndLocation.setText(lastPoint.displayName)
                    MAIN.appViewModel.points = points as MutableList<Point>
                }
            }
            override fun onCancelled(error: DatabaseError) {}
        })
        binding.etEventDistance.setText("${route.distance} км")
    }

    private fun changeVisibilityRoute(){
        binding.tvEventStartLocation.visibility = View.VISIBLE
        binding.etEventStartLocation.visibility = View.VISIBLE
        binding.vDividerEventStartLocation.visibility = View.VISIBLE

        binding.tvEventEndLocation.visibility = View.VISIBLE
        binding.etEventEndLocation.visibility = View.VISIBLE
        binding.vDividerEventEndLocation.visibility = View.VISIBLE

        binding.tvEventDistance.visibility = View.VISIBLE
        binding.etEventDistance.visibility = View.VISIBLE
        binding.vDividerEventDistance.visibility = View.VISIBLE

        binding.btnAddRoute.text = "Изменить маршрут"
        binding.btnSaveRoute.visibility = View.VISIBLE
    }

    @SuppressLint("RestrictedApi")
    private fun showPopup(imagePhoto: ImageView) {
        val popupMenu = PopupMenu(MAIN, imagePhoto)
        popupMenu.inflate(R.menu.profile_photo_popup_menu)
        popupMenu.setOnMenuItemClickListener(this)

        val menuHelper = MenuPopupHelper(MAIN,
            popupMenu.menu as MenuBuilder, imagePhoto)
        menuHelper.setForceShowIcon(true)
        menuHelper.show()
    }

    override fun onMenuItemClick(menuItem: MenuItem?): Boolean {
        when (menuItem?.itemId) {
            R.id.changePhoto -> {
                changeImage()
            }
            R.id.deletePhoto -> {
                val builderDeleteDialog: AlertDialog.Builder = AlertDialog.Builder(MAIN)
                builderDeleteDialog
                    .setTitle("Вы уверены, что хотите удалить изображение?")
                    .setCancelable(false)
                    .setPositiveButton("Да") { _, _ ->
                        val path = REF_STORAGE_ROOT.child(FOLDER_EVENT_IMAGE)
                        path.delete().addOnSuccessListener {
                            MAIN.appViewModel.event.photo = ""
                            if (MAIN.appViewModel.event.id != "")
                                REF_DATABASE_EVENT.child(MAIN.appViewModel.event.id).child("photo")
                                    .removeValue()

                            Toast.makeText(MAIN, "Изображение удалено", Toast.LENGTH_SHORT).show()
                            binding.ivEventPhoto.setImageResource(R.drawable.rollers)
                        }.addOnFailureListener {
                            Toast.makeText(MAIN, it.message, Toast.LENGTH_SHORT).show()
                        }
                    }
                    .setNegativeButton("Отмена"){dialog, _ ->
                        dialog.cancel()
                    }
                val alertDialogDeletePhoto: AlertDialog = builderDeleteDialog.create()
                alertDialogDeletePhoto.show()
            }
        }
        return false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}