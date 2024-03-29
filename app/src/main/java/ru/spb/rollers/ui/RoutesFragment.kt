package ru.spb.rollers.ui

import android.annotation.SuppressLint
import android.os.Build
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
import androidx.cardview.widget.CardView
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
    private var currentIndex = 0 // Текущий индекс в списке значений

    private var routeList: MutableList<Route> = mutableListOf()
    private lateinit var routeAdapter: RouteAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = RoutesFragmentBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        MAIN.appViewModel.setPhoto(binding.ivPhoto)

        binding.ivMyLocation.setOnClickListener{
            MAIN.navController.navigate(R.id.action_routes_to_mapsFragment)
            titleRoutes = "Поиск местоположения"
            MAIN.appViewModel.maps = 1
        }

        binding.btnAddPoint.setOnClickListener{
            addPoint()
        }

        binding.searchView.setOnSearchClickListener{
            binding.tvTitle.visibility = View.GONE
        }
        binding.searchView.setOnCloseListener {
            binding.tvTitle.visibility = View.VISIBLE
            binding.searchView.onActionViewCollapsed()
            true
        }
        binding.btnBuildRoute.setOnClickListener{
            MAIN.appViewModel.maps = 3

            if (MAIN.appViewModel.listPoint.size > 1){
                MAIN.navController.navigate(R.id.action_routes_to_mapsFragment)
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
            MAIN.appViewModel.maps = 2
            titleRoutes = ""
        }
        binding.etLocation2.setOnClickListener {
            MAIN.navController.navigate(R.id.action_routes_to_mapsFragment)
            MAIN.appViewModel.maps = 2
            titleRoutes = ""
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
        binding.routesList.adapter = routeAdapter

        REF_DATABASE_ROUTE.child(MAIN.appViewModel.user.id)
            .addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val listRoute = snapshot.children.map { it.getRouteModel() }
                    routeAdapter.setList(listRoute.sortedByDescending { it.pinned } as MutableList<Route>)
                    routeList = listRoute.toMutableList()
                }
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
        val cardView = inflater.inflate(R.layout.item_point, null) as CardView
        binding.llContainer.addView(cardView)
        val etLocation: EditText = cardView.findViewById(R.id.et_location)
        etLocation.setOnClickListener {
            MAIN.navController.navigate(R.id.action_routes_to_mapsFragment)
            MAIN.appViewModel.maps = 2
            titleRoutes = ""
        }
        val delete: ImageView = cardView.findViewById(R.id.iv_delete)
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
        routeAdapter.setList(searchList.sortedByDescending { it.pinned } as MutableList<Route>)
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