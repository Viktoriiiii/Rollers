package ru.spb.rollers.model

class Event {
    var eventID: Int = 0
    var eventName: String? = ""
    var eventStartLocation: String? = ""
    var eventEndLocation: String? = ""
    var eventNameOrganizer: String? = ""
    var eventDate: String? = ""
    var eventTimeMeeting: String? = ""
    var eventTimeStart: String? = ""
    var eventSpeed: String? = ""
    var eventDistance: String? = ""
    var eventDescription: String? = ""
    var eventPitStops: String? = ""
    var isParticipate: Boolean = false

    constructor(
        eventID: Int,
        eventName: String?,
        eventStartLocation: String?,
        eventEndLocation: String?,
        eventNameOrganizer: String?,
        eventDate: String?,
        eventTimeMeeting: String?,
        eventTimeStart: String?,
        eventSpeed: String?,
        eventDistance: String?,
        eventDescription: String?,
        isParticipate: Boolean
    ) {
        this.eventID = eventID
        this.eventName = eventName
        this.eventStartLocation = eventStartLocation
        this.eventEndLocation = eventEndLocation
        this.eventNameOrganizer = eventNameOrganizer
        this.eventDate = eventDate
        this.eventTimeMeeting = eventTimeMeeting
        this.eventTimeStart = eventTimeStart
        this.eventSpeed = eventSpeed
        this.eventDistance = eventDistance
        this.eventDescription = eventDescription
        this.isParticipate = isParticipate
    }
}