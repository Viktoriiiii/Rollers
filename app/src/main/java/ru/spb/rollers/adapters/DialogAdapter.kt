package ru.spb.rollers.adapters

import android.annotation.SuppressLint
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.view.menu.MenuBuilder
import androidx.appcompat.view.menu.MenuPopupHelper
import androidx.appcompat.widget.PopupMenu
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.card.MaterialCardView
import com.google.android.material.textview.MaterialTextView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import ru.spb.rollers.*
import ru.spb.rollers.models.Dialog
import ru.spb.rollers.models.User


class DialogAdapter(
    private var itemListDialog: List<Dialog>,
): RecyclerView.Adapter<DialogAdapter.DialogViewHolder>() {

    override fun onCreateViewHolder (parent: ViewGroup, viewType: Int): DialogViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_dialog, parent, false)
        return DialogViewHolder(view)
    }

    override fun onBindViewHolder(holder: DialogViewHolder, position: Int) {
        val item = itemListDialog[position]

        REF_DATABASE_USER.child(item.id).addValueEventListener(object : ValueEventListener {
            @SuppressLint("SetTextI18n")
            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue<User>()!!
                if (user.role == "Организатор")
                    holder.mtvName.text = if (user.schoolName.isNullOrEmpty()) "Неизвестный организатор"
                    else user.schoolName
                else{
                    if (!user.lastName.isNullOrEmpty() || !user.firstName.isNullOrEmpty())
                        holder.mtvName.text = "${user.lastName} ${user.firstName}"
                    else
                        holder.mtvName.text = "Неизвестный пользователь"
                }
                Glide.with(MAIN)
                    .load(user.photo)
                    .placeholder(R.drawable.avatar)
                    .into(holder.ivPhoto)
            }
            override fun onCancelled(error: DatabaseError) {}
        })

        val refMessages = REF_DATABASE_DIALOG
            .child(MAIN.appViewModel.user.id)
            .child(item.id)
            .child("Messages")
        refMessages.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val messages = snapshot.children.map { it.getMessageModel() }
                val lastMessage = messages.last()
                holder.mtvMessage.text = if (MAIN.appViewModel.user.id == lastMessage.from)
                    "Вы: " + lastMessage.text else lastMessage.text
                val count = messages.count{ !it.read }

                holder.mtvMessage.typeface = if (!lastMessage.read) Typeface.defaultFromStyle(Typeface.BOLD)
                    else Typeface.defaultFromStyle(Typeface.ITALIC)

                holder.txvContMessage.text = count.toString()
                holder.cvCountMessages.visibility = if (count == 0) View.GONE else View.VISIBLE
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })

        holder.dialogContainer.setOnClickListener{
            REF_DATABASE_USER.child(item.id).addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val user = snapshot.getValue<User>()!!
                    MAIN.appViewModel.contactForMessages = user
                }
                override fun onCancelled(error: DatabaseError) {}
            })
            MAIN.navController.navigate(R.id.action_dialogs_to_messagesFragment)
        }

        holder.dialogContainer.setOnLongClickListener{
            showPopupMenu(it)
            true
        }
    }

    @SuppressLint("RestrictedApi")
    private fun showPopupMenu(view: View) {
        val popupMenu = PopupMenu(view.context, view)
        popupMenu.inflate(R.menu.dialog_popup_menu)
        popupMenu.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.pinDialog -> {
                    Toast.makeText(MAIN, "Диалог закреплен", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.deleteMessages -> {
                    Toast.makeText(MAIN, "Сообщения удалены", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.deleteDialog -> {
                    Toast.makeText(MAIN, "Диалог удален", Toast.LENGTH_SHORT).show()
                    true
                }
                else -> false
            }
        }

        val menuHelper = MenuPopupHelper(view.context,
            popupMenu.menu as MenuBuilder, view)
        menuHelper.setForceShowIcon(true)
        menuHelper.show()
    }

    override fun getItemCount(): Int {
        return itemListDialog.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setList(list: List<Dialog>) {
        itemListDialog = list
        notifyDataSetChanged()
    }

    inner class DialogViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val dialogContainer: MaterialCardView = itemView.findViewById(R.id.dialogContainer)
        val mtvName: MaterialTextView = itemView.findViewById(R.id.mtvName)
        var mtvMessage: MaterialTextView = itemView.findViewById(R.id.mtvMessage)
        val txvContMessage: MaterialTextView = itemView.findViewById(R.id.txvContMessage)
        val ivPhoto: ImageView = itemView.findViewById(R.id.ivPhoto)
        val cvCountMessages: CardView = itemView.findViewById(R.id.cvCountMessages)
    }
}
