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
import com.google.firebase.auth.EmailAuthProvider
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

        val switchStatus: SwitchCompat = view.findViewById(R.id.switchStatus)
        switchStatus.setOnCheckedChangeListener {  buttonView, isChecked ->
            buttonView.text = if (isChecked) "На роликах" else "Не активен"
        }

        binding.btnSaveChanges.setOnClickListener{
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
    }

    private fun showInfoInProfile(){
        if (MAIN.appViewModel.user.role == "Организатор") {
            binding.llUser.visibility = View.GONE
            binding.etSchoolName.setText(MAIN.appViewModel.user.userSchoolName)
            binding.etSchoolDescription.setText(MAIN.appViewModel.user.userDescription)
            binding.etSchoolAddress.setText(MAIN.appViewModel.user.userAddress)
        }
        if (MAIN.appViewModel.user.role == "Участник") {
            binding.llSchool.visibility = View.GONE
            binding.etFirstName.setText(MAIN.appViewModel.user.userFirstName)
            binding.etLastName.setText(MAIN.appViewModel.user.userLastName)
            binding.switchStatus.isChecked = MAIN.appViewModel.user.userStatus != "Не активен"
            binding.switchStatus.text = MAIN.appViewModel.user.userStatus
            binding.etDistrict.setText(MAIN.appViewModel.user.userDistrict)
            binding.etBirthday.setText(MAIN.appViewModel.user.userBirthday)
            binding.etGender.setText(MAIN.appViewModel.user.userGender)
        }

        binding.etPhone.setText(MAIN.appViewModel.user.userPhone)
        binding.etEmail.setText(MAIN.appViewModel.user.userEmail)
        binding.etPassword.setText(MAIN.appViewModel.user.userPassword)
    }

    private fun saveChangesInProfile(){

        // Проверки смены пароля


        MAIN.appViewModel.user.userEmail = binding.etEmail.text.toString()
        MAIN.appViewModel.user.userId = MAIN.appViewModel.AUTH.currentUser!!.uid
        MAIN.appViewModel.user.userFirstName = binding.etFirstName.text.toString()
        MAIN.appViewModel.user.userLastName = binding.etLastName.text.toString()
        MAIN.appViewModel.user.userStatus = binding.switchStatus.text.toString()
        MAIN.appViewModel.user.userDistrict = binding.etDistrict.text.toString()
        MAIN.appViewModel.user.userBirthday = binding.etBirthday.text.toString()
        MAIN.appViewModel.user.userGender = binding.etGender.text.toString()

        MAIN.appViewModel.user.userPhone = binding.etPhone.text.toString()
        MAIN.appViewModel.user.userPassword = binding.etPassword.text.toString()

        MAIN.appViewModel.user.userSchoolName = binding.etSchoolName.text.toString()
        MAIN.appViewModel.user.userDescription = binding.etSchoolDescription.text.toString()
        MAIN.appViewModel.user.userAddress = binding.etSchoolAddress.text.toString()

        changeEmailAndApssword()

        val userValues = MAIN.appViewModel.user.toMap()
        val childUpdates = hashMapOf<String, Any>(
            "/User/${MAIN.appViewModel.user.userId}" to userValues
        )

        Toast.makeText(MAIN, "Данные обновлены", Toast.LENGTH_SHORT).show()
        MAIN.appViewModel.REF_DATABASE_ROOT.updateChildren(childUpdates)
    }

    private fun changeEmailAndApssword(){
        val credential =
            EmailAuthProvider.getCredential(MAIN.appViewModel.user.userEmail!!,
                MAIN.appViewModel.user.userPassword!!
            )

        MAIN.appViewModel.AUTH.currentUser?.updateEmail(binding.etEmail.text.toString())
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    MAIN.appViewModel.AUTH.currentUser?.updatePassword(binding.etPassword.text.toString())
                        ?.addOnCompleteListener { task3 ->
                            if (task3.isSuccessful) {
                                MAIN.appViewModel.AUTH.currentUser?.reauthenticate(credential)
                                    ?.addOnCompleteListener { task2 ->
                                        if (!task2.isSuccessful) {
                                            Toast.makeText(MAIN, task2.exception?.message, Toast.LENGTH_SHORT)
                                                .show()
                                        }
                                    }
                            }
                            else {
                                Toast.makeText(MAIN, task.exception?.message, Toast.LENGTH_SHORT).show()
                            }
                        }
                }
                else {
                    Toast.makeText(MAIN, task.exception?.message, Toast.LENGTH_SHORT).show()
                }
            }
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