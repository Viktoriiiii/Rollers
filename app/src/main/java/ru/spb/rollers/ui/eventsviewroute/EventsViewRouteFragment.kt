package ru.spb.rollers.ui.eventsviewroute

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.yandex.mapkit.MapKit
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.RequestPoint
import com.yandex.mapkit.RequestPointType
import com.yandex.mapkit.directions.DirectionsFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.MapObjectCollection
import com.yandex.mapkit.transport.TransportFactory
import com.yandex.mapkit.transport.masstransit.PedestrianRouter
import com.yandex.mapkit.transport.masstransit.Route
import com.yandex.mapkit.transport.masstransit.Session
import com.yandex.mapkit.transport.masstransit.TimeOptions
import com.yandex.runtime.Error
import com.yandex.runtime.network.NetworkError
import com.yandex.runtime.network.RemoteError
import ru.spb.rollers.MAIN
import ru.spb.rollers.REF_DATABASE_EVENT
import ru.spb.rollers.databinding.EventsViewRouteFragmentBinding
import ru.spb.rollers.getPointModel

class EventsViewRouteFragment : Fragment(), Session.RouteListener {

    private var _binding: EventsViewRouteFragmentBinding? = null
    private val binding get() = _binding!!

    private var mapKit: MapKit? = null

    private lateinit var mapObjects: MapObjectCollection
    private lateinit var drivingRouter: PedestrianRouter
    private lateinit var drivingSession: Session

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        DirectionsFactory.initialize(MAIN)
        TransportFactory.initialize(MAIN)
        _binding = EventsViewRouteFragmentBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.ivBack.setOnClickListener{
            MAIN.onSupportNavigateUp()
        }

        mapKit = MapKitFactory.getInstance()
        drivingRouter = TransportFactory.getInstance().createPedestrianRouter()
        mapObjects = binding.mapView.map.mapObjects.addCollection()
        if (MAIN.appViewModel.route.id != ""){
            REF_DATABASE_EVENT.child(MAIN.appViewModel.event.id).child("route")
                .child("Points").addValueEventListener(object : ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()){
                            MAIN.appViewModel.points = snapshot.children.map { it.getPointModel() } as MutableList<ru.spb.rollers.models.Point>
                            buildRoute()
                        }
                    }
                    override fun onCancelled(error: DatabaseError) {}
                })
        }
    }

    private fun buildRoute(){
        submitRequest()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun submitRequest() {
        val requestPoints: ArrayList<RequestPoint> = ArrayList()
        for (item in MAIN.appViewModel.points){
            val latitude = item.latitude!!.toDouble()
            val longitude = item.longitude!!.toDouble()
            val p = Point(latitude, longitude)
            requestPoints.add(
                RequestPoint(
                p,
                RequestPointType.WAYPOINT,
                null
            )
            )
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
}