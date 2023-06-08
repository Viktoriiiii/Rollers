package ru.spb.rollers.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions

import ru.spb.rollers.*
import ru.spb.rollers.holders.UserViewHolder
import ru.spb.rollers.models.User

class UserAdapter(options: FirebaseRecyclerOptions<User>) :
    FirebaseRecyclerAdapter<User, UserViewHolder>(options)
     {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_contact, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int, user: User) {
        holder.bind(user)
    }
 }