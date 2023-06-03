package ru.spb.rollers.ui.eventscreate

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.text.Editable
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.view.menu.MenuBuilder
import androidx.appcompat.view.menu.MenuPopupHelper
import androidx.appcompat.widget.PopupMenu
import com.google.android.material.button.MaterialButton
import ru.spb.rollers.MAIN
import ru.spb.rollers.R
import ru.spb.rollers.databinding.EventsCreateFragmentBinding
import ru.spb.rollers.model.Route
import ru.spb.rollers.titleEvents

class EventsCreateFragment : Fragment(), PopupMenu.OnMenuItemClickListener {

    companion object {
        fun newInstance() = EventsCreateFragment()
    }

    private var _binding: EventsCreateFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: EventsCreateViewModel
    private var routeList: List<Route> = mutableListOf()

    private var title: String = "Изменение события"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = EventsCreateFragmentBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this)[EventsCreateViewModel::class.java]
        // TODO: Use the ViewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.imageButtonBack.setOnClickListener{
            MAIN.onSupportNavigateUp()
        }

        binding.txvTitle.text = titleEvents

        setInitialData()

        val btnAddRoute: MaterialButton = view.findViewById(R.id.btnAddRoute)
        btnAddRoute.setOnClickListener{ addRoute() }

        val btnSaveRoute: MaterialButton = view.findViewById(R.id.btnSaveRoute)
        btnSaveRoute.setOnClickListener{
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
                    Toast.makeText(MAIN, "Мероприятие удалено", Toast.LENGTH_SHORT).show()
                }
                .setNegativeButton("Отмена"){dialog, _ ->
                    dialog.cancel()
                }
            val alertDialogDeletePhoto: AlertDialog = builderDeleteDialog.create()
            alertDialogDeletePhoto.show()
        }

        binding.ivEventPhoto.setOnClickListener{
            showPopup(binding.imageViewChangePhoto)
        }
    }
    private fun addRoute(){
        val builder = AlertDialog.Builder(MAIN)
        builder.setTitle("Выберите машрут")
            .setSingleChoiceItems(routeList.map { route ->
                "${route.routeName} (${route.routeStartLocation} - ${route.routeEndLocation})"
            }.toTypedArray(), -1
            ) { _, item ->
                binding.tvEventStartLocation.visibility = View.VISIBLE
                binding.etEventStartLocation.visibility = View.VISIBLE
                binding.etEventStartLocation.text =
                    Editable.Factory.getInstance().newEditable(routeList[item].routeStartLocation)
                binding.vDividerEventStartLocation.visibility = View.VISIBLE

                binding.tvEventEndLocation.visibility = View.VISIBLE
                binding.etEventEndLocation.visibility = View.VISIBLE
                binding.etEventEndLocation.text =
                    Editable.Factory.getInstance().newEditable(routeList[item].routeEndLocation)
                binding.vDividerEventEndLocation.visibility = View.VISIBLE

                binding.tvEventDistance.visibility = View.VISIBLE
                binding.etEventDistance.visibility = View.VISIBLE
                binding.etEventDistance.text =
                    Editable.Factory.getInstance().newEditable(routeList[item].routeDistance)
                binding.vDividerEventDistance.visibility = View.VISIBLE

                binding.btnAddRoute.text = "Изменить маршрут"
                binding.btnSaveRoute.visibility = View.VISIBLE
            }
            .setPositiveButton("OK"
            ) { _, _ ->
                Toast.makeText(activity, "Маршрут обновлен",
                    Toast.LENGTH_SHORT).show()
            }
        builder.create()
        builder.show()

    }

    private fun setInitialData() {
        routeList += Route(
            1, "Маршрут № 1", "Васька",
            "Петроградка",  "15 км")
        routeList += Route(
            2, "Маршрут № 2", "Васька",
            "Петроградка",  "15 км")
        routeList += Route(
            3, "Маршрут № 3", "Васька",
            "Петроградка",  "15 км")
        routeList += Route(
            4, "Маршрут № 4", "Васька",
            "Петроградка",  "15 км")
        routeList += Route(
            5, "Маршрут № 5", "Васька",
            "Петроградка",  "15 км")
        routeList += Route(
            6, "Маршрут № 6", "Васька",
            "Петроградка",  "15 км")
        routeList += Route(
            7, "Маршрут № 7", "Васька",
            "Петроградка",  "15 км")
        routeList += Route(
            8, "Маршрут № 8", "Васька",
            "Петроградка",  "15 км")
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
                Toast.makeText(MAIN, "Изображение изменено", Toast.LENGTH_SHORT).show()
            }
            R.id.deletePhoto -> {
                val builderDeleteDialog: AlertDialog.Builder = AlertDialog.Builder(MAIN)
                builderDeleteDialog
                    .setTitle("Вы уверены, что хотите удалить изображение?")
                    .setCancelable(false)
                    .setPositiveButton("Да") { _, _ ->
                        Toast.makeText(MAIN, "Изображение удалено", Toast.LENGTH_SHORT).show()
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