package ru.spb.rollers.ui.routes

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import com.google.android.material.card.MaterialCardView
import com.yandex.mapkit.map.*
import com.yandex.mapkit.search.*
import ru.spb.rollers.MAIN
import ru.spb.rollers.R
import ru.spb.rollers.oldadapters.RouteAdapter
import ru.spb.rollers.databinding.RoutesFragmentBinding
import ru.spb.rollers.oldmodel.Route
import ru.spb.rollers.titleRoutes

class RoutesFragment : Fragment() {
    private var _binding: RoutesFragmentBinding? = null
    private val binding get() = _binding!!
    var currentIndex = 0 // Текущий индекс в списке значений

    private lateinit var viewModel: RoutesViewModel

    private var routeList: List<Route> = mutableListOf()
    private lateinit var routeAdapter: RouteAdapter

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

        binding.ivMyLocation.setOnClickListener{
            // Записать все точки списка


            MAIN.navController.navigate(R.id.action_routes_to_mapsFragment)
            titleRoutes = "Поиск местоположения"
        }

        binding.btnAddPoint.setOnClickListener{
            MAIN.appViewModel.addingPoint = true
            addPoint()
        }
        routeAdapter = RouteAdapter(routeList)
        binding.routesList.adapter = routeAdapter

        binding.searchView.setOnSearchClickListener{
            binding.txvTitle.visibility = View.GONE
        }
        binding.searchView.setOnCloseListener {
            binding.txvTitle.visibility = View.VISIBLE
            binding.searchView.onActionViewCollapsed()
            true
        }
        binding.btnBuildRoute.setOnClickListener{
            if (MAIN.appViewModel.listPoint.size > 1){
                MAIN.navController.navigate(R.id.action_routes_to_mapsFragment)
                MAIN.appViewModel.addingPoint = false
                titleRoutes = "Просмотр маршрута"
            }
            else {
                Toast.makeText(MAIN, "Добавьте точки для построения маршрута",
                    Toast.LENGTH_SHORT).show()
            }
        }

        binding.ivDelete1.setOnClickListener {
            binding.etLocation1.text.clear()
        }

        binding.ivDelete2.setOnClickListener { binding.etLocation2.text.clear() }

        binding.etLocation1.setOnClickListener {
            MAIN.navController.navigate(R.id.action_routes_to_mapsFragment)
            MAIN.appViewModel.addingPoint = true
        }
        binding.etLocation2.setOnClickListener {
            MAIN.navController.navigate(R.id.action_routes_to_mapsFragment)
            MAIN.appViewModel.addingPoint = true
        }
        setPoints()
    }

    private fun traverseViewHierarchy(view: View) {
        if (view is ViewGroup) {
            val childCount = view.childCount
            for (i in 0 until childCount) {
                val childView = view.getChildAt(i)
                traverseViewHierarchy(childView)
            }
        } else if (view is EditText) {
            if (currentIndex < MAIN.appViewModel.listPoint.size) {
                val value = MAIN.appViewModel.listPoint[currentIndex].displayName
                view.setText(value)
                currentIndex++
            }
        }
    }

    private fun setPoints(){
        if (MAIN.appViewModel.listPoint.isNotEmpty()){
            val container: LinearLayout = binding.llContainer
            while (MAIN.appViewModel.listPoint.size > container.childCount){
                addPoint()
            }
            traverseViewHierarchy(container)
        }
    }

    @SuppressLint("InflateParams")
    private fun addPoint(){
        val inflater = LayoutInflater.from(MAIN)
        val cardView = inflater.inflate(R.layout.item_point, null) as MaterialCardView
        binding.llContainer.addView(cardView)
        val delete: ImageView = cardView.findViewById(R.id.imageViewDelete)
        delete.setOnClickListener{
            binding.llContainer.removeView(cardView)
        }
        val etLocation: EditText = cardView.findViewById(R.id.editTextLocation)
        etLocation.setOnClickListener {
            MAIN.navController.navigate(R.id.action_routes_to_mapsFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}