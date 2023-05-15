package ru.spb.rollers.screens

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import ru.spb.rollers.MAIN
import ru.spb.rollers.R
import ru.spb.rollers.databinding.FragmentEventViewBinding

class EventView : Fragment() {

    private lateinit var binding: FragmentEventViewBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEventViewBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    @SuppressLint("MissingInflatedId")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.imageButtonBack.setOnClickListener{
            MAIN.navController.navigate(R.id.action_eventView_to_events2)
        }

        binding.imageButtonViewManager.setOnClickListener {
            val builderViewProfile: AlertDialog.Builder = AlertDialog.Builder(MAIN)
            val profileView: View = MAIN.layoutInflater.inflate(R.layout.view_profile, null)
            val imageViewClose: ImageView = profileView.findViewById(R.id.imageViewClose)
            builderViewProfile.setView(profileView)
            val alertViewProfile: AlertDialog = builderViewProfile.create()
            alertViewProfile.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            alertViewProfile.setOnShowListener {
                imageViewClose.setOnClickListener { alertViewProfile.cancel() }
            }
            alertViewProfile.show()
        }

        binding.imageButtonViewRoute.setOnClickListener{
            MAIN.navController.navigate(R.id.action_eventView_to_viewRouteOnEvent)
        }

        binding.imageButtonViewParticipants.setOnClickListener{
            MAIN.navController.navigate(R.id.action_eventView_to_viewParticipantsOnEvent)
        }
    }
}