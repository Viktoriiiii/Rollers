package ru.spb.rollers

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

lateinit var MAIN: AppActivity

var isMapKitInitialized = false

var delayMillis = 2000L

var titleEvents = ""

var titleRoutes = ""

var roleId = 3 // 1 - администратор, 2 - организатор, 3 - участник


lateinit var AUTH: FirebaseAuth
lateinit var REF_DATABASE_ROOT: DatabaseReference
lateinit var REF_STORAGE_ROOT: StorageReference
lateinit var REF_DATABASE_USER: DatabaseReference
const val FOLDER_PROFILE_IMAGE = "profile_image"
lateinit var CURRENT_UID:String

const val CHILD_PHOTO_URL = "userPhoto"

fun initFirebase() {
    AUTH = FirebaseAuth.getInstance()
    REF_DATABASE_ROOT = FirebaseDatabase.getInstance().reference
    REF_DATABASE_USER = FirebaseDatabase.getInstance().getReference("User")
    CURRENT_UID =  AUTH.currentUser?.uid.toString()
    REF_STORAGE_ROOT = FirebaseStorage.getInstance().reference
}
