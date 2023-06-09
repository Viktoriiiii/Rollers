package ru.spb.rollers.oldadapters

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
import ru.spb.rollers.MAIN
import ru.spb.rollers.R
import ru.spb.rollers.oldmodel.Event
import ru.spb.rollers.titleEvents

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
        holder.ivManager.setImageResource(R.drawable.ic_manage_account_foreground)
        holder.txvEventName.text = item.eventName
        holder.txvEventDateDay.text = "29"
        holder.txvEventDateMonth.text = "июня"
        holder.txvEventDateTime.text = "15:30"
        holder.txvEventStartLocation.text = "${item.eventStartLocation}"
        holder.txvEventEndLocation.text = "${item.eventEndLocation}"
        holder.txvEventCost.text = if (item.eventCost == 0.0) "Бесплатно" else item.eventCost.toString() + "р."

        holder.ivEventPhoto.setImageResource(R.drawable.roliki)

        if (item.isParticipate) {
            holder.ivEventStatus.setImageResource(R.drawable.ic_done_foreground)
        }
        else {
            holder.ivEventStatus.setImageResource(R.drawable.ic_add_event_foreground)
        }

        if (userID == item.eventManager) {
            holder.ivManager.setImageResource(R.drawable.ic_manage_account_foreground)
        }
        else {
            holder.ivManager.visibility = View.GONE
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
                    titleEvents = "Изменение события"
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
        val txvEventName: TextView = itemView.findViewById(R.id.txvEventName)
        val txvEventStartLocation: TextView = itemView.findViewById(R.id.txvEventStartLocation)
        val txvEventEndLocation: TextView = itemView.findViewById(R.id.txvEventEndLocation)
        val txvEventCost: TextView = itemView.findViewById(R.id.txvEventCost)
        val txvEventDateDay: TextView = itemView.findViewById(R.id.txvEventDateDay)
        val txvEventDateMonth: TextView = itemView.findViewById(R.id.txvEventDateMonth)
        val txvEventDateTime: TextView = itemView.findViewById(R.id.txvEventDateTime)
        val ivEventStatus: ImageView = itemView.findViewById(R.id.ivEventStatus)
        val ivManager: ImageView = itemView.findViewById(R.id.ivManager)
        val ivEventPhoto: ImageView = itemView.findViewById(R.id.ivEventPhoto)

    }
}