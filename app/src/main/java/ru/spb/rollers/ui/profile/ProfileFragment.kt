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
    private val WEATHER_API_KEY = "43cf8001-588a-477e-b84f-0a140208c3de"

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

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this)[ProfileViewModel::class.java]
        // TODO: Use the ViewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
            Toast.makeText(MAIN, "Изменения сохранены", Toast.LENGTH_SHORT).show()
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