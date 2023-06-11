package ru.spb.rollers.ui.routes

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
import ru.spb.rollers.oldadapters.RouteAdapter
import ru.spb.rollers.databinding.RoutesFragmentBinding
import ru.spb.rollers.models.Point
import ru.spb.rollers.oldmodel.Route
import ru.spb.rollers.titleRoutes

class RoutesFragment : Fragment() {

    private var _binding: RoutesFragmentBinding? = null
    private val binding get() = _binding!!

    companion object {
        fun newInstance(latitude: String, longitude: String): RoutesFragment {
            val fragment = RoutesFragment()
            val args = Bundle()
            args.putString("latitude", latitude)
            args.putString("longitude", longitude)
            fragment.arguments = args
            return fragment
        }
    }

    private lateinit var viewModel: RoutesViewModel

    private var routeList: List<Route> = mutableListOf()
    private lateinit var recyclerView: RecyclerView
    private lateinit var routeAdapter: RouteAdapter

    private var pointList: List<Point> = mutableListOf()
    private lateinit var pointAdapter: PointAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this)[RoutesViewModel::class.java]
        _binding = RoutesFragmentBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        pointList += Point(
            "1", "", "", "")
        pointList += Point(
            "2", "", "", "")

        binding.pointsList.adapter = PointAdapter(pointList)

        binding.ivMyLocation.setOnClickListener{
            // Записать все точки списка




            MAIN.navController.navigate(R.id.action_routes_to_mapsFragment)
            titleRoutes = "Поиск местоположения"
        }

        binding.btnAddPoint.setOnClickListener{ addPoint() }

        recyclerView = view.findViewById(R.id.routesList)
        routeAdapter = RouteAdapter(routeList)
        recyclerView.adapter = routeAdapter

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

    private fun addPoint(){
        pointAdapter.addPoint(Point("1", "", "", ""))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}