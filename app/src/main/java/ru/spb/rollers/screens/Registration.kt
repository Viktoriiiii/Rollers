package ru.spb.rollers.screens

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.spb.rollers.MAIN
import ru.spb.rollers.R
import ru.spb.rollers.databinding.FragmentAuthorizationBinding
import ru.spb.rollers.databinding.FragmentRegistrationBinding

class Registration : Fragment() {

    lateinit var binding: FragmentRegistrationBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegistrationBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.mbBack.setOnClickListener{
            MAIN.navController.navigate(R.id.action_registration_to_authorization)
        }
        binding.mbRegister.setOnClickListener {
            MAIN.navController.navigate(R.id.action_registration_to_homePage)
        }
    }
}