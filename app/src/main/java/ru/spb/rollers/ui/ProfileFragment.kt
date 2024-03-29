package ru.spb.rollers.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.view.menu.MenuBuilder
import androidx.appcompat.view.menu.MenuPopupHelper
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import com.google.firebase.auth.EmailAuthProvider
import com.squareup.picasso.Picasso
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import okhttp3.*
import ru.spb.rollers.*
import ru.spb.rollers.databinding.FragmentProfileBinding
import java.util.*

class ProfileFragment : Fragment(), PopupMenu.OnMenuItemClickListener {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val genders: Array<String> = arrayOf("Женский", "Мужской")
    private val districts: Array<String> = arrayOf("Адмиралтейский", "Василеостровский", "Выборгский",
        "Калининский", "Кировский", "Колпинский", "Красногвардейский", "Красносельский",
        "Кронштадтский", "Курортный", "Московский", "Невский", "Петроградский", "Петродворцовый",
        "Приморский", "Пушкинский", "Фрунзенский", "Центральный")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        MAIN.appViewModel.setPhoto(binding.ivMyPhoto)

        showInfoInProfile()

        binding.swStatus.setOnCheckedChangeListener {  buttonView, isChecked ->
            buttonView.text = if (isChecked) "На роликах" else "Не активен"
        }

        binding.btnSaveChanges.setOnClickListener{
            saveChangesInProfile()
        }

        binding.ivMyPhoto.setOnClickListener{
            showPopup(binding.ivMyPhoto)
        }

        binding.ivExit.setOnClickListener{
            MAIN.navController.navigate(R.id.action_profile_to_authorizationFragment)
            MAIN.finish()
            startActivity(Intent(MAIN, AppActivity::class.java)  )
            delayMillis = 0
        }

        binding.etGender.setOnClickListener {
            setText(genders, binding.etGender, "Выберите пол")
        }

        binding.etDistrict.setOnClickListener {
            setText(districts, binding.etDistrict, "Выберите район")
        }

        binding.etBirthday.setOnClickListener {
            setDate()
        }
    }

    private fun setDate() {
        val calendar: Calendar = Calendar.getInstance()
        val mYear = calendar.get(Calendar.YEAR)
        val mMonth = calendar.get(Calendar.MONTH)
        val mDay = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            MAIN,
            { _, year, monthOfYear, dayOfMonth ->
                val formattedDate = String.format("%02d.%02d.%04d", dayOfMonth, monthOfYear + 1, year)
                binding.etBirthday.setText(formattedDate) },
            mYear,
            mMonth,
            mDay
        )
        datePickerDialog.show()
    }

    private fun setText(arrays: Array<String>, et: EditText, title: String){
        val builder = AlertDialog.Builder(MAIN)
        builder.setTitle(title)
            .setSingleChoiceItems(arrays, -1
            ) { _, _ -> }
            .setPositiveButton("OK"
            ) { dialog, _ ->
                val selectedPosition = (dialog as AlertDialog).listView.checkedItemPosition
                if (selectedPosition != -1) {
                    et.setText(arrays[selectedPosition])
                }
            }
        builder.create()
        builder.show()
    }

    private fun showInfoInProfile(){
        if (MAIN.appViewModel.user.role == "Организатор") {
            binding.llUser.visibility = View.GONE
            binding.etSchoolName.setText(MAIN.appViewModel.user.schoolName)
            binding.etSchoolDescription.setText(MAIN.appViewModel.user.description)
            binding.etSchoolAddress.setText(MAIN.appViewModel.user.address)
        }
        if (MAIN.appViewModel.user.role == "Участник") {
            binding.llSchool.visibility = View.GONE
            binding.etFirstName.setText(MAIN.appViewModel.user.firstName)
            binding.etLastName.setText(MAIN.appViewModel.user.lastName)
            binding.swStatus.isChecked = MAIN.appViewModel.user.status != "Не активен"
            binding.swStatus.text = MAIN.appViewModel.user.status
            binding.etDistrict.setText(MAIN.appViewModel.user.district)
            binding.etBirthday.setText(MAIN.appViewModel.user.birthday)
            binding.etGender.setText(MAIN.appViewModel.user.gender)
        }

        binding.etPhone.setText(MAIN.appViewModel.user.phone)
        binding.etEmail.setText(MAIN.appViewModel.user.email)
        binding.etPassword.setText(MAIN.appViewModel.user.password)
        if (!MAIN.appViewModel.user.photo.isNullOrEmpty()) {
            Picasso.get()
                .load(MAIN.appViewModel.user.photo)
                .placeholder(R.drawable.avatar)
                .into(binding.ivPhoto)
        }
    }

    private fun saveChangesInProfile(){

        if (binding.etPassword.text.length < 6){
            Toast.makeText(MAIN, "Пароль должен быть более 6 символов", Toast.LENGTH_SHORT)
                .show()
            return
        }

        MAIN.appViewModel.user.email = binding.etEmail.text.toString()
        MAIN.appViewModel.user.id = AUTH.currentUser!!.uid
        MAIN.appViewModel.user.firstName = binding.etFirstName.text.toString()
        MAIN.appViewModel.user.lastName = binding.etLastName.text.toString()
        MAIN.appViewModel.user.status = binding.swStatus.text.toString()
        MAIN.appViewModel.user.district = binding.etDistrict.text.toString()
        MAIN.appViewModel.user.birthday = binding.etBirthday.text.toString()
        MAIN.appViewModel.user.gender = binding.etGender.text.toString()

        MAIN.appViewModel.user.phone = binding.etPhone.text.toString()
        MAIN.appViewModel.user.password = binding.etPassword.text.toString()

        MAIN.appViewModel.user.schoolName = binding.etSchoolName.text.toString()
        MAIN.appViewModel.user.description = binding.etSchoolDescription.text.toString()
        MAIN.appViewModel.user.address = binding.etSchoolAddress.text.toString()

        changeEmailAndPassword()

        val userValues = MAIN.appViewModel.user.toMap()
        val childUpdates = hashMapOf<String, Any>(
            "/User/${MAIN.appViewModel.user.id}" to userValues
        )

        Toast.makeText(MAIN, "Данные обновлены", Toast.LENGTH_SHORT).show()
        REF_DATABASE_ROOT.updateChildren(childUpdates)
    }

    private fun changeEmailAndPassword(){
        val credential =
            EmailAuthProvider.getCredential(MAIN.appViewModel.user.email!!,
                MAIN.appViewModel.user.password!!
            )
        AUTH.currentUser?.updateEmail(binding.etEmail.text.toString())
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    AUTH.currentUser?.updatePassword(binding.etPassword.text.toString())
                        ?.addOnCompleteListener { task3 ->
                            if (task3.isSuccessful) {
                                AUTH.currentUser?.reauthenticate(credential)
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
                changeImage()
            }
            R.id.deletePhoto -> {
                val builderDeleteDialog: AlertDialog.Builder = AlertDialog.Builder(MAIN)
                builderDeleteDialog
                    .setTitle("Вы уверены, что хотите удалить изображение?")
                    .setCancelable(false)
                    .setPositiveButton("Да") { _, _ ->
                        val path = REF_STORAGE_ROOT.child(FOLDER_PROFILE_IMAGE)
                            .child(MAIN.appViewModel.user.id)
                        path.delete().addOnSuccessListener {
                            MAIN.appViewModel.user.photo = ""
                            REF_DATABASE_ROOT.child("User").child(MAIN.appViewModel.user.id)
                                .child(CHILD_PHOTO_URL).setValue(MAIN.appViewModel.user.photo)
                            Toast.makeText(MAIN, "Изображение удалено", Toast.LENGTH_SHORT).show()
                            binding.ivPhoto.setImageResource(R.drawable.avatar)
                        }.addOnFailureListener {
                            Toast.makeText(MAIN, it.message, Toast.LENGTH_SHORT).show()
                        }
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

    private fun changeImage(){
        CropImage.activity()
            .setAspectRatio(1, 1)
            .setRequestedSize(600, 600)
            .setCropShape(CropImageView.CropShape.OVAL)
            .start(MAIN, this)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE
            && resultCode == Activity.RESULT_OK && data != null){
            val uri = CropImage.getActivityResult(data).uri
            val path = REF_STORAGE_ROOT.child(FOLDER_PROFILE_IMAGE)
                .child(MAIN.appViewModel.user.id)
            path.putFile(uri).addOnCompleteListener { task1 ->
                if (task1.isSuccessful) {
                    path.downloadUrl.addOnCompleteListener { task2 ->
                        if (task2.isSuccessful) {
                            val photoUrl = task2.result.toString()
                            REF_DATABASE_ROOT.child("User").child(MAIN.appViewModel.user.id)
                                .child(CHILD_PHOTO_URL).setValue(photoUrl)
                                .addOnCompleteListener {
                                    if (it.isSuccessful) {
                                        Picasso.get()
                                            .load(photoUrl)
                                            .placeholder(R.drawable.avatar)
                                            .into(binding.ivPhoto)
                                        MAIN.appViewModel.user.photo = photoUrl
                                    }
                                }
                        }
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}