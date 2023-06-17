package ru.spb.rollers.ui.eventsview

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import ru.spb.rollers.*
import ru.spb.rollers.databinding.EventsViewFragmentBinding
import ru.spb.rollers.models.Event
import ru.spb.rollers.models.Route
import ru.spb.rollers.models.User

class EventsViewFragment : Fragment() {

    private var _binding: EventsViewFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: EventsViewViewModel
    private var manager = User()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this)[EventsViewViewModel::class.java]
        _binding = EventsViewFragmentBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.imageButtonBack.setOnClickListener{
            MAIN.appViewModel.event = Event()
            MAIN.appViewModel.route = Route()
            MAIN.onSupportNavigateUp()
        }

        binding.imageButtonViewManager.setOnClickListener {
            val builderViewProfile: AlertDialog.Builder = AlertDialog.Builder(MAIN)
            val profileView: View = MAIN.layoutInflater.inflate(R.layout.view_profile_school, null)
            val imageViewClose: ImageView = profileView.findViewById(R.id.imageViewClose)

            val txvSchoolName: TextView? = profileView.findViewById(R.id.txvSchoolName)
            val txvDescription: TextView? = profileView.findViewById(R.id.txvDescription)
            val txvSchoolAddress: TextView? = profileView.findViewById(R.id.txvSchoolAddress)
            val txvSchoolPhone: TextView? = profileView.findViewById(R.id.txvSchoolPhone)

            txvSchoolName?.text = if (manager.schoolName.isNullOrEmpty()) "Неизвестный организатор"
            else manager.schoolName
            txvDescription?.text = if (manager.description.isNullOrEmpty()) "Описание не добалено"
            else manager.description

            if (manager.address.isNullOrEmpty())
                txvSchoolAddress?.text = "Адрес не известен"
            else
                txvSchoolAddress?.text = manager.address

            if (manager.phone.isNullOrEmpty())
                txvSchoolPhone?.text = "Телефон не известен"
            else
                txvSchoolPhone?.text = manager.phone

            val ivPhoto: ImageView? = profileView.findViewById(R.id.ivPhoto)
            if (ivPhoto != null) {
                Glide.with(view.context)
                    .load(manager.photo)
                    .placeholder(R.drawable.avatar)
                    .into(ivPhoto)
            }
            builderViewProfile.setView(profileView)
            val alertViewProfile: AlertDialog = builderViewProfile.create()
            alertViewProfile.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            alertViewProfile.setOnShowListener {
                imageViewClose.setOnClickListener { alertViewProfile.cancel() }
            }
            alertViewProfile.show()
        }

        binding.imageButtonViewRoute.setOnClickListener{
            MAIN.navController.navigate(R.id.action_eventsViewFragment_to_eventsViewRouteFragment)
        }

        binding.ibViewParticipants.setOnClickListener{
            MAIN.navController.navigate(R.id.action_eventsViewFragment_to_eventParticipantFragment)
        }

        if (MAIN.appViewModel.event.id != ""){
            getManager()
            getRoute()
            binding.tvEventName.text = MAIN.appViewModel.event.name
            binding.tvEventDate.text = "Дата и время: ${MAIN.appViewModel.event.date} ${MAIN.appViewModel.event.time}"
            binding.tvEventDescription.text = "Описание: ${MAIN.appViewModel.event.description}"
            binding.tvEventCost.text = "Стоимость: " + if (MAIN.appViewModel.event.cost == 0.0) "Бесплатно"
                else "${MAIN.appViewModel.event.cost} руб."
            binding.tvSpeed.text = "Скорость: ${MAIN.appViewModel.event.speed} км/ч"
        }
    }

    private fun getManager(){
        REF_DATABASE_USER.child(MAIN.appViewModel.event.managerId).addListenerForSingleValueEvent(object : ValueEventListener{
            @SuppressLint("SetTextI18n")
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    manager = snapshot.getUserModel()
                    binding.tvManager.text = "Организатор: ${manager.schoolName}"
                }
            }
            override fun onCancelled(error: DatabaseError) { }
        })
    }

    private fun getRoute(){
        REF_DATABASE_EVENT.child(MAIN.appViewModel.event.id).child("route")
            .addListenerForSingleValueEvent(object : ValueEventListener{
            @SuppressLint("SetTextI18n")
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    MAIN.appViewModel.route = snapshot.getRouteModel()
                    binding.tvEventNameRoute.text = "Маршрут: ${MAIN.appViewModel.route.name}"
                    binding.tvEventDistance.text = "Дистанция: ${MAIN.appViewModel.route.distance} км."
                }
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}