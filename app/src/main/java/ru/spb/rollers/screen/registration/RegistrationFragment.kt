package ru.spb.rollers.screen.registration

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import ru.spb.rollers.MAIN
import ru.spb.rollers.R
import ru.spb.rollers.databinding.RegistrationFragmentBinding

class RegistrationFragment : Fragment() {

    private var _binding: RegistrationFragmentBinding? = null
    private val binding get() = _binding!!
    private var userEmail: String? = null

    companion object {
        fun newInstance() = RegistrationFragment()
    }

    private lateinit var viewModel: RegistrationViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = RegistrationFragmentBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this)[RegistrationViewModel::class.java]
        // TODO: Use the ViewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.mbBack.setOnClickListener{
            MAIN.onSupportNavigateUp()
        }

        binding.mbRegister.setOnClickListener {
            MAIN.navController.navigate(R.id.action_registrationFragment_to_events)
        }

        binding.checkBoxManager.setOnCheckedChangeListener{ checkBoxView, isChecked ->
            if (isChecked){
                val li = LayoutInflater.from(MAIN)
                val promptsView: View = li.inflate(R.layout.alert_dialog_change_name, null)
                val builderChangeRouteNameDialog: AlertDialog.Builder = AlertDialog.Builder(MAIN)
                builderChangeRouteNameDialog.setView(promptsView)
                val userInput: EditText = promptsView.findViewById(R.id.input_text)
                userInput.hint = "Электронная почта"
                builderChangeRouteNameDialog
                    .setTitle("Введите ваш почтовый адрес")
                    .setCancelable(false)
                    .setPositiveButton("Ок") { dialog, _ ->
                        val inputText = userInput.text.toString()
                        userEmail = inputText
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
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

