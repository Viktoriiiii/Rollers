package ru.spb.rollers

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.spb.rollers.models.Contact
import ru.spb.rollers.models.User

class AppViewModel : ViewModel() {
    val liveData = MutableLiveData<Boolean>()
    var user: User = User("", "","", false)
    var contact: Contact = Contact()

    init {
        liveData.value = false
    }
}