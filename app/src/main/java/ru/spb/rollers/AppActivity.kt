package ru.spb.rollers

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.yandex.mapkit.MapKitFactory
import ru.spb.rollers.databinding.ActivityAppBinding

class AppActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAppBinding
    lateinit var navController: NavController
    lateinit var bottomNavigationView: BottomNavigationView

    lateinit var appViewModel: AppViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initFirebase()
        binding = ActivityAppBinding.inflate(layoutInflater)

        setContentView(binding.root)

        navController = Navigation.findNavController(this,
            R.id.fragmentContainer)

        appViewModel = ViewModelProvider(this)[AppViewModel::class.java]

        MAIN = this

        if (!isMapKitInitialized){
            MapKitFactory.setApiKey(BuildConfig.MAPKIT_API_KEY)
            MapKitFactory.initialize(MAIN)
            isMapKitInitialized = true
        }

        bottomNavigationView = findViewById(R.id.nav_view)
        bottomNavigationView.setupWithNavController(navController)
        bottomNavigationView.setOnItemSelectedListener { item ->
            NavigationUI.onNavDestinationSelected(item, navController)
        }

        if (appViewModel.liveData.value == true)
            setBottomNavigationVisible(true)
        else {
            setBottomNavigationVisible(false)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        // Обрабатываем возврат на предыдущий фрагмент при нажатии на кнопку "Назад" в панели инструментов
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    fun setBottomNavigationVisible(visible: Boolean) {
        appViewModel.liveData.value = visible
        if (appViewModel.liveData.value == true) {
            bottomNavigationView.visibility = View.VISIBLE
        } else {
            bottomNavigationView.visibility = View.GONE
        }
    }
}