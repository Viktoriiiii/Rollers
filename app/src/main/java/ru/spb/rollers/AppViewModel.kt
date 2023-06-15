package ru.spb.rollers

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.spb.rollers.models.*

class AppViewModel : ViewModel() {
    val liveData = MutableLiveData<Boolean>()
    var user: User = User(role = String(), email = String(), password = String(), isManager = false)
    var contact: Contact = Contact()
    var contactForMessages = User()

    var addingPoint = false
    var buildRoute = false
    var clearList = false

    var listPoint: MutableList<Point> = mutableListOf()

    var route: Route = Route()
    var points: MutableList<Point> = mutableListOf()
    var event: Event = Event()

    init {
        liveData.value = false
    }
}