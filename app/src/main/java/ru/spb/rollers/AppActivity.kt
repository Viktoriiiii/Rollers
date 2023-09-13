package ru.spb.rollers

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.yandex.mapkit.MapKitFactory
import ru.spb.rollers.databinding.ActivityAppBinding

class AppActivity : AppCompatActivity() {

    lateinit var binding: ActivityAppBinding
    lateinit var navController: NavController

    lateinit var appViewModel: AppViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_Rollers)
        super.onCreate(savedInstanceState)

        initFirebase()
        binding = ActivityAppBinding.inflate(layoutInflater)

        setContentView(binding.root)
        navController = Navigation.findNavController(this,
            R.id.fragment_container)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            handleBottomNavVisibility(destination)
        }

        appViewModel = ViewModelProvider(this)[AppViewModel::class.java]

        MAIN = this

        if (!isMapKitInitialized){
            MapKitFactory.setApiKey(BuildConfig.MAPKIT_API_KEY)
            MapKitFactory.initialize(MAIN)
            isMapKitInitialized = true
        }

        binding.navView.setupWithNavController(navController)
        binding.navView.setOnItemSelectedListener { item ->
            NavigationUI.onNavDestinationSelected(item, navController)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        // Обрабатываем возврат на предыдущий фрагмент при нажатии на кнопку "Назад" в панели инструментов
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    private fun handleBottomNavVisibility(destination: NavDestination) {
        // Определите, на каких фрагментах не нужно показывать BottomNavigationView
        val hideBottomNav = destination.id == R.id.authorizationFragment ||
                destination.id == R.id.messagesFragment ||
                destination.id == R.id.registrationFragment

        binding.navView.visibility =
            if (hideBottomNav)
                View.GONE
            else
                View.VISIBLE
    }
}