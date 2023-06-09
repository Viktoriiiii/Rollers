package ru.spb.rollers.models

data class Dialog(
    var id: String = "",
    var pinned: Boolean = false,
    var message: ArrayList<Message> = arrayListOf()
)