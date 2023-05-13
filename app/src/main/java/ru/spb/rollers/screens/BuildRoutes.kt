package ru.spb.rollers.screens

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.yandex.mapkit.MapKit
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.mapview.MapView
import ru.spb.rollers.MAIN
import ru.spb.rollers.R
import ru.spb.rollers.databinding.FragmentBuildRoutesBinding

class BuildRoutes : Fragment() {

    private lateinit var binding: FragmentBuildRoutesBinding
    private lateinit var mapView: MapView
    private val PERMISSIONS_REQUEST_FINE_LOCATION = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBuildRoutesBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.imageViewBack.setOnClickListener{
            MAIN.navController.navigate(R.id.action_buildRoutes_to_routes2)
        }
        binding.imageViewSaveRoute.setOnClickListener{
            Toast.makeText(MAIN, "Маршрут сохранен", Toast.LENGTH_SHORT).show()
            MAIN.navController.navigate(R.id.action_buildRoutes_to_mapFragment)
        }

        mapView = view.findViewById(R.id.mapView)
        val mapKit: MapKit = MapKitFactory.getInstance()
    }
    private fun requestLocationPermission() {
        if (ContextCompat.checkSelfPermission(
                MAIN, "android.permission.ACCESS_FINE_LOCATION")
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                MAIN, arrayOf("android.permission.ACCESS_FINE_LOCATION"),
                PERMISSIONS_REQUEST_FINE_LOCATION
            )
        }
    }

    override fun onStop() {
        mapView.onStop()
        MapKitFactory.getInstance().onStop()
        super.onStop()
    }

    override fun onStart() {
        super.onStart()
        MapKitFactory.getInstance().onStart()
        mapView.onStart()
    }

    override fun onDestroy() {
        mapView.onStop()
        MapKitFactory.getInstance().onStop()
        super.onDestroy()
    }
}