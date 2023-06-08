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

lateinit var AUTH: FirebaseAuth
lateinit var REF_DATABASE_ROOT: DatabaseReference
lateinit var REF_STORAGE_ROOT: StorageReference
lateinit var REF_DATABASE_USER: DatabaseReference
lateinit var REF_DATABASE_CONTACT: DatabaseReference

const val FOLDER_PROFILE_IMAGE = "profile_image"

const val CHILD_PHOTO_URL = "userPhoto"

fun initFirebase() {
    AUTH = FirebaseAuth.getInstance()
    REF_DATABASE_ROOT = FirebaseDatabase.getInstance().reference
    REF_DATABASE_USER = FirebaseDatabase.getInstance().getReference("User")
    REF_DATABASE_CONTACT = FirebaseDatabase.getInstance().getReference("Contact")
    REF_STORAGE_ROOT = FirebaseStorage.getInstance().reference
}
