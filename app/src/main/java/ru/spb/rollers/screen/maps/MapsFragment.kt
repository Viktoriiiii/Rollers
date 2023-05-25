package ru.spb.rollers.screen.maps

import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.PointF
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKit
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.BoundingBox
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.layers.ObjectEvent
import com.yandex.mapkit.map.*
import com.yandex.mapkit.map.Map
import com.yandex.mapkit.mapview.MapView
import com.yandex.mapkit.search.*
import com.yandex.mapkit.user_location.UserLocationLayer
import com.yandex.mapkit.user_location.UserLocationObjectListener
import com.yandex.mapkit.user_location.UserLocationView
import com.yandex.runtime.Error
import com.yandex.runtime.image.ImageProvider
import com.yandex.runtime.network.NetworkError
import com.yandex.runtime.network.RemoteError
import ru.spb.rollers.MAIN
import ru.spb.rollers.R
import ru.spb.rollers.adapters.OnItemClickListener
import ru.spb.rollers.adapters.SearchAdapter
import ru.spb.rollers.databinding.MapsFragmentBinding
import ru.spb.rollers.databinding.ProfileFragmentBinding

class MapsFragment : Fragment(), UserLocationObjectListener, Session.SearchListener,
    CameraListener {

    companion object {
        fun newInstance() = MapsFragment()
    }

    private var _binding: MapsFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: MapsViewModel
    private val PERMISSIONS_REQUEST_FINE_LOCATION = 1
    private lateinit var mapView: MapView
    var mapKit: MapKit? = null
    private var userLocationLayer: UserLocationLayer? = null
    private lateinit var searchManager: SearchManager
    private lateinit var searchSession: Session

    private lateinit var recyclerView: RecyclerView
    private lateinit var searchAdapter: SearchAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MapsFragmentBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this)[MapsViewModel::class.java]
        // TODO: Use the ViewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.imageButtonBack.setOnClickListener{
            MAIN.onSupportNavigateUp()
        }
        recyclerView = view.findViewById(R.id.suggestList)

        mapView = view.findViewById(R.id.mapView)
        mapKit = MapKitFactory.getInstance()
        mapView.map.move(
            CameraPosition(Point(59.939427, 30.309217), 11.0f, 0.0f, 0.0f),
            Animation(Animation.Type.SMOOTH, 0F),
            null
        )

        binding.searchMyLocation.setOnClickListener{
            getMyLocation()
        }
        searchManager = SearchFactory.getInstance().createSearchManager(SearchManagerType.COMBINED)
        mapView.map.addCameraListener(this)

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
                val southWest = Point(59.681658, 29.369953) // Южно-западная граница города Санкт-Петербург
                val northEast = Point(60.130912, 30.645520)
                val boundingBox = BoundingBox(southWest, northEast)
                suggestSession.suggest(query, boundingBox, suggestOptions, object : SuggestSession.SuggestListener {
                    override fun onResponse(suggestItems: List<SuggestItem>) {
                        searchAdapter = SearchAdapter(suggestItems)
                        recyclerView.adapter = searchAdapter
                        searchAdapter.setOnItemClickListener(object : OnItemClickListener {
                            override fun onItemClick(item: SuggestItem) {
                                binding.searchView.setQuery(item.title.text, false)
                                mapView.map.move(
                                    CameraPosition(
                                        item.center!!,14.0f, 0.0f, 0.0f
                                    ),
                                    Animation(Animation.Type.SMOOTH, 4.0f),
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
            val mapObjects = mapView.map.mapObjects
            mapObjects.clear()
            true
        }

        mapView.map.move(
            CameraPosition(Point(59.945933, 30.320045), 14.0f, 0.0f, 0.0f)
        )
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
            mapView.map.isRotateGesturesEnabled = true
            mapKit!!.resetLocationManagerToDefault()
            userLocationLayer = mapKit!!.createUserLocationLayer(mapView.mapWindow)
        }
        userLocationLayer!!.isVisible = !userLocationLayer!!.isVisible
        userLocationLayer!!.isHeadingEnabled = false
        userLocationLayer!!.setObjectListener(this)
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

    override fun onObjectAdded(userLocationView: UserLocationView) {
        userLocationLayer!!.setAnchor(
            PointF((mapView.width * 0.5).toFloat(), (mapView.height * 0.5).toFloat()),
            PointF((mapView.width * 0.5).toFloat(), (mapView.height * 0.83).toFloat())
        )
        userLocationView.accuracyCircle.fillColor = Color.WHITE
    }

    override fun onObjectRemoved(view: UserLocationView) {
    }

    override fun onObjectUpdated(view: UserLocationView, event: ObjectEvent) {
    }

    override fun onSearchResponse(response: Response) {
        val mapObjects = mapView.map.mapObjects
        mapObjects.clear()

        if (!binding.searchView.query.isNullOrEmpty()) {
            for (searchResult in response.collection.children) {
                val resultLocation = searchResult.obj!!.geometry[0].point
                if (resultLocation != null) {
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
            VisibleRegionUtils.toPolygon(mapView.map.visibleRegion),
            SearchOptions(),
            this
        )
    }
}