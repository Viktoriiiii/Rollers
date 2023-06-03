package ru.spb.rollers

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference

lateinit var MAIN: AppActivity

var isMapKitInitialized = false

var delayMillis = 2000L

var titleEvents = ""

var titleRoutes = ""

var roleId = 3 // 1 - администратор, 2 - организатор, 3 - участник

lateinit var AUTH: FirebaseAuth
lateinit var REF_DATABASE_ROOT: DatabaseReference
lateinit var REF_DATABASE_USER: DatabaseReference