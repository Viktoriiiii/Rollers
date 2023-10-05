package ru.spb.rollers.holders

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.view.menu.MenuBuilder
import androidx.appcompat.view.menu.MenuPopupHelper
import androidx.appcompat.widget.PopupMenu
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import ru.spb.rollers.*
import ru.spb.rollers.models.Contact
import ru.spb.rollers.models.User

class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val contactContainer: CardView = itemView.findViewById(R.id.contact_container)
    private val ivPhoto: ImageView = itemView.findViewById(R.id.iv_photo)
    private val txvName: TextView = itemView.findViewById(R.id.tv_name)
    private val txvDistrict: TextView = itemView.findViewById(R.id.tv_district)
    private val txvStatus: TextView = itemView.findViewById(R.id.tv_status)
    private val ivStatus: ImageView = itemView.findViewById(R.id.iv_status)
    private val ivToContact: ImageView = itemView.findViewById(R.id.iv_to_contact)

    @SuppressLint("SetTextI18n")
    fun bind(user: User) {
        showInfoAboutUser(user)

        // проверка на то, есть ли указанный пользователь в контактах
        REF_DATABASE_CONTACT.child(MAIN.appViewModel.user.id).child(user.id)
            .addListenerForSingleValueEvent(object :
                ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        ivToContact.setImageResource(R.drawable.ic_done_foreground)
                    } else {
                        ivToContact.setImageResource(R.drawable.ic_add_contact_foreground)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })

        if (user.id == MAIN.appViewModel.user.id || MAIN.appViewModel.user.role == "Администратор")
            ivToContact.visibility = View.GONE
        else
            ivToContact.visibility = View.VISIBLE

        contactContainer.setOnClickListener {
            showPopupMenu(it, user)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun showInfoAboutUser(user: User) {

        Glide.with(itemView.context)
            .load(user.photo)
            .placeholder(R.drawable.avatar)
            .into(ivPhoto)

        if (user.role == "Организатор") {
            if (user.schoolName.isNullOrEmpty())
                txvName.text = "Неизвестный организатор"
            else
                txvName.text = user.schoolName
            ivStatus.visibility = View.GONE
            if (user.address.isNullOrEmpty())
                txvStatus.text = "Адрес не известен"
            else
                txvStatus.text = user.address

            txvDistrict.visibility = View.GONE
        } else {
            if (!user.lastName.isNullOrEmpty() || !user.firstName.isNullOrEmpty())
                txvName.text = "${user.lastName} ${user.firstName}"
            else
                txvName.text = "Неизвестный пользователь"
            ivStatus.setImageResource(R.drawable.ic_rollers_foreground)

            txvStatus.text = user.status
            if (user.status == "На роликах") {
                ivStatus.setImageResource(R.drawable.ic_rollers_foreground)
            } else {
                ivStatus.setImageResource(R.drawable.ic_inactive_foreground)
            }

            if (user.district.isNullOrEmpty())
                txvDistrict.text = "Район прогулок не известен"
            else
                txvDistrict.text = user.district

            txvDistrict.visibility = View.VISIBLE
        }
    }

    @SuppressLint("RestrictedApi", "SetTextI18n")
    private fun showPopupMenu(view: View, contact: User) {
        val popupMenu = PopupMenu(view.context, view)
        if (MAIN.appViewModel.user.role == "Администратор") {
            popupMenu.inflate(R.menu.contact_for_admin)
        } else if (contact.id == MAIN.appViewModel.user.id) {
            popupMenu.inflate(R.menu.contact_for_admin)
        } else {
            popupMenu.inflate(R.menu.contact_common_popup_menu)
            checkIcon(contact, popupMenu)
        }

        popupMenu.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.toViewProfile -> {
                    viewProfile(view, contact)
                    true
                }
                R.id.toDialog -> {
                    toDialog(contact)
                    true
                }
                R.id.deleteContact -> {
                    deleteContact(view, contact)
                    true
                }
                R.id.addContact -> {
                    addContact(view, contact)
                    true
                }
                else -> false
            }
        }
        val menuHelper = MenuPopupHelper(
            view.context,
            popupMenu.menu as MenuBuilder, view
        )
        menuHelper.setForceShowIcon(true)
        menuHelper.show()
    }

    @SuppressLint("SetTextI18n")
    private fun viewProfile(view: View, contact: User) {
        val builderViewProfile: AlertDialog.Builder = AlertDialog.Builder(MAIN)
        val profileView: View? = if (contact.role == "Организатор") {
            MAIN.layoutInflater.inflate(R.layout.view_profile_school, null)
        } else {
            MAIN.layoutInflater.inflate(R.layout.view_profile, null)
        }

        val txvSchoolName: TextView?
        val txvDescription: TextView?
        val txvSchoolAddress: TextView?
        val txvSchoolPhone: TextView?

        val txvName: TextView?
        val textViewStatus: TextView?
        val txvBirthday: TextView?
        val txvDistrict: TextView?
        val ivStatus: ImageView?

        if (contact.role == "Организатор") {
            txvSchoolName = profileView?.findViewById(R.id.tv_school_name)
            txvDescription = profileView?.findViewById(R.id.tv_description)
            txvSchoolAddress = profileView?.findViewById(R.id.tv_school_address)
            txvSchoolPhone = profileView?.findViewById(R.id.tv_school_phone)

            txvSchoolName?.text = if (contact.schoolName.isNullOrEmpty()) "Неизвестный организатор"
            else contact.schoolName
            txvDescription?.text = if (contact.description.isNullOrEmpty()) "Описание не добалено"
            else contact.description

            if (contact.address.isNullOrEmpty())
                txvSchoolAddress?.text = "Адрес не известен"
            else
                txvSchoolAddress?.text = contact.address

            if (contact.phone.isNullOrEmpty())
                txvSchoolPhone?.text = "Телефон не известен"
            else
                txvSchoolPhone?.text = contact.phone
        } else {
            txvName = profileView?.findViewById(R.id.tv_name)
            textViewStatus = profileView?.findViewById(R.id.tv_status)
            txvBirthday = profileView?.findViewById(R.id.tv_birthday)
            txvDistrict = profileView?.findViewById(R.id.tv_district)
            ivStatus = profileView?.findViewById(R.id.iv_status)

            if (!contact.lastName.isNullOrEmpty() || !contact.firstName.isNullOrEmpty())
                txvName?.text = "${contact.lastName} ${contact.firstName}"
            else
                txvName?.text = "Неизвестный пользователь"
            ivStatus?.setImageResource(R.drawable.ic_rollers_foreground)
            textViewStatus?.text = "Статус:  ${contact.status}"
            if (contact.status == "На роликах") {
                ivStatus?.setImageResource(R.drawable.ic_rollers_foreground)
            } else {
                ivStatus?.setImageResource(R.drawable.ic_inactive_foreground)
            }

            if (contact.district.isNullOrEmpty())
                txvDistrict?.text = "Район прогулок не известен"
            else
                txvDistrict?.text = "Район:  ${contact.district}"

            if (contact.birthday.isNullOrEmpty())
                txvBirthday?.text = "Дата рождения не указана"
            else
                txvBirthday?.text = "Дата рождения: ${contact.birthday}"
        }
        val ivPhoto: ImageView? = profileView?.findViewById(R.id.iv_photo)
        if (ivPhoto != null) {
            Glide.with(view.context)
                .load(contact.photo)
                .placeholder(R.drawable.avatar)
                .into(ivPhoto)
        }

        val imageViewClose: ImageView = profileView!!.findViewById(R.id.iv_close)
        builderViewProfile.setView(profileView)
        val alertViewProfile: AlertDialog = builderViewProfile.create()
        alertViewProfile.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        alertViewProfile.setOnShowListener {
            imageViewClose.setOnClickListener { alertViewProfile.cancel() }
        }
        alertViewProfile.show()
    }

    private fun toDialog(user: User) {
        MAIN.appViewModel.contactForMessages = user
        try {
            MAIN.navController.navigate(R.id.action_contacts_to_messagesFragment)
        } catch (ex: Exception) {
            try {
                MAIN.navController.navigate(R.id.action_contactsSearchFragment_to_messagesFragment)
            } catch (ex: Exception) {
                MAIN.navController.navigate(R.id.action_eventParticipantFragment_to_messagesFragment)
            }
        }
    }

    private fun addContact(view: View, contact: User) {
        val c = Contact()
        c.id = contact.id
        REF_DATABASE_ROOT.child("Contact").child(MAIN.appViewModel.user.id)
            .child(contact.id)
            .setValue(c)
            .addOnSuccessListener {
                val v = view.findViewById<ImageView>(R.id.iv_to_contact)
                v.setImageResource(R.drawable.ic_done_foreground)
                Toast.makeText(MAIN, "Контакт добавлен", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                // Произошла ошибка при удалении контакта
            }
    }

    private fun deleteContact(view: View, contact: User) {
        val builderDeleteDialog: AlertDialog.Builder = AlertDialog.Builder(MAIN)
        builderDeleteDialog
            .setTitle("Вы уверены, что хотите удалить контакт?")
            .setCancelable(false)
            .setPositiveButton("Да") { _, _ ->
                REF_DATABASE_CONTACT.child(MAIN.appViewModel.user.id).child(contact.id)
                    .removeValue()
                    .addOnSuccessListener {
                        val v = view.findViewById<ImageView>(R.id.iv_to_contact)
                        v.setImageResource(R.drawable.ic_add_contact_foreground)
                        Toast.makeText(MAIN, "Контакт удален", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener {
                        // Произошла ошибка при удалении контакта
                    }
            }
            .setNegativeButton("Отмена") { dialog, _ ->
                dialog.cancel()
            }
        val alertDialogDeletePhoto: AlertDialog = builderDeleteDialog.create()
        alertDialogDeletePhoto.show()
    }

    private fun checkIcon(contact: User, popupMenu: PopupMenu) {
        REF_DATABASE_CONTACT.child(MAIN.appViewModel.user.id).child(contact.id)
            .addListenerForSingleValueEvent(object :
                ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists())
                        popupMenu.inflate(R.menu.contact_delete_popup_menu)
                    else
                        popupMenu.inflate(R.menu.contact_add_popup_menu)
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
    }
}