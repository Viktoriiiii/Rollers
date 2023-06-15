package ru.spb.rollers.models

data class Event (
    var id: String = "",
    var managerId: String = "",
    var name: String = "",
    var date: String = "",
    var time: String = "",
    var dateTime: Any = "",
    var description: String = "",
    var speed: String = "",
    var cost: Double = 0.0,
    var photo: String = ""
)