package ru.spb.rollers.model

class Waypoint {
    var waypointID: Int = 0
    var waypointName: String? = ""

    constructor(waypointID: Int, waypointName: String?) {
        this.waypointID = waypointID
        this.waypointName = waypointName
    }
}