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
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import ru.spb.rollers.*
import ru.spb.rollers.models.Event
import ru.spb.rollers.models.Point
import java.text.SimpleDateFormat
import java.util.*

class EventAdapter(private var itemListEvent: MutableList<Event>):
    RecyclerView.Adapter<EventAdapter.EventViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_event, parent, false)
        return EventViewHolder(view)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val item = itemListEvent[position]
        holder.eventsContainer
        holder.ivManager.setImageResource(R.drawable.ic_manage_account_foreground)
        holder.txvEventName.text = item.name

        val formatter = SimpleDateFormat("d.MM.yyyy", Locale("RU"))
        val dateFormatter = SimpleDateFormat("MMMM", Locale("ru"))
        val date = formatter.parse(item.date)
        val cal = Calendar.getInstance()
        cal.time = date!!

        holder.txvEventDateDay.text = cal.get(Calendar.DAY_OF_MONTH).toString()
        holder.txvEventDateMonth.text = dateFormatter.format(cal.time)
        holder.txvEventDateTime.text = item.time

        REF_DATABASE_EVENT.child(item.id).child("route").child("Points")
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists())
                    {
                        val points = snapshot.children.map { it.getPointModel() }
                        val firstPoint = points.first()
                        val lastPoint = points.last()
                        holder.txvEventStartLocation.text = firstPoint.displayName
                        holder.txvEventEndLocation.text = lastPoint.displayName
                    }
                }
                override fun onCancelled(error: DatabaseError) {}
            })

        holder.txvEventCost.text = if (item.cost == 0.0) "Бесплатно" else item.cost.toString() + "р."

        if (item.photo != "") {
            Picasso.get()
                .load(item.photo)
                .placeholder(R.drawable.rollers)
                .into(holder.ivEventPhoto)
        }
        else
            holder.ivEventPhoto.setImageResource(R.drawable.rollers)

        REF_DATABASE_EVENT_USER.child(MAIN.appViewModel.user.id).child(item.id).child("id")
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()){
                        holder.ivEventStatus.setImageResource(R.drawable.ic_done_foreground)
                    }
                    else
                        holder.ivEventStatus.setImageResource(R.drawable.ic_add_event_foreground)
                }
                override fun onCancelled(error: DatabaseError) {}
            })

        if (MAIN.appViewModel.user.id == item.managerId)
            holder.ivManager.setImageResource(R.drawable.ic_manage_account_foreground)
        else
            holder.ivManager.setImageResource(R.drawable.ic_person_foreground)

        holder.eventsContainer.setOnClickListener{
            showPopupMenu(it, item)
        }
    }

    @SuppressLint("RestrictedApi", "NotifyDataSetChanged")
    private fun showPopupMenu(view: View, event: Event) {
        val popupMenu = PopupMenu(view.context, view)
        popupMenu.inflate(R.menu.event_pm_common)
        REF_DATABASE_EVENT_USER.child(MAIN.appViewModel.user.id).child(event.id).child("id")
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (MAIN.appViewModel.user.id == event.managerId)
                        popupMenu.inflate(R.menu.event_pm_for_manager)
                    else if (snapshot.exists()){
                        popupMenu.inflate(R.menu.event_pm_for_participant)
                    }
                    else
                        popupMenu.inflate(R.menu.event_pm_for_everybody)
                }
                override fun onCancelled(error: DatabaseError) {}
            })

        popupMenu.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.toViewEvent -> {
                    try {
                        MAIN.navController.navigate(R.id.action_events_to_eventsViewFragment)
                    }catch (ex: Exception){
                        MAIN.navController.navigate(R.id.action_eventsMyFragment_to_eventsViewFragment)
                    }
                    true
                }
                R.id.toAddEvent -> {
                    REF_DATABASE_EVENT_USER.child(MAIN.appViewModel.user.id).child(event.id)
                        .child("id").setValue(event.id)
                    REF_DATABASE_EVENT_PARTICIPANT.child(event.id).child(MAIN.appViewModel.user.id)
                        .child("id").setValue(MAIN.appViewModel.user.id)
                    Toast.makeText(MAIN, "Мероприятие добавлено", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.toChangeEvent ->{
                    MAIN.appViewModel.event = event

                    REF_DATABASE_EVENT.child(event.id).child("route")
                        .addValueEventListener(object : ValueEventListener{
                            override fun onDataChange(snapshot: DataSnapshot) {
                                if (snapshot.exists())
                                {
                                    val route = snapshot.getRouteModel()
                                    MAIN.appViewModel.route = route
                                }
                            }
                            override fun onCancelled(error: DatabaseError) {}
                        })

                    REF_DATABASE_EVENT.child(event.id).child("route").child("Points")
                        .addValueEventListener(object : ValueEventListener{
                            override fun onDataChange(snapshot: DataSnapshot) {
                                if (snapshot.exists())
                                {
                                    val points = snapshot.children.map { it.getPointModel() }
                                    MAIN.appViewModel.points = points as MutableList<Point>
                                }
                            }
                            override fun onCancelled(error: DatabaseError) {}
                        })

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
                            REF_DATABASE_EVENT_USER.child(MAIN.appViewModel.user.id).child(event.id)
                                .child("id").removeValue()
                            REF_DATABASE_EVENT_PARTICIPANT.child(event.id).child(MAIN.appViewModel.user.id)
                                .child("id").removeValue()
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

    @SuppressLint("NotifyDataSetChanged")
    fun setList(list: MutableList<Event>) {
        itemListEvent = list
        notifyDataSetChanged()
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