package ru.spb.rollers

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.spb.rollers.models.Contact
import ru.spb.rollers.models.Point
import ru.spb.rollers.models.User

class AppViewModel : ViewModel() {
    val liveData = MutableLiveData<Boolean>()
    var user: User = User(role = String(), email = String(), password = String(), isManager = false)
    var contact: Contact = Contact()
    var contactForMessages = User()

    var addingPoint = false

    var listPoint: List<Point> = mutableListOf()

    init {
        liveData.value = false
    }
}