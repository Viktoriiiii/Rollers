package ru.spb.rollers.models

data class Event (
    var id: String = "",
    var managerId: String = "",
    var name: String = "",
    var date: String = "",
    var time: String = "",
    var description: String = "",
    var speed: String = "",
    var cost: String = "",
    var photo: String = ""
)