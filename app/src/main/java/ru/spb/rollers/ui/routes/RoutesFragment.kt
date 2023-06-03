package ru.spb.rollers.ui.routes

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModelProvider
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
import ru.spb.rollers.databinding.RoutesFragmentBinding
import ru.spb.rollers.model.Route
import ru.spb.rollers.model.Waypoint
import ru.spb.rollers.titleRoutes

class RoutesFragment : Fragment() {

    private var _binding: RoutesFragmentBinding? = null
    private val binding get() = _binding!!
    companion object {
        fun newInstance() = RoutesFragment()
    }

    private lateinit var viewModel: RoutesViewModel

    private var routeList: List<Route> = mutableListOf()
    private lateinit var recyclerView: RecyclerView
    private lateinit var routeAdapter: RouteAdapter

    private var pointList: List<Waypoint> = mutableListOf()
    private lateinit var recyclerViewForPoint: RecyclerView
    private lateinit var pointAdapter: PointAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = RoutesFragmentBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this)[RoutesViewModel::class.java]
        // TODO: Use the ViewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.imageViewMyLocation.setOnClickListener{
            MAIN.navController.navigate(R.id.action_routes_to_mapsFragment)
            titleRoutes = "Поиск местоположения"
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

        binding.searchView.setOnSearchClickListener{
            binding.txvTitle.visibility = View.GONE
        }

        binding.searchView.setOnCloseListener {
            binding.txvTitle.visibility = View.VISIBLE
            binding.searchView.onActionViewCollapsed()
            true
        }

        binding.btnBuildRoute.setOnClickListener{
            MAIN.navController.navigate(R.id.action_routes_to_mapsFragment)
            titleRoutes = "Просмотр маршрута"
        }
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}