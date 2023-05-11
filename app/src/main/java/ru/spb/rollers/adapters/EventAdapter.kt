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

class EventAdapter (private var itemListEvent: List<Event>
): RecyclerView.Adapter<EventAdapter.EventViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_event, parent, false)
        return EventViewHolder(view)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val item = itemListEvent[position]
        holder.eventsContainer
        holder.imageViewRoute.setImageResource(R.drawable.route)
        holder.textViewEventName.text = item.eventName
        holder.txvEventDate.text = item.eventDate
        holder.txvEventStartLocation.text = item.eventStartLocation
        holder.txvEventEndLocation.text = item.eventEndLocation

        if (item.isParticipate) holder.imageViewEventStatus.setImageResource(R.drawable.ic_done_foreground)
        else holder.imageViewEventStatus.setImageResource(R.drawable.ic_add_event_foreground)
    }

    override fun getItemCount(): Int {
        return itemListEvent.size
    }

    inner class EventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener{
        val eventsContainer: MaterialCardView = itemView.findViewById(R.id.eventsContainer)
        val imageViewRoute: ImageView = itemView.findViewById(R.id.imageViewRoute)
        val textViewEventName: TextView = itemView.findViewById(R.id.textViewEventName)
        val txvEventDate: MaterialTextView = itemView.findViewById(R.id.txvEventDate)
        val txvEventStartLocation: TextView = itemView.findViewById(R.id.txvEventStartLocation)
        val txvEventEndLocation: TextView = itemView.findViewById(R.id.txvEventEndLocation)
        val imageViewEventStatus: ImageView = itemView.findViewById(R.id.imageViewEventStatus)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(p0: View) {
            showPopupMenu(p0)
        }

        @SuppressLint("RestrictedApi", "MissingInflatedId")
        private fun showPopupMenu(view: View) {
            val popupMenu = PopupMenu(view.context, view)
            popupMenu.inflate(R.menu.events_popup_menu)
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
            val menuHelper = MenuPopupHelper(view.context,
                popupMenu.menu as MenuBuilder, view)
            menuHelper.setForceShowIcon(true)
            menuHelper.show()
        }
    }
}