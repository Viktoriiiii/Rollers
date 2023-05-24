package ru.spb.rollers.adapters

import android.annotation.SuppressLint
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
        holder.txvEventCost.text = "Стоимость: " + if (item.eventCost == 0.0) "Бесплатно" else item.eventCost.toString()

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
            showPopupMenu(it, item)
        }
    }

    @SuppressLint("RestrictedApi")
    private fun showPopupMenu(view: View, event:Event) {
        val popupMenu = PopupMenu(view.context, view)

        if (userID == event.eventManager)
            popupMenu.inflate(R.menu.event_pm_for_manager)
        else if (event.isParticipate){
            popupMenu.inflate(R.menu.event_pm_for_participant)
        }
        else
            popupMenu.inflate(R.menu.event_pm_for_everybody)
        popupMenu.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.toViewEvent -> {
                    MAIN.navController.navigate(R.id.action_events_to_eventsViewFragment)
                    true
                }
                R.id.toAddEvent -> {
                    Toast.makeText(MAIN, "Мероприятие добавлено", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.toChangeEvent ->{
                    MAIN.navController.navigate(R.id.action_events_to_eventsCreateFragment)
                    true
                }
                R.id.deleteEvent -> {
                    val builderDeleteDialog: AlertDialog.Builder = AlertDialog.Builder(MAIN)
                    builderDeleteDialog
                        .setTitle("Вы уверены, что хотите удалить мероприятие?")
                        .setCancelable(false)
                        .setPositiveButton("Да") { _, _ ->
                            Toast.makeText(MAIN, "Мероприятие удалено", Toast.LENGTH_SHORT).show()
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

    override fun getItemCount(): Int {
        return itemListEvent.size
    }

    inner class EventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val eventsContainer: MaterialCardView = itemView.findViewById(R.id.eventsContainer)
        val textViewEventName: TextView = itemView.findViewById(R.id.textViewEventName)
        val txvEventDate: MaterialTextView = itemView.findViewById(R.id.txvEventDate)
        val txvEventStartLocation: TextView = itemView.findViewById(R.id.txvEventStartLocation)
        val txvEventEndLocation: TextView = itemView.findViewById(R.id.txvEventEndLocation)
        val txvEventCost: TextView = itemView.findViewById(R.id.txvEventCost)
        val imageViewEventStatus: ImageView = itemView.findViewById(R.id.imageViewEventStatus)
        val imageViewManage: ImageView = itemView.findViewById(R.id.imageViewManage)
    }
}