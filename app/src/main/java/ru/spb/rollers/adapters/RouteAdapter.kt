package ru.spb.rollers.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
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
import ru.spb.rollers.*
import ru.spb.rollers.models.Route


class RouteAdapter(
    private var itemListRoute: MutableList<Route>,
): RecyclerView.Adapter<RouteAdapter.RouteViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RouteAdapter.RouteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_route, parent, false)
        return RouteViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RouteAdapter.RouteViewHolder, position: Int) {
        val item = itemListRoute[position]

        val refMessages = REF_DATABASE_ROUTE
            .child(MAIN.appViewModel.user.id)
            .child(item.id!!)
            .child("Points")
        refMessages.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists())
                {
                    val points = snapshot.children.map { it.getPointModel() }
                    val firstPoint = points.first()
                    val lastPoint = points.last()
                    holder.tvRouteStartLocation.text = firstPoint.displayName
                    holder.tvRouteEndLocation.text = lastPoint.displayName
                }
            }
            override fun onCancelled(error: DatabaseError) {}
        })

        holder.tvRouteName.text = item.name
        holder.tvRouteDistance.text = "${item.distance} км"

        holder.routesContainer.setOnClickListener{
            showPopupMenu(it, item)
        }
    }

    override fun getItemCount(): Int {
        return itemListRoute.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setList(list: MutableList<Route>) {
        itemListRoute = list
        notifyDataSetChanged()
    }

    @SuppressLint("RestrictedApi", "MissingInflatedId", "NotifyDataSetChanged")
    private fun showPopupMenu(view: View, route: Route) {
        val popupMenu = PopupMenu(view.context, view)
        popupMenu.inflate(R.menu.route_popup_menu)
        popupMenu.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.toPinRoute -> {
                    Toast.makeText(MAIN, "Маршрут закреплен", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.toViewRoute -> {
                    true
                }
                R.id.toChangeRouteName -> {
                    val li = LayoutInflater.from(MAIN)
                    val promptsView: View = li.inflate(R.layout.alert_dialog_change_name, null)
                    val builderChangeRouteNameDialog: AlertDialog.Builder = AlertDialog.Builder(MAIN)
                    builderChangeRouteNameDialog.setView(promptsView)
                    val userInput: EditText = promptsView.findViewById(R.id.input_text)
                    builderChangeRouteNameDialog
                        .setTitle("Введите новое имя маршрута")
                        .setCancelable(false)
                        .setPositiveButton("Да") { _, _ ->
                            route.name = userInput.text.toString()
                            REF_DATABASE_ROUTE.child(MAIN.appViewModel.user.id)
                                .child(route.id!!).child("name")
                                .setValue(route.name)
                            Toast.makeText(MAIN, "Название изменено", Toast.LENGTH_SHORT).show()
                        }
                        .setNegativeButton("Отмена"){dialog, _ ->
                            dialog.cancel()
                        }

                    val alertDialogChangeRouteName: AlertDialog = builderChangeRouteNameDialog.create()
                    alertDialogChangeRouteName.show()
                    true
                }
                R.id.deleteRoute -> {
                    val builderDeleteDialog: AlertDialog.Builder = AlertDialog.Builder(MAIN)
                    builderDeleteDialog
                        .setTitle("Удаление маршрута")
                        .setMessage("Вы уверены, что хотите удалить маршрут? Отменить это действие будет нельзя")
                        .setCancelable(false)
                        .setPositiveButton("Да") { _, _ ->
                            val path = REF_DATABASE_ROUTE
                                .child(MAIN.appViewModel.user.id)
                                .child(route.id!!)
                            path.removeValue().addOnSuccessListener {
                                Toast.makeText(MAIN, "Удалено", Toast.LENGTH_SHORT).show()
                            }.addOnFailureListener {}
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

    inner class RouteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val routesContainer: MaterialCardView = itemView.findViewById(R.id.routesContainer)
        val tvRouteName: TextView = itemView.findViewById(R.id.tvRouteName)
        val tvRouteDistance: TextView = itemView.findViewById(R.id.tvRouteDistance)
        val tvRouteStartLocation: TextView = itemView.findViewById(R.id.tvRouteStartLocation)
        val tvRouteEndLocation: TextView = itemView.findViewById(R.id.tvRouteEndLocation)

    }
}