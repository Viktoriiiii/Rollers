package ru.spb.rollers

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.spb.rollers.models.User

class AppViewModel : ViewModel() {
    val liveData = MutableLiveData<Boolean>()

    var user: User = User()

    init {
        liveData.value = false
    }
}