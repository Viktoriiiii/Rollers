package ru.spb.rollers

import ru.spb.rollers.model.Contact

lateinit var MAIN: AppActivity

var isMapKitInitialized = false

var delayMillis = 2000L

var titleEvents = ""

var titleRoutes = ""

var roleId = 2 // 1 - администратор, 2 - организатор, 3 - участник

var user = Contact(1,
    1, "login", "password")