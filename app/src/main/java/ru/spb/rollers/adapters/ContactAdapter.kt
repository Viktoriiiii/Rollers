package ru.spb.rollers.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import ru.spb.rollers.*
import ru.spb.rollers.holders.UserViewHolder
import ru.spb.rollers.models.User

class ContactAdapter(options: FirebaseRecyclerOptions<User>) :
    FirebaseRecyclerAdapter<User,UserViewHolder>(options)
{

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_contact, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int, user: User) {
        // вывод только контактов
        REF_DATABASE_USER.child(user.id).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    val contact = snapshot.getValue<User>()!!
                    holder.bind(contact)
                }
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
    }
}