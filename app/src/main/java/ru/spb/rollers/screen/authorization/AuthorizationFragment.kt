package ru.spb.rollers.screen.authorization

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.spb.rollers.MAIN
import ru.spb.rollers.R
import ru.spb.rollers.databinding.AuthorizationFragmentBinding
import ru.spb.rollers.roleId

class AuthorizationFragment : Fragment() {

    private var _binding: AuthorizationFragmentBinding? = null
    private val binding get() = _binding!!

    companion object {
        fun newInstance() = AuthorizationFragment()
    }

    private lateinit var viewModel: AuthorizationViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = AuthorizationFragmentBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.mbRegister.setOnClickListener{
            MAIN.navController.navigate(R.id.action_authorizationFragment_to_registrationFragment)
        }

        binding.mbLogin.setOnClickListener {
            when (roleId) {
                1 -> {
                    MAIN.navController.navigate(R.id.action_authorizationFragment_to_usersFragment)
                }
                2, 3 -> {
                    MAIN.navController.navigate(R.id.action_authorizationFragment_to_events)
                }
            }

        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this)[AuthorizationViewModel::class.java]
        // TODO: Use the ViewModel
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}