package ru.spb.rollers.models

data class Route(
    var id: String? = "",
    var name: String? = "",
    var distance: String? = "",
    var pinned: Boolean = false
)