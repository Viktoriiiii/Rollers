package ru.spb.rollers

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.yandex.mapkit.MapKitFactory
import ru.spb.rollers.databinding.ActivityAppBinding

class AppActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAppBinding
    lateinit var navController: NavController
    private val MAPKIT_API_KEY = "630ce119-481d-42ab-83de-c4d18d7533d5"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAppBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navController = Navigation.findNavController(this,
            R.id.fragmentContainer)

        MAIN = this

        MapKitFactory.setApiKey(MAPKIT_API_KEY)
        MapKitFactory.initialize(MAIN)
    }
}