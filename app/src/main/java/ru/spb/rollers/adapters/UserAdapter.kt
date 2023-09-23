package ru.spb.rollers.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ru.spb.rollers.*
import ru.spb.rollers.holders.UserViewHolder
import ru.spb.rollers.models.User

class UserAdapter: RecyclerView.Adapter<UserViewHolder>() {

    var itemsUser = listOf <User>()
        set(value) {
            val callback = CommonCallbackImpl(
                oldItems = field,
                newItems = value,
                { oldItem: User, newItem: User ->  oldItem.id == newItem.id})
            field = value
            val diffResult = DiffUtil.calculateDiff(callback)
            diffResult.dispatchUpdatesTo(this)
        }

    override fun onCreateViewHolder(parent: ViewGroup,viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_contact, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = itemsUser[position]
        holder.bind(user)
    }

    override fun getItemCount() = itemsUser.size
}