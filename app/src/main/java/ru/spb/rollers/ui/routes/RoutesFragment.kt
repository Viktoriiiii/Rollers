package ru.spb.rollers.ui.routes

import android.annotation.SuppressLint
import android.os.Build
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
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.SearchView
import com.google.android.material.card.MaterialCardView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.yandex.mapkit.map.*
import com.yandex.mapkit.search.*
import ru.spb.rollers.*
import ru.spb.rollers.adapters.RouteAdapter
import ru.spb.rollers.databinding.RoutesFragmentBinding
import ru.spb.rollers.models.Route
import java.util.*

class RoutesFragment : Fragment() {
    private var _binding: RoutesFragmentBinding? = null
    private val binding get() = _binding!!
    var currentIndex = 0 // Текущий индекс в списке значений

    private lateinit var viewModel: RoutesViewModel

    private var routeList: MutableList<Route> = mutableListOf()
    private lateinit var routeAdapter: RouteAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this)[RoutesViewModel::class.java]
        _binding = RoutesFragmentBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.ivMyLocation.setOnClickListener{
            MAIN.navController.navigate(R.id.action_routes_to_mapsFragment)
            MAIN.appViewModel.buildRoute = false
            MAIN.appViewModel.addingPoint = false
            titleRoutes = "Поиск местоположения"
        }

        binding.btnAddPoint.setOnClickListener{
            addPoint()
        }

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
                MAIN.appViewModel.buildRoute = true
                titleRoutes = "Просмотр маршрута"
            }
            else {
                Toast.makeText(MAIN, "Добавьте точки для построения маршрута",
                    Toast.LENGTH_SHORT).show()
            }
        }

        binding.ivDelete1.setOnClickListener {
            MAIN.appViewModel.listPoint.removeIf { it.displayName == binding.etLocation1.text.toString() }
            binding.etLocation1.text.clear()
        }

        binding.ivDelete2.setOnClickListener {
            MAIN.appViewModel.listPoint.removeIf { it.displayName == binding.etLocation2.text.toString() }
            binding.etLocation2.text.clear()
        }

        binding.etLocation1.setOnClickListener {
            MAIN.navController.navigate(R.id.action_routes_to_mapsFragment)
            MAIN.appViewModel.buildRoute = false
            MAIN.appViewModel.addingPoint = true
            titleRoutes = ""
        }
        binding.etLocation2.setOnClickListener {
            MAIN.navController.navigate(R.id.action_routes_to_mapsFragment)
            MAIN.appViewModel.buildRoute = false
            MAIN.appViewModel.addingPoint = true
            titleRoutes = ""
        }

        if (MAIN.appViewModel.clearList){
            !MAIN.appViewModel.clearList
            MAIN.appViewModel.listPoint.clear()
            binding.etLocation1.text.clear()
            binding.etLocation2.text.clear()
        }
        setPoints()

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null && routeList.isNotEmpty()) {
                    searchList(newText)
                }
                return true
            }
        })
    }

    private fun initRoutesRecyclerView() {
        routeAdapter = RouteAdapter(routeList)

        val ref = REF_DATABASE_ROUTE
            .child(MAIN.appViewModel.user.id)

        binding.routesList.adapter = routeAdapter

        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val listRoute = snapshot.children.map { it.getRouteModel() }
                routeAdapter.setList(listRoute as MutableList<Route>)
                routeList = listRoute.toMutableList()
            }
            override fun onCancelled(error: DatabaseError) {}
        })
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

    @RequiresApi(Build.VERSION_CODES.N)
    private fun setPoints(){
        if (MAIN.appViewModel.listPoint.isNotEmpty()){
            val container: LinearLayout = binding.llContainer
            while (MAIN.appViewModel.listPoint.size > container.childCount){
                addPoint()
            }
            traverseViewHierarchy(container)
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    @SuppressLint("InflateParams")
    private fun addPoint(){
        val inflater = LayoutInflater.from(MAIN)
        val cardView = inflater.inflate(R.layout.item_point, null) as MaterialCardView
        binding.llContainer.addView(cardView)
        val etLocation: EditText = cardView.findViewById(R.id.editTextLocation)
        etLocation.setOnClickListener {
            MAIN.navController.navigate(R.id.action_routes_to_mapsFragment)
            MAIN.appViewModel.buildRoute = false
            MAIN.appViewModel.addingPoint = true
            titleRoutes = ""
        }
        val delete: ImageView = cardView.findViewById(R.id.imageViewDelete)
        delete.setOnClickListener{
            MAIN.appViewModel.listPoint.removeIf { it.displayName == etLocation.text.toString() }

            binding.llContainer.removeView(cardView)
        }
    }

    fun searchList(text: String) {
        val searchList: MutableList<Route> = mutableListOf()
        for (route in routeList) {
            if (route.name!!.lowercase(Locale.getDefault())
                    .contains(text.lowercase(Locale.getDefault()))
            ) {
                searchList.add(route)
            }
        }
        routeAdapter.setList(searchList)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        initRoutesRecyclerView()
        super.onResume()
    }
}