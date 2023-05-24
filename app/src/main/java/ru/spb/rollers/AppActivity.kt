package ru.spb.rollers

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
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
    private val MAPKIT_API_KEY = "630ce119-481d-42ab-83de-c4d18d7533d5"
    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAppBinding.inflate(layoutInflater)
        setContentView(binding.root)
        navController = Navigation.findNavController(this,
            R.id.fragmentContainer)

        MAIN = this

        if (!isMapKitInitialized){
            MapKitFactory.setApiKey(MAPKIT_API_KEY)
            MapKitFactory.initialize(MAIN)
            isMapKitInitialized = true
        }

        bottomNavigationView = findViewById(R.id.nav_view)
        bottomNavigationView.setupWithNavController(navController)
        bottomNavigationView.setOnItemSelectedListener { item ->
            NavigationUI.onNavDestinationSelected(item, navController)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        // Обрабатываем возврат на предыдущий фрагмент при нажатии на кнопку "Назад" в панели инструментов
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
    fun setBottomNavigationVisible(visible: Boolean) {
        if (visible) {
            bottomNavigationView.visibility = View.VISIBLE
        } else {
            bottomNavigationView.visibility = View.GONE
        }
    }
}