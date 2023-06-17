package ru.spb.rollers.ui.maps

import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.PointF
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.yandex.mapkit.*
import com.yandex.mapkit.directions.DirectionsFactory
import com.yandex.mapkit.geometry.BoundingBox
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.layers.ObjectEvent
import com.yandex.mapkit.map.*
import com.yandex.mapkit.map.Map
import com.yandex.mapkit.search.*
import com.yandex.mapkit.transport.TransportFactory
import com.yandex.mapkit.transport.masstransit.PedestrianRouter
import com.yandex.mapkit.transport.masstransit.Route
import com.yandex.mapkit.transport.masstransit.TimeOptions
import com.yandex.mapkit.user_location.UserLocationLayer
import com.yandex.mapkit.user_location.UserLocationObjectListener
import com.yandex.mapkit.user_location.UserLocationView
import com.yandex.runtime.Error
import com.yandex.runtime.image.ImageProvider
import com.yandex.runtime.network.NetworkError
import com.yandex.runtime.network.RemoteError
import ru.spb.rollers.*
import ru.spb.rollers.databinding.MapsFragmentBinding
import ru.spb.rollers.adapters.OnItemClickListener
import ru.spb.rollers.adapters.SearchAdapter
import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

class MapsFragment : Fragment(), UserLocationObjectListener, Session.SearchListener,
    CameraListener,
    com.yandex.mapkit.transport.masstransit.Session.RouteListener {

    private var _binding: MapsFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: MapsViewModel
    private val PERMISSIONS_REQUEST_FINE_LOCATION = 1
    private var mapKit: MapKit? = null
    private var userLocationLayer: UserLocationLayer? = null
    private lateinit var searchManager: SearchManager
    private lateinit var searchSession: Session

    private var point = ru.spb.rollers.models.Point()

    private lateinit var recyclerView: RecyclerView
    private lateinit var searchAdapter: SearchAdapter

    private lateinit var mapObjects: MapObjectCollection
    private lateinit var drivingRouter: PedestrianRouter
    private lateinit var drivingSession: com.yandex.mapkit.transport.masstransit.Session

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        DirectionsFactory.initialize(MAIN)
        TransportFactory.initialize(MAIN)

        viewModel = ViewModelProvider(this)[MapsViewModel::class.java]
        _binding = MapsFragmentBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.imageButtonBack.setOnClickListener{
            if (MAIN.appViewModel.buildRoute && MAIN.appViewModel.listPoint.size > 1){
                if (MAIN.appViewModel.viewRoute){
                    MAIN.onSupportNavigateUp()
                    MAIN.appViewModel.viewRoute = false
                    MAIN.appViewModel.buildRoute = false
                }
                else {
                    // сохранить маршрут?
                    val builderSaveRouteDialog: AlertDialog.Builder = AlertDialog.Builder(MAIN)
                    builderSaveRouteDialog
                        .setTitle("Сохранение маршрута")
                        .setMessage("Сохранить маршрут?")
                        .setCancelable(false)
                        .setPositiveButton("Да") { _, _ ->

                            // Здесть еще один alert с edittext, для ввода имени и в нем при ок сохранение
                            val builder: AlertDialog.Builder = AlertDialog.Builder(MAIN)
                            val profileView: View? =
                                MAIN.layoutInflater.inflate(R.layout.alert_dialog_change_name, null)
                            val etNameRoute: EditText = profileView!!.findViewById(R.id.input_text)
                            builder.setView(profileView)

                            builder.setTitle("Название маршрута")

                            builder.setPositiveButton("OK") { _, _ ->
                                var name = etNameRoute.text.toString()
                                if (name.isEmpty()){
                                    name = "Маршрут №"
                                }
                                val distance = getDistance()
                                val curUser = MAIN.appViewModel.user.id
                                val routeKey = REF_DATABASE_ROUTE.child(curUser).push().key
                                val refRoute = "Route/$curUser/$routeKey"

                                val route = ru.spb.rollers.models.Route(routeKey, name, distance.toString())
                                REF_DATABASE_ROOT.child(refRoute).setValue(route)

                                for (p in MAIN.appViewModel.listPoint){
                                    val pointKey = REF_DATABASE_ROOT.child(refRoute).child("Points").push().key
                                    p.id = pointKey.toString()
                                    REF_DATABASE_ROOT.child(refRoute).child("Points")
                                        .child(pointKey.toString())
                                        .setValue(p)
                                }
                                Toast.makeText(MAIN, "Маршрут сохранен", Toast.LENGTH_SHORT).show()
                                MAIN.appViewModel.clearList = true
                                MAIN.onSupportNavigateUp()
                            }

                            builder.setNegativeButton("Отмена") { dialog, _ ->
                                dialog.cancel()
                            }
                            val dialog = builder.create()
                            dialog.show()

                        }
                        .setNegativeButton("Отмена"){dialog, _ ->
                            dialog.cancel()
                            MAIN.onSupportNavigateUp()
                        }
                    val alertDialogSaveRoute: AlertDialog = builderSaveRouteDialog.create()
                    alertDialogSaveRoute.show()
                }
            }
            else
                MAIN.onSupportNavigateUp()
        }
        binding.txvTitle.text = titleRoutes

        if (MAIN.appViewModel.addingPoint){
            binding.cardViewDecrease.visibility = View.GONE
            binding.floatingActionButton.visibility = View.VISIBLE
            binding.searchView.isIconified = false
            binding.searchView.isFocusable = true
            binding.searchView.requestFocus()
            titleRoutes = ""
        }

        recyclerView = view.findViewById(R.id.suggestList)

        mapKit = MapKitFactory.getInstance()
        binding.mapView.map.move(
            CameraPosition(Point(59.939427, 30.309217), 11.0f, 0.0f, 0.0f),
            Animation(Animation.Type.SMOOTH, 0F),
            null
        )

        binding.searchMyLocation.setOnClickListener{
            getMyLocation()
        }
        searchManager = SearchFactory.getInstance().createSearchManager(SearchManagerType.COMBINED)

        val suggestOptions = SuggestOptions().setSuggestTypes(SuggestType.GEO.value)
        val suggestSession = searchManager.createSuggestSession()

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                submitQuery(binding.searchView.query.toString())
                binding.suggestList.visibility = View.GONE
                return true
            }

            override fun onQueryTextChange(query: String): Boolean {
                binding.suggestList.visibility = View.VISIBLE
                val southWest = Point(59.681658, 29.369953) // Юго-западная граница города Санкт-Петербург
                val northEast = Point(60.130912, 30.645520)
                val boundingBox = BoundingBox(southWest, northEast)
                suggestSession.suggest(query, boundingBox, suggestOptions, object : SuggestSession.SuggestListener {
                    override fun onResponse(suggestItems: List<SuggestItem>) {
                        searchAdapter = SearchAdapter(suggestItems)
                        recyclerView.adapter = searchAdapter
                        searchAdapter.setOnItemClickListener(object : OnItemClickListener {
                            override fun onItemClick(item: SuggestItem) {
                                binding.searchView.setQuery(item.title.text, false)
                                binding.mapView.map.move(
                                    CameraPosition(item.center!!,14.0f, 0.0f, 0.0f),
                                    Animation(Animation.Type.SMOOTH, 1.0f),
                                    null
                                )
                                binding.suggestList.visibility = View.GONE
                            }
                        })
                    }
                    override fun onError(error: Error) {
                        Toast.makeText(MAIN, "Диалог закреплен", Toast.LENGTH_SHORT).show()
                    }
                })
                return true
            }
        })

        binding.searchView.setOnCloseListener {
            val mapObjects = binding.mapView.map.mapObjects
            mapObjects.clear()
            true
        }

        binding.searchView.setOnSearchClickListener{
            binding.txvTitle.visibility = View.GONE
        }

        binding.searchView.setOnCloseListener {
            binding.txvTitle.visibility = View.VISIBLE
            binding.searchView.onActionViewCollapsed()
            true
        }

        binding.floatingActionButton.setOnClickListener{
            val query = binding.searchView.query.toString()
            if (query.isNotEmpty()){
                point.displayName = binding.searchView.query.toString()
            }
            else
                Toast.makeText(MAIN, "Выберите местоположение", Toast.LENGTH_SHORT).show()
            if (!point.displayName.isNullOrEmpty() && !point.latitude.isNullOrEmpty() && !point.longitude.isNullOrEmpty()){
                MAIN.appViewModel.listPoint += point
                MAIN.navController.navigate(R.id.action_mapsFragment_to_routes)
            }
        }

        drivingRouter = TransportFactory.getInstance().createPedestrianRouter()
        mapObjects = binding.mapView.map.mapObjects.addCollection()
        if (MAIN.appViewModel.buildRoute && MAIN.appViewModel.listPoint.size > 1){
            binding.cardViewDecrease.visibility = View.GONE
            buildRoute()
        }
        else {
            binding.mapView.map.addCameraListener(this)
            binding.mapView.map.move(
                CameraPosition(Point(59.945933, 30.320045), 14.0f, 0.0f, 0.0f)
            )
        }
    }

    private fun getDistance(): Double {
        val size = MAIN.appViewModel.listPoint.size
        var distance = 0.0
        for (index in 0 until size - 1) {
            distance += calculateDistance(
                MAIN.appViewModel.listPoint[index],
                MAIN.appViewModel.listPoint[index + 1]
            )
        }
        return BigDecimal.valueOf(distance).setScale(1, RoundingMode.HALF_UP).toDouble()
    }

    private fun buildRoute(){
        submitRequest()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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

    private fun getMyLocation() {
        requestLocationPermission()
        if (userLocationLayer == null) {
            binding.mapView.map.isRotateGesturesEnabled = true
            mapKit!!.resetLocationManagerToDefault()
            userLocationLayer = mapKit!!.createUserLocationLayer(binding.mapView.mapWindow)
        }
        userLocationLayer!!.isVisible = !userLocationLayer!!.isVisible
        userLocationLayer!!.isHeadingEnabled = false
        userLocationLayer!!.setObjectListener(this)
    }

    override fun onStop() {
        binding.mapView.onStop()
        MapKitFactory.getInstance().onStop()
        super.onStop()
    }

    override fun onStart() {
        super.onStart()
        MapKitFactory.getInstance().onStart()
        binding.mapView.onStart()
    }

    override fun onObjectAdded(userLocationView: UserLocationView) {
        userLocationLayer!!.setAnchor(
            PointF((binding.mapView.width * 0.5).toFloat(), (binding.mapView.height * 0.5).toFloat()),
            PointF((binding.mapView.width * 0.5).toFloat(), (binding.mapView.height * 0.83).toFloat())
        )
        userLocationView.accuracyCircle.fillColor = Color.WHITE
    }

    override fun onObjectRemoved(view: UserLocationView) {}

    override fun onObjectUpdated(view: UserLocationView, event: ObjectEvent) {}

    override fun onSearchResponse(response: Response) {
        val mapObjects = binding.mapView.map.mapObjects
        mapObjects.clear()

        if (!binding.searchView.query.isNullOrEmpty()) {
            for (searchResult in response.collection.children) {
                val resultLocation = searchResult.obj!!.geometry[0].point
                if (resultLocation != null) {
                    point.latitude = resultLocation.latitude.toString()
                    point.longitude = resultLocation.longitude.toString()
                    mapObjects.addPlacemark(
                        resultLocation,
                        ImageProvider.fromResource(MAIN, R.drawable.search_result)
                    )
                }
            }
        }
    }

    override fun onSearchError(error: Error) {
        var errorMessage = "getString(R.string.unknown_error_message)"
        if (error is RemoteError) {
            errorMessage = "getString(R.string.remote_error_message)"
        } else if (error is NetworkError) {
            errorMessage = "getString(R.string.network_error_message)"
        }

        Toast.makeText(MAIN, errorMessage, Toast.LENGTH_SHORT).show()
    }

    override fun onCameraPositionChanged(
        map: Map,
        cameraPosition: CameraPosition,
        cameraUpdateReason: CameraUpdateReason,
        finished: Boolean
    ) {
        if (finished) {
            submitQuery(binding.searchView.query.toString())
        }
    }

    private fun submitQuery(query: String) {
        searchSession = searchManager.submit(
            query,
            VisibleRegionUtils.toPolygon(binding.mapView.map.visibleRegion),
            SearchOptions(),
            this
        )
    }

    private fun submitRequest() {
        val requestPoints: ArrayList<RequestPoint> = ArrayList()
        for (item in MAIN.appViewModel.listPoint){
            val latitude = item.latitude!!.toDouble()
            val longitude = item.longitude!!.toDouble()
            val p = Point(latitude, longitude)
            requestPoints.add(RequestPoint(
                p,
                RequestPointType.WAYPOINT,
                null
            ))
        }
        val center =  Point(
            (requestPoints.first().point.latitude + requestPoints.last().point.latitude) / 2,
            (requestPoints.first().point.longitude + requestPoints.last().point.longitude) / 2)
        drivingSession =
            drivingRouter.requestRoutes(requestPoints, TimeOptions(), this)
        binding.mapView.map.move(
            CameraPosition(
                center, 13F, 0F, 0F
            )
        )
    }

    private fun calculateDistance(p1: ru.spb.rollers.models.Point, p2: ru.spb.rollers.models.Point): Double {
        val earthRadius = 6371.0
        val lat1 = p1.latitude!!.toDouble()
        val lat2 = p2.latitude!!.toDouble()
        val lon1 = p1.longitude!!.toDouble()
        val lon2 = p2.longitude!!.toDouble()
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)
        val a = sin(dLat / 2) * sin(dLat / 2) +
                cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) *
                sin(dLon / 2) * sin(dLon / 2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        return earthRadius * c
    }


    override fun onMasstransitRoutes(routes: MutableList<Route>) {
        mapObjects.clear()
        for (route in routes) {
            mapObjects.addPolyline(route.geometry)
        }
    }

    override fun onMasstransitRoutesError(error: Error) {
        var errorMessage = "getString(R.string.unknown_error_message)"
        if (error is RemoteError) {
            errorMessage = "getString(R.string.remote_error_message)"
        } else if (error is NetworkError) {
            errorMessage = "getString(R.string.network_error_message)"
        }
        Toast.makeText(MAIN, errorMessage, Toast.LENGTH_SHORT).show()
    }
}