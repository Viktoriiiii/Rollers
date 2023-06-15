package ru.spb.rollers

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import ru.spb.rollers.models.*
import java.text.SimpleDateFormat
import java.util.*

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
lateinit var REF_DATABASE_DIALOG: DatabaseReference
lateinit var REF_DATABASE_ROUTE: DatabaseReference
lateinit var REF_DATABASE_EVENT: DatabaseReference
lateinit var REF_DATABASE_EVENT_USER: DatabaseReference
lateinit var REF_DATABASE_EVENT_PARTICIPANT: DatabaseReference

const val FOLDER_PROFILE_IMAGE = "profile_image"
const val FOLDER_EVENT_IMAGE = "event_image"

const val CHILD_PHOTO_URL = "userPhoto"

fun initFirebase() {
    AUTH = FirebaseAuth.getInstance()
    REF_DATABASE_ROOT = FirebaseDatabase.getInstance().reference
    REF_DATABASE_USER = FirebaseDatabase.getInstance().getReference("User")
    REF_DATABASE_CONTACT = FirebaseDatabase.getInstance().getReference("Contact")
    REF_DATABASE_DIALOG = FirebaseDatabase.getInstance().getReference("Dialog")
    REF_DATABASE_ROUTE = FirebaseDatabase.getInstance().getReference("Route")
    REF_DATABASE_EVENT = FirebaseDatabase.getInstance().getReference("Event")
    REF_DATABASE_EVENT_USER = FirebaseDatabase.getInstance().getReference("Event_User")
    REF_DATABASE_EVENT_PARTICIPANT = FirebaseDatabase.getInstance().getReference("Event_Participant")
    REF_STORAGE_ROOT = FirebaseStorage.getInstance().reference
}

fun String.asTime(): String {
    val time = Date(this.toLong())
    val timeFormat = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
    return timeFormat.format(time)
}

fun DataSnapshot.getUserModel(): User =
    this.getValue(User::class.java) ?: User()

fun DataSnapshot.getMessageModel(): Message =
    this.getValue(Message::class.java) ?: Message()

fun DataSnapshot.getDialogModel(): Dialog =
    this.getValue(Dialog::class.java) ?: Dialog()

fun DataSnapshot.getRouteModel(): Route =
    this.getValue(Route::class.java) ?: Route()

fun DataSnapshot.getPointModel(): Point =
    this.getValue(Point::class.java) ?: Point()

fun DataSnapshot.getEventModel(): Event =
    this.getValue(Event::class.java) ?: Event()

fun DataSnapshot.getEventUserModel(): EventUser =
    this.getValue(EventUser::class.java) ?: EventUser()