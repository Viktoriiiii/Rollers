package ru.spb.rollers.screens

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.spb.rollers.MAIN
import ru.spb.rollers.R
import ru.spb.rollers.databinding.FragmentViewRouteBinding

class ViewRoute : Fragment() {

    private lateinit var binding: FragmentViewRouteBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentViewRouteBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.imageViewBack.setOnClickListener{
            MAIN.navController.navigate(R.id.action_viewRoute_to_routes2)
        }
    }


}