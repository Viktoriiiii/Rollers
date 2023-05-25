package ru.spb.rollers.screen

import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.spb.rollers.MAIN
import ru.spb.rollers.R
import ru.spb.rollers.databinding.LaunchFragmentBinding
import ru.spb.rollers.delayMillis

class Launch : Fragment() {

    private var _binding: LaunchFragmentBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = LaunchFragmentBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Handler().postDelayed({
            MAIN.navController.navigate(R.id.action_launch_to_authorizationFragment)
        }, delayMillis)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}