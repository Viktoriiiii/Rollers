package ru.spb.rollers.screens

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.spb.rollers.MAIN
import ru.spb.rollers.R
import ru.spb.rollers.databinding.FragmentViewRouteOnEventBinding

class ViewRouteOnEvent : Fragment() {

    private lateinit var binding: FragmentViewRouteOnEventBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentViewRouteOnEventBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.imageViewBack.setOnClickListener{
            MAIN.navController.navigate(R.id.action_viewRouteOnEvent_to_eventView)
        }
    }


}