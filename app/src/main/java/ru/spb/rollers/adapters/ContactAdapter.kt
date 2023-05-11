package ru.spb.rollers.adapters

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.view.menu.MenuBuilder
import androidx.appcompat.view.menu.MenuPopupHelper
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.google.android.material.textview.MaterialTextView
import ru.spb.rollers.MAIN
import ru.spb.rollers.R
import ru.spb.rollers.model.Contact

class ContactAdapter (private var itemListContact: List<Contact>
): RecyclerView.Adapter<ContactAdapter.ContactViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_contact, parent, false)
        return ContactViewHolder(view)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        val item = itemListContact[position]
        holder.contactContainer
        holder.ivPhoto.setImageResource(R.drawable.logo)
        holder.textViewName.text = item.contactName
        holder.txvDistrict.text = item.contactDistrict
        holder.txvAge.text = item.contactAge
        holder.imageViewStatus.setImageResource(R.drawable.ic_rollers_foreground)
        if (item.contactStatus){
            holder.textViewStatus.text = "На роликах"
            holder.imageViewStatus.setImageResource(R.drawable.ic_rollers_foreground)
        } else {
            holder.textViewStatus.text = "Не активен"
            holder.imageViewStatus.setImageResource(R.drawable.ic_inactive_foreground)
        }
        if (item.isContact) holder.imageViewToContact.setImageResource(R.drawable.ic_done_foreground)
        else holder.imageViewToContact.setImageResource(R.drawable.ic_add_contact_foreground)
    }

    override fun getItemCount(): Int {
        return itemListContact.size
    }

    inner class ContactViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener{
        val contactContainer: MaterialCardView = itemView.findViewById(R.id.contactContainer)
        val ivPhoto: ImageView = itemView.findViewById(R.id.imageViewPhoto)
        val textViewName: TextView = itemView.findViewById(R.id.textViewName)
        val txvDistrict: MaterialTextView = itemView.findViewById(R.id.txvDistrict)
        val txvAge: TextView = itemView.findViewById(R.id.txvAge)
        val textViewStatus: TextView = itemView.findViewById(R.id.textViewStatus)
        val imageViewStatus: ImageView = itemView.findViewById(R.id.imageViewStatus)
        val imageViewToContact: ImageView = itemView.findViewById(R.id.imageViewToContact)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(p0: View) {
            showPopupMenu(p0)
        }

        @SuppressLint("RestrictedApi")
        private fun showPopupMenu(view: View) {
            val popupMenu = PopupMenu(view.context, view)
            popupMenu.inflate(R.menu.contact_popup_menu)
            popupMenu.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.toViewProfile -> {
                        val builderViewProfile: AlertDialog.Builder = AlertDialog.Builder(MAIN)

                        val profileView: View = MAIN.layoutInflater.inflate(R.layout.view_profile, null)
                        builderViewProfile.setView(profileView)
                        val alertViewProfile: AlertDialog = builderViewProfile.create()
                        alertViewProfile.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                        alertViewProfile.show()
                        true
                    }
                    R.id.toDialog -> {
                        try {
                            MAIN.navController.navigate(R.id.action_contacts_to_messages)
                        }
                        catch (ex: Exception){
                            MAIN.navController.navigate(R.id.action_contactSearch_to_messages)
                        }
                        true
                    }
                    R.id.deleteContact -> {
                        val builderDeleteDialog: AlertDialog.Builder = AlertDialog.Builder(MAIN)
                        builderDeleteDialog
                            .setTitle("Вы уверены, что хотите удалить контакт?")
                            .setCancelable(false)
                            .setPositiveButton("Да") { _, _ ->
                                Toast.makeText(MAIN, "Контакт удален", Toast.LENGTH_SHORT).show()
                            }
                            .setNegativeButton("Отмена"){dialog, _ ->
                                dialog.cancel()
                            }
                        val alertDialogDeletePhoto: AlertDialog = builderDeleteDialog.create()
                        alertDialogDeletePhoto.show()
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
    }
}