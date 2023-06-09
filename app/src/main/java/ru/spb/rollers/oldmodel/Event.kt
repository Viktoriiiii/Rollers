package ru.spb.rollers.oldmodel

class Event {
    var eventID: Int = 0
    var eventName: String? = ""
    var eventStartLocation: String? = ""
    var eventEndLocation: String? = ""
    var eventManager: Int = 0
    var eventDate: String? = ""
    var eventTimeMeeting: String? = ""
    var eventTimeStart: String? = ""
    var eventSpeed: String? = ""
    var eventDistance: String? = ""
    var eventDescription: String? = ""
    var eventCost: Double = 0.0
    var eventPitStops: String? = ""
    var isParticipate: Boolean = false

    constructor(
        eventID: Int,
        eventName: String?,
        eventStartLocation: String?,
        eventEndLocation: String?,
        eventManager: Int,
        eventDate: String?,
        eventTimeMeeting: String?,
        eventTimeStart: String?,
        eventSpeed: String?,
        eventDistance: String?,
        eventDescription: String?,
        eventCost: Double,
        isParticipate: Boolean
    ) {
        this.eventID = eventID
        this.eventName = eventName
        this.eventStartLocation = eventStartLocation
        this.eventEndLocation = eventEndLocation
        this.eventManager = eventManager
        this.eventDate = eventDate
        this.eventTimeMeeting = eventTimeMeeting
        this.eventTimeStart = eventTimeStart
        this.eventSpeed = eventSpeed
        this.eventDistance = eventDistance
        this.eventDescription = eventDescription
        this.eventCost = eventCost
        this.isParticipate = isParticipate
    }
}