package ru.spb.rollers.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue
import ru.spb.rollers.*
import ru.spb.rollers.R
import ru.spb.rollers.databinding.AuthorizationFragmentBinding
import ru.spb.rollers.models.User

class AuthorizationFragment : Fragment() {

    private var _binding: AuthorizationFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = AuthorizationFragmentBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.etEmail.setText(R.string.user_mail_ru)
        binding.etPassword.setText(R.string.input_pass)

        binding.mbRegister.setOnClickListener{
            MAIN.navController.navigate(R.id.action_authorizationFragment_to_registrationFragment)
        }

        binding.mbLogin.setOnClickListener {
            val email  = binding.etEmail.text.toString()
            val password  = binding.etPassword.text.toString()

            AUTH.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener{
                    if (it.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        val user = AUTH.currentUser
                        user?.let {
                            val uid = user.uid
                            val userListener = object : ValueEventListener {
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    MAIN.appViewModel.user = snapshot.getValue<User>()!!
                                    REF_DATABASE_USER.child(uid).removeEventListener(this)

                                    Toast.makeText(MAIN,"Добро пожаловать!", Toast.LENGTH_SHORT).show()
                                    when (MAIN.appViewModel.user.role) {
                                        "Администратор" -> {
                                            MAIN.navController.navigate(R.id.action_authorizationFragment_to_usersFragment)
                                        }
                                        "Организатор", "Участник" -> {
                                            MAIN.navController.navigate(R.id.action_authorizationFragment_to_events)
                                        }
                                    }
                                }
                                override fun onCancelled(error: DatabaseError) {
                                    Toast.makeText( MAIN, error.toException().message, Toast.LENGTH_SHORT).show()
                                }
                            }
                            REF_DATABASE_USER.child(uid).addValueEventListener(userListener)
                        }

                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText( MAIN, it.exception?.message, Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}