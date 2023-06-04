package ru.spb.rollers

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.yandex.mapkit.MapKitFactory
import ru.spb.rollers.databinding.ActivityAppBinding

class AppActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAppBinding
    lateinit var navController: NavController
    private val MAPKIT_API_KEY = "630ce119-481d-42ab-83de-c4d18d7533d5"
    private lateinit var bottomNavigationView: BottomNavigationView

    lateinit var appViewModel: AppViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAppBinding.inflate(layoutInflater)
        setContentView(binding.root)
        navController = Navigation.findNavController(this,
            R.id.fragmentContainer)

        appViewModel = ViewModelProvider(this)[AppViewModel::class.java]

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
        bottomNavigationView.getOrCreateBadge(R.id.dialogs).number = 2

        setBottomNavigationVisible(false)

        initFirebase()

        println(object : Any() {}.javaClass.enclosingMethod?.name)
        println(appViewModel.liveData.value)

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

    private fun initFirebase() {
        appViewModel.AUTH = FirebaseAuth.getInstance()
        appViewModel.REF_DATABASE_ROOT = FirebaseDatabase.getInstance().reference
        appViewModel.REF_DATABASE_USER = FirebaseDatabase.getInstance().getReference("User")
    }


    override fun onStart() {
        super.onStart()
        println(object : Any() {}.javaClass.enclosingMethod?.name)
        println(appViewModel.liveData.value)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        println(object : Any() {}.javaClass.enclosingMethod?.name)
        println(appViewModel.liveData.value)
        setBottomNavigationVisible(true)
    }

    override fun onResume() {
        super.onResume()
        println(object : Any() {}.javaClass.enclosingMethod?.name)
        println(appViewModel.liveData.value)

    }

    override fun onPause() {
        super.onPause()
        println(object : Any() {}.javaClass.enclosingMethod?.name)
        println(appViewModel.liveData.value)

    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        println(object : Any() {}.javaClass.enclosingMethod?.name)
        println(appViewModel.liveData.value)

    }

    override fun onStop() {
        super.onStop()
        println(object : Any() {}.javaClass.enclosingMethod?.name)
        println(appViewModel.liveData.value)

    }

    override fun onDestroy() {
        super.onDestroy()
        println(object : Any() {}.javaClass.enclosingMethod?.name)
        println(appViewModel.liveData.value)
    }
}