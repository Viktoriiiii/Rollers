package ru.spb.rollers.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import ru.spb.rollers.*
import ru.spb.rollers.databinding.RegistrationFragmentBinding
import ru.spb.rollers.models.User

class RegistrationFragment : Fragment() {

    private var _binding: RegistrationFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = RegistrationFragmentBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.etEmail.setText(R.string.user_mail_ru)
        binding.etPassword.setText(R.string.input_pass)
        binding.etPasswordAdmit.setText(R.string.input_pass)

        binding.mbBack.setOnClickListener{
            MAIN.onSupportNavigateUp()
        }

        binding.checkBoxManager.setOnCheckedChangeListener{ checkBoxView, isChecked ->
            if (isChecked){
                val builderChangeRouteNameDialog: AlertDialog.Builder = AlertDialog.Builder(MAIN)
                builderChangeRouteNameDialog
                    .setTitle("Отправка письма")
                    .setCancelable(false)
                    .setPositiveButton("Отправить") { dialog, _ ->
                        dialog.dismiss()
                        Toast.makeText(MAIN, "Письмо отправлено", Toast.LENGTH_SHORT).show()
                    }
                    .setMessage("На ваш почтовый адрес будет отправлено письмо с информацией об оплате." +
                            "Вы можете начать пользоваться приложением уже сейчас. " +
                            "Полный функционал для роли оганизатора будет доступ после оплаты. ")
                    .setNegativeButton("Отмена"){dialog, _ ->
                        dialog.cancel()
                        checkBoxView.isChecked = false
                    }

                val alertDialogChangeRouteName: AlertDialog = builderChangeRouteNameDialog.create()
                alertDialogChangeRouteName.show()
            }
        }

        binding.mbRegister.setOnClickListener {
            registration()
        }
    }

    private fun registration(){
        val email  = binding.etEmail.text.toString()
        val password  = binding.etPassword.text.toString()
        val admitPass = binding.etPasswordAdmit.text.toString()

        if (password != admitPass) {
            Toast.makeText(MAIN, "Пароли не совпадают", Toast.LENGTH_SHORT).show()
            return
        }

        val role = if (binding.checkBoxManager.isChecked) "Организатор" else "Участник"
        val user = User(role = role, email = email, password= password, isManager = false)
        user.status = "Не активен"

        AUTH.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener{
                if (it.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    user.id = AUTH.currentUser?.uid.toString()
                    MAIN.appViewModel.user = user
                    REF_DATABASE_USER.child(user.id).setValue(user)
                    Toast.makeText(MAIN,"Добро пожаловать!", Toast.LENGTH_SHORT).show()
                    MAIN.navController.navigate(R.id.action_registrationFragment_to_events)
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(MAIN, it.exception?.message, Toast.LENGTH_SHORT).show()
                }
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}


