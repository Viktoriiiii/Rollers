package ru.spb.rollers.screens

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import ru.spb.rollers.MAIN
import ru.spb.rollers.R
import ru.spb.rollers.databinding.FragmentRegistrationBinding
import ru.spb.rollers.model.Contact

class Registration : Fragment() {

    lateinit var binding: FragmentRegistrationBinding
    private var contact = Contact()
    private var step: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegistrationBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.mbBack.setOnClickListener{
            MAIN.navController.navigate(R.id.action_registration_to_authorization)
        }
        binding.mbRegister.setOnClickListener { registerOnStep() }
    }

    private fun registerOnStep(){
        when (step){
            0 -> {
                contact.contactLogin = binding.mavLogin.text.toString()
                contact.contactPassword = binding.mavPassword.text.toString()
                if (!contact.contactLogin.isNullOrEmpty() && !contact.contactPassword.isNullOrEmpty())
                {
                    binding.mbRegister.text = "Далее"
                    binding.mavPasswordAdmit.visibility = View.GONE
                    binding.mavLogin.visibility = View.GONE
                    binding.mavPassword.visibility = View.GONE
                    binding.etFirstName.visibility = View.VISIBLE
                    binding.etLastName.visibility = View.VISIBLE
                    step = 1
                } else{
                    Toast.makeText(MAIN, "Поля заполнены неверно", Toast.LENGTH_SHORT).show()
                    step = 0
                }
            }
            1 -> {
                contact.contactFirstName = binding.etFirstName.text.toString()
                contact.contactLastName = binding.etLastName.text.toString()

                if (!contact.contactFirstName.isNullOrEmpty() || !contact.contactLastName.isNullOrEmpty())
                {
                    binding.etFirstName.visibility = View.GONE
                    binding.etLastName.visibility = View.GONE
                    binding.etDistrict.visibility = View.VISIBLE
                    binding.txvGender.visibility = View.VISIBLE
                    binding.switchGender.visibility = View.VISIBLE
                    step = 2
                } else {
                    Toast.makeText(MAIN, "Поля заполнены неверно", Toast.LENGTH_SHORT).show()
                    step = 1
                }
            }
            2 -> {
                contact.contactDistrict = binding.etDistrict.text.toString()
                contact.contactGender = binding.switchGender.isChecked

                if (!contact.contactDistrict.isNullOrEmpty()){
                    binding.etDistrict.visibility = View.GONE
                    binding.txvGender.visibility = View.GONE
                    binding.switchGender.visibility = View.GONE
                    binding.etBirthday.visibility = View.VISIBLE
                    binding.txvStatus.visibility = View.VISIBLE
                    binding.switchStatus.visibility = View.VISIBLE
                    step = 3
                } else {
                    Toast.makeText(MAIN, "Поля заполнены неверно", Toast.LENGTH_SHORT).show()
                    step = 2
                }
            }
            3 -> {
                contact.contactAge = binding.etBirthday.text.toString()
                contact.contactStatus = binding.switchStatus.isChecked

                if (!contact.contactAge.isNullOrEmpty()){
                    binding.etBirthday.visibility = View.GONE
                    binding.txvStatus.visibility = View.GONE
                    binding.switchStatus.visibility = View.GONE
                    binding.etNickName.visibility = View.VISIBLE
                    step = 4
                }else {
                    Toast.makeText(MAIN, "Поля заполнены неверно", Toast.LENGTH_SHORT).show()
                    step = 3
                }
            }
            4 -> {
                contact.contactPublicName = binding.etNickName.text.toString()

                if (!contact.contactPublicName.isNullOrEmpty())
                    MAIN.navController.navigate(R.id.action_registration_to_homePage)
                else {
                    Toast.makeText(MAIN, "Поля заполнены неверно", Toast.LENGTH_SHORT).show()
                    step = 4
                }
            }
        }
    }
}