package ru.spb.rollers.oldadapters

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
import ru.spb.rollers.oldmodel.Contact

class ContactAdapter (private var itemListContact: List<Contact>
): RecyclerView.Adapter<ContactAdapter.ContactViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_contact, parent, false)
        return ContactViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        val item = itemListContact[position]
        holder.contactContainer

        holder.ivPhoto.setImageResource(R.drawable.logo)

        if (item.isManager){
            holder.txvName.text = item.contactSchoolName
            holder.ivStatus.visibility = View.GONE
            holder.txvStatus.text = item.contactAddress
        }
        else {
            holder.txvName.text = "${item.contactLastName} ${item.contactFirstName}"
            holder.ivStatus.setImageResource(R.drawable.ic_rollers_foreground)
            if (item.contactStatus){
                holder.txvStatus.text = "На роликах"
                holder.ivStatus.setImageResource(R.drawable.ic_rollers_foreground)
            } else {
                holder.txvStatus.text = "Не активен"
                holder.ivStatus.setImageResource(R.drawable.ic_inactive_foreground)
            }
        }

        holder.txvDistrict.text = item.contactDistrict

        if (item.isContact) holder.ivToContact.setImageResource(R.drawable.ic_done_foreground)
        else holder.ivToContact.setImageResource(R.drawable.ic_add_contact_foreground)

        holder.contactContainer.setOnClickListener{
            showPopupMenu(it, item)
        }
    }

    @SuppressLint("RestrictedApi")
    private fun showPopupMenu(view: View, contact: Contact) {
        val popupMenu = PopupMenu(view.context, view)

        if (contact.roleID == 1){
            popupMenu.inflate(R.menu.contact_for_admin)
        }
        else {
            if (contact.isContact)
                popupMenu.inflate(R.menu.contact_delete_popup_menu)
            else
                popupMenu.inflate(R.menu.contact_add_popup_menu)
        }

        popupMenu.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.toViewProfile -> {
                    val builderViewProfile: AlertDialog.Builder = AlertDialog.Builder(MAIN)
                    var profileView: View? = null
                    profileView = if (contact.isManager){
                        MAIN.layoutInflater.inflate(R.layout.view_profile_school, null)
                    } else {
                        MAIN.layoutInflater.inflate(R.layout.view_profile, null)
                    }
                    val imageViewClose: ImageView = profileView.findViewById(R.id.imageViewClose)
                    val imageViewToAdmin: ImageView = profileView.findViewById(R.id.imageViewToAdmin)
                    builderViewProfile.setView(profileView)
                    val alertViewProfile: AlertDialog = builderViewProfile.create()
                    alertViewProfile.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                    alertViewProfile.setOnShowListener {
                        imageViewClose.setOnClickListener { alertViewProfile.cancel() }

                        if (contact.roleID == 1) imageViewToAdmin.setImageResource(R.drawable.ic_person_foreground)
                        else imageViewToAdmin.setImageResource(R.drawable.ic_manage_account_foreground)

                        imageViewToAdmin.setOnClickListener{
                            val title: String = if (contact.roleID == 1)
                                "Изменить роль участника на организатора?" else
                                    "Изменить роль организатора на участника?"
                            val builderToAdminDialog: AlertDialog.Builder = AlertDialog.Builder(MAIN)
                            builderToAdminDialog
                                .setTitle(title)
                                .setCancelable(false)
                                .setPositiveButton("Да") { _, _ ->
                                    Toast.makeText(MAIN, "Выполнено", Toast.LENGTH_SHORT).show()
                                }
                                .setNegativeButton("Отмена"){dialog, _ ->
                                    dialog.cancel()
                                }
                            val alertDialogToAdmin: AlertDialog = builderToAdminDialog.create()
                            alertDialogToAdmin.show()
                        }
                    }
                    alertViewProfile.show()
                    true
                }
                R.id.toDialog -> {
                    try {
                        MAIN.navController.navigate(R.id.action_contacts_to_messagesFragment)
                    }
                    catch (ex: Exception){
                        MAIN.navController.navigate(R.id.action_eventParticipantFragment_to_messagesFragment)
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
                R.id.addContact -> {
                    Toast.makeText(MAIN, "Контакт добвлен", Toast.LENGTH_SHORT).show()
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
        return itemListContact.size
    }

    inner class ContactViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val contactContainer: MaterialCardView = itemView.findViewById(R.id.contactContainer)
        val ivPhoto: ImageView = itemView.findViewById(R.id.ivPhoto)
        val txvName: TextView = itemView.findViewById(R.id.txvName)
        val txvDistrict: MaterialTextView = itemView.findViewById(R.id.txvDistrict)
        val txvStatus: TextView = itemView.findViewById(R.id.txvStatus)
        val ivStatus: ImageView = itemView.findViewById(R.id.ivStatus)
        val ivToContact: ImageView = itemView.findViewById(R.id.ivToContact)
    }
}