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
import ru.spb.rollers.model.Event

class EventAdapter (private var itemListEvent: List<Event>, var userID: Int
): RecyclerView.Adapter<EventAdapter.EventViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_event, parent, false)
        return EventViewHolder(view)
    }

    @SuppressLint("RestrictedApi", "SetTextI18n")
    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val item = itemListEvent[position]
        holder.eventsContainer
        holder.imageViewManage.setImageResource(R.drawable.ic_manage_account_foreground)
        holder.textViewEventName.text = item.eventName
        holder.txvEventDate.text = "Дата: ${item.eventDate}"
        holder.txvEventStartLocation.text = "Старт: ${item.eventStartLocation}"
        holder.txvEventEndLocation.text = "Финиш: ${item.eventEndLocation}"

        if (item.isParticipate) {
            holder.imageViewEventStatus.setImageResource(R.drawable.ic_done_foreground)
        }
        else {
            holder.imageViewEventStatus.setImageResource(R.drawable.ic_add_event_foreground)
        }

        if (userID == item.eventManager) {
            holder.imageViewManage.setImageResource(R.drawable.ic_manage_account_foreground)
        }
        else {
            holder.imageViewManage.visibility = View.GONE
        }

        holder.eventsContainer.setOnClickListener{
            val popupMenu = PopupMenu(it.context, it)

            if (userID == item.eventManager)
                popupMenu.inflate(R.menu.event_pm_for_manager)
            else if (item.isParticipate){
                popupMenu.inflate(R.menu.event_pm_for_participant)
            }
            else
                popupMenu.inflate(R.menu.event_pm_for_everybody)
            popupMenu.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.toViewEvent -> {
                        val builderViewProfile: AlertDialog.Builder = AlertDialog.Builder(MAIN)
                        val profileView: View = MAIN.layoutInflater.inflate(R.layout.view_event, null)
                        val imageViewClose: ImageView = profileView.findViewById(R.id.imageViewClose)
                        builderViewProfile.setView(profileView)
                        val alertViewProfile: AlertDialog = builderViewProfile.create()
                        alertViewProfile.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                        alertViewProfile.setOnShowListener {
                            imageViewClose.setOnClickListener { alertViewProfile.cancel() }
                        }
                        alertViewProfile.show()
                        true
                    }
                    R.id.toAddEvent -> {
                        Toast.makeText(MAIN, "Мероприятие добавлено", Toast.LENGTH_SHORT).show()
                        true
                    }
                    R.id.toChangeEvent ->{
                        try {
                            MAIN.navController.navigate(R.id.action_eventSearch_to_eventAdding)
                        }
                        catch (ex: Exception) {
                            MAIN.navController.navigate(R.id.action_events2_to_eventAdding)
                        }
                        true
                    }
                    R.id.deleteEvent -> {
                        val builderDeleteDialog: AlertDialog.Builder = AlertDialog.Builder(MAIN)
                        builderDeleteDialog
                            .setTitle("Вы уверены, что хотите удалить мероприятие?")
                            .setCancelable(false)
                            .setPositiveButton("Да") { _, _ ->
                                Toast.makeText(MAIN, "Мероприятие удален", Toast.LENGTH_SHORT).show()
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
            val menuHelper = MenuPopupHelper(it.context,
                popupMenu.menu as MenuBuilder, it)
            menuHelper.setForceShowIcon(true)
            menuHelper.show()
        }
    }

    override fun getItemCount(): Int {
        return itemListEvent.size
    }

    inner class EventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val eventsContainer: MaterialCardView = itemView.findViewById(R.id.eventsContainer)
        val textViewEventName: TextView = itemView.findViewById(R.id.textViewEventName)
        val txvEventDate: MaterialTextView = itemView.findViewById(R.id.txvEventDate)
        val txvEventStartLocation: TextView = itemView.findViewById(R.id.txvEventStartLocation)
        val txvEventEndLocation: TextView = itemView.findViewById(R.id.txvEventEndLocation)
        val imageViewEventStatus: ImageView = itemView.findViewById(R.id.imageViewEventStatus)
        val imageViewManage: ImageView = itemView.findViewById(R.id.imageViewManage)
    }
}