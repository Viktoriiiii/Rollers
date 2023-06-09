package ru.spb.rollers.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.spb.rollers.*
import ru.spb.rollers.holders.UserViewHolder
import ru.spb.rollers.models.User

class UserAdapter(private var listUsers: MutableList<User>
): RecyclerView.Adapter<UserViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup,viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_contact, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = listUsers[position]
        holder.bind(user)
    }

    override fun getItemCount(): Int {
        return listUsers.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setList(searchList: MutableList<User>) {
        listUsers = searchList
        notifyDataSetChanged()
    }
}