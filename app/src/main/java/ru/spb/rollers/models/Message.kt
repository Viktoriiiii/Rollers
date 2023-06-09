package ru.spb.rollers.models

data class Message (
    var from: String = "",
    var text: String = "",
    var timeStamp: Any = "",
    var read: Boolean = false
)