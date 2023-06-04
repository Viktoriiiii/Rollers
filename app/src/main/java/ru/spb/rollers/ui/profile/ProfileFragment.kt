package ru.spb.rollers.ui.profile

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.view.menu.MenuBuilder
import androidx.appcompat.view.menu.MenuPopupHelper
import androidx.appcompat.widget.PopupMenu
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.button.MaterialButton
import okhttp3.*
import ru.spb.rollers.*
import ru.spb.rollers.databinding.ProfileFragmentBinding

class ProfileFragment : Fragment(), PopupMenu.OnMenuItemClickListener {

    private var _binding: ProfileFragmentBinding? = null
    private val binding get() = _binding!!

//    val weatherConditions = mapOf(
//        "clear" to "Ясно",
//        "partly-cloudy" to "Малооблачно",
//        "cloudy" to "Облачно с прояснениями",
//        "overcast" to "Пасмурно",
//        "drizzle" to "Морось",
//        "light-rain" to "Небольшой дождь",
//        "rain" to "Дождь",
//        "moderate-rain" to "Умеренно сильный дождь",
//        "heavy-rain" to "Сильный дождь",
//        "continuous-heavy-rain" to "Длительный сильный дождь",
//        "showers" to "Ливень",
//        "wet-snow" to "Дождь со снегом",
//        "light-snow" to "Небольшой снег",
//        "snow" to "Снег",
//        "snow-showers" to "Снегопад",
//        "hail" to "Град",
//        "thunderstorm" to "Гроза",
//        "thunderstorm-with-rain" to "Дождь с грозой",
//        "thunderstorm-with-hail" to "Гроза с градом"
//    )

    companion object {
        fun newInstance() = ProfileFragment()
    }

    private lateinit var viewModel: ProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ProfileFragmentBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[ProfileViewModel::class.java]
        showInfoInProfile()

        val switchGender: SwitchCompat = view.findViewById(R.id.switchGender)
        switchGender.setOnCheckedChangeListener{buttonView, isChecked ->
            buttonView.text = if (isChecked) "Мужской" else "Женский"
        }

        val switchStatus: SwitchCompat = view.findViewById(R.id.switchStatus)
        switchStatus.setOnCheckedChangeListener {  buttonView, isChecked ->
            buttonView.text = if (isChecked) "На роликах" else "Не активен"
        }

        val btnSaveChanges: MaterialButton = view.findViewById(R.id.btnSaveChanges)
        btnSaveChanges.setOnClickListener{
            saveChangesInProfile()
        }

        val imagePhoto: ImageView = view.findViewById(R.id.imageViewPhoto)
        imagePhoto.setOnClickListener{
            showPopup(imagePhoto)
        }

        binding.imageViewExit.setOnClickListener{
            MAIN.navController.navigate(R.id.action_profile_to_authorizationFragment)
            MAIN.finish()
            startActivity(Intent(MAIN, AppActivity::class.java)  )
            delayMillis = 0
        }

        if (roleId == 2)
            binding.llUser.visibility = View.GONE
        if (roleId == 3)
            binding.llSchool.visibility = View.GONE
    }

    private fun showInfoInProfile(){
        binding.etPublicName.setText(MAIN.appViewModel.user.userPublicName)
        binding.etFirstName.setText(MAIN.appViewModel.user.userFirstName)
        binding.etLastName.setText(MAIN.appViewModel.user.userLastName)

        binding.switchStatus.text = MAIN.appViewModel.user.userStatus // !!! Переделать

        binding.etDistrict.setText(MAIN.appViewModel.user.userDistrict)
        binding.etBirthday.setText(MAIN.appViewModel.user.userBirthday)

        binding.etGender.setText(MAIN.appViewModel.user.userGender) // !!! Переделать

        binding.etPhone.setText(MAIN.appViewModel.user.userPhone)
        binding.etEmail.setText(MAIN.appViewModel.user.userEmail)
        binding.etPassword.setText(MAIN.appViewModel.user.userPassword)



    }

    private fun saveChangesInProfile(){

        // Проверки поле логина, пароля и публичного имени



        MAIN.appViewModel.user.userId = AUTH.currentUser!!.uid
        MAIN.appViewModel.user.userPublicName = binding.etPublicName.text.toString()
        MAIN.appViewModel.user.userFirstName = binding.etFirstName.text.toString()
        MAIN.appViewModel.user.userLastName = binding.etLastName.text.toString()
        MAIN.appViewModel.user.userStatus = binding.switchStatus.text.toString()
        MAIN.appViewModel.user.userDistrict = binding.etDistrict.text.toString()
        MAIN.appViewModel.user.userBirthday = binding.etBirthday.text.toString()
        MAIN.appViewModel.user.userGender = binding.etGender.text.toString()
        MAIN.appViewModel.user.userPhone = binding.etPhone.text.toString()
        MAIN.appViewModel.user.userEmail = binding.etEmail.text.toString()
        MAIN.appViewModel.user.userPassword = binding.etPassword.text.toString()
       // MAIN.appViewModel.user.role = REF_DATABASE_ROOT.child("User").child(user.userId).child("role").key.toString()

        AUTH.currentUser?.let {
            REF_DATABASE_ROOT
                .child("User")
                .child(MAIN.appViewModel.user.userId)
                .setValue(MAIN.appViewModel.user)
       //     AUTH.updateCurrentUser(it)
        }

//        AUTH.createUserWithEmailAndPassword(email, password)
//            .addOnCompleteListener{
//                if (it.isSuccessful) {
//                    // Sign in success, update UI with the signed-in user's information
//                    user.userId = AUTH.currentUser?.uid.toString()
//                    MAIN.appViewModel.user = user
//                    REF_DATABASE_ROOT.child("User").child(user.userId).setValue(user)
//                    Toast.makeText(MAIN,"Добро пожаловать!", Toast.LENGTH_SHORT).show()
//                    MAIN.navController.navigate(R.id.action_registrationFragment_to_events)
//                } else {
//                    // If sign in fails, display a message to the user.
//                    Toast.makeText(MAIN, it.exception?.message, Toast.LENGTH_SHORT).show()
//                }
//            }

        Toast.makeText(MAIN, "Изменения сохранены", Toast.LENGTH_SHORT).show()
    }

    @SuppressLint("RestrictedApi")
    private fun showPopup(imagePhoto: ImageView) {
        val popupMenu = PopupMenu(MAIN, imagePhoto)
        popupMenu.inflate(R.menu.profile_photo_popup_menu)
        popupMenu.setOnMenuItemClickListener(this)

        val menuHelper = MenuPopupHelper(MAIN,
            popupMenu.menu as MenuBuilder, imagePhoto)
        menuHelper.setForceShowIcon(true)
        menuHelper.show()
    }

    override fun onMenuItemClick(menuItem: MenuItem?): Boolean {
        when (menuItem?.itemId) {
            R.id.changePhoto -> {
                Toast.makeText(MAIN, "Изображение изменено", Toast.LENGTH_SHORT).show()
            }
            R.id.deletePhoto -> {
                val builderDeleteDialog: AlertDialog.Builder = AlertDialog.Builder(MAIN)
                builderDeleteDialog
                    .setTitle("Вы уверены, что хотите удалить изображение?")
                    .setCancelable(false)
                    .setPositiveButton("Да") { _, _ ->
                        Toast.makeText(MAIN, "Изображение удалено", Toast.LENGTH_SHORT).show()
                    }
                    .setNegativeButton("Отмена"){dialog, _ ->
                        dialog.cancel()
                    }
                val alertDialogDeletePhoto: AlertDialog = builderDeleteDialog.create()
                alertDialogDeletePhoto.show()
            }
        }
        return false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}