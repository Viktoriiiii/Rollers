package ru.spb.rollers

import android.widget.ImageView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.squareup.picasso.Picasso
import ru.spb.rollers.models.*

class AppViewModel : ViewModel() {
    val liveData = MutableLiveData<Boolean>()
    var user: User = User(role = String(), email = String(), password = String(), isManager = false)
    var contact: Contact = Contact()
    var contactForMessages = User()

    var maps = 0

    var listPoint: MutableList<Point> = mutableListOf()

    var route: Route = Route()
    var points: MutableList<Point> = mutableListOf()
    var event: Event = Event()

    init {
        liveData.value = false
    }

    fun setPhoto(ivPhoto: ImageView){
        if (MAIN.appViewModel.user.photo != ""){
            Picasso.get()
                .load(MAIN.appViewModel.user.photo)
                .placeholder(R.drawable.avatar)
                .into(ivPhoto)
        }
    }
}