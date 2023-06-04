package ru.spb.rollers

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import ru.spb.rollers.models.User

class AppViewModel : ViewModel() {
    val liveData = MutableLiveData<Boolean>()

    lateinit var AUTH: FirebaseAuth
    lateinit var REF_DATABASE_ROOT: DatabaseReference
    lateinit var REF_DATABASE_USER: DatabaseReference

    var user: User = User("", "","", false)

    init {
        liveData.value = false
    }
}