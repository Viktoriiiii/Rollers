package ru.spb.rollers.screens

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import ru.spb.rollers.MAIN
import ru.spb.rollers.R
import ru.spb.rollers.adapters.PointAdapter
import ru.spb.rollers.adapters.RouteAdapter
import ru.spb.rollers.databinding.FragmentRoutesBinding
import ru.spb.rollers.model.Route
import ru.spb.rollers.model.Waypoint

class Routes : Fragment() {

    private lateinit var binding: FragmentRoutesBinding
    private var routeList: List<Route> = mutableListOf()
    private lateinit var recyclerView: RecyclerView
    private lateinit var routeAdapter: RouteAdapter

    private var pointList: List<Waypoint> = mutableListOf()
    private lateinit var recyclerViewForPoint: RecyclerView
    private lateinit var pointAdapter: PointAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRoutesBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.imageViewBack.setOnClickListener{
            MAIN.navController.navigate(R.id.action_routes2_to_mapFragment)
        }

        binding.btnBuildRoute.setOnClickListener{
            MAIN.navController.navigate(R.id.action_routes2_to_buildRoutes)
        }

        val btnAddPoint: MaterialButton = view.findViewById(R.id.btnAddPoint)
        btnAddPoint.setOnClickListener{ addPoint() }

        setInitialData()
        recyclerView = view.findViewById(R.id.routesList)
        routeAdapter = RouteAdapter(routeList)
        recyclerView.adapter = routeAdapter

        recyclerViewForPoint = view.findViewById(R.id.pointsList)
        pointAdapter = PointAdapter(pointList)
        recyclerViewForPoint.adapter = pointAdapter
    }


    @SuppressLint("NotifyDataSetChanged")
    private fun addPoint(){
        pointAdapter.addPoint(Waypoint(
            3, "Электросила"))
    }

    private fun setInitialData() {
        routeList += Route(
            1, "Маршрут № 1", "Васька",
            "Петроградка",  "15 км")
        routeList += Route(
            2, "Маршрут № 2", "Васька",
            "Петроградка",  "15 км")
        routeList += Route(
            3, "Маршрут № 3", "Васька",
            "Петроградка",  "15 км")
        routeList += Route(
            4, "Маршрут № 4", "Васька",
            "Петроградка",  "15 км")
        routeList += Route(
            5, "Маршрут № 5", "Васька",
            "Петроградка",  "15 км")
        routeList += Route(
            6, "Маршрут № 6", "Васька",
            "Петроградка",  "15 км")
        routeList += Route(
            7, "Маршрут № 7", "Васька",
            "Петроградка",  "15 км")
        routeList += Route(
            8, "Маршрут № 8", "Васька",
            "Петроградка",  "15 км")

        pointList += Waypoint(
            1, null)
        pointList += Waypoint(
            2, null)
    }
}

