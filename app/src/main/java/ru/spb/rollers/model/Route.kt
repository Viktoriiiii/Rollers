package ru.spb.rollers.model

class Route {
    var routeID: Int = 0
    var routeName: String? = ""
    var routeStartLocation: String? = ""
    var routeEndLocation: String? = ""
    var routeDistance: String? = ""
    var routeWayPoints = mutableListOf("")

    constructor(
        routeID: Int,
        routeName: String?,
        routeStartLocation: String?,
        routeEndLocation: String?,
        routeDistance: String?,
        routeWayPoints: MutableList<String>
    ) {
        this.routeID = routeID
        this.routeName = routeName
        this.routeStartLocation = routeStartLocation
        this.routeEndLocation = routeEndLocation
        this.routeDistance = routeDistance
        this.routeWayPoints = routeWayPoints
    }

    constructor(
        routeID: Int,
        routeName: String?,
        routeStartLocation: String?,
        routeEndLocation: String?,
        routeDistance: String?
    ) {
        this.routeID = routeID
        this.routeName = routeName
        this.routeStartLocation = routeStartLocation
        this.routeEndLocation = routeEndLocation
        this.routeDistance = routeDistance
    }
}