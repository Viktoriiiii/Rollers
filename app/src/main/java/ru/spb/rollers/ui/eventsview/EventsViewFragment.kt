package ru.spb.rollers.ui.eventsview

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import ru.spb.rollers.MAIN
import ru.spb.rollers.R
import ru.spb.rollers.databinding.EventsViewFragmentBinding

class EventsViewFragment : Fragment() {

    private var _binding: EventsViewFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: EventsViewViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this)[EventsViewViewModel::class.java]
        _binding = EventsViewFragmentBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.imageButtonBack.setOnClickListener{
            MAIN.onSupportNavigateUp()
        }

        binding.imageButtonViewManager.setOnClickListener {
            val builderViewProfile: AlertDialog.Builder = AlertDialog.Builder(MAIN)
            val profileView: View = MAIN.layoutInflater.inflate(R.layout.view_profile_school, null)
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
            MAIN.navController.navigate(R.id.action_eventsViewFragment_to_eventsViewRouteFragment)
        }

        binding.imageButtonViewParticipants.setOnClickListener{
            MAIN.navController.navigate(R.id.action_eventsViewFragment_to_eventParticipantFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}