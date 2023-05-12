package ru.spb.rollers.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import ru.spb.rollers.model.Route

class RouteAdapter (private var itemListRoute: List<Route>
): RecyclerView.Adapter<RouteAdapter.RouteViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RouteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_route, parent, false)
        return RouteViewHolder(view)
    }

    override fun onBindViewHolder(holder: RouteViewHolder, position: Int) {
        val item = itemListRoute[position]
        holder.routesContainer
        holder.textViewRouteEventName.text = item.routeName
        holder.textViewRouteDistance.text = item.routeDistance
        holder.txvEventStartLocation.text = item.routeStartLocation
        holder.txvEventEndLocation.text = item.routeEndLocation
    }

    override fun getItemCount(): Int {
        return itemListRoute.size
    }

    inner class RouteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener{
        val routesContainer: MaterialCardView = itemView.findViewById(R.id.routesContainer)
        val textViewRouteEventName: TextView = itemView.findViewById(R.id.textViewRouteEventName)
        val textViewRouteDistance: MaterialTextView = itemView.findViewById(R.id.textViewRouteDistance)
        val txvEventStartLocation: TextView = itemView.findViewById(R.id.txvEventStartLocation)
        val txvEventEndLocation: TextView = itemView.findViewById(R.id.txvEventEndLocation)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(p0: View) {
            showPopupMenu(p0)
        }

        @SuppressLint("RestrictedApi", "MissingInflatedId")
        private fun showPopupMenu(view: View) {
            val popupMenu = PopupMenu(view.context, view)
            popupMenu.inflate(R.menu.route_popup_menu)
            popupMenu.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.toPinRoute -> {
                        Toast.makeText(MAIN, "Маршрут закреплен", Toast.LENGTH_SHORT).show()
                        true
                    }
                    R.id.toViewRoute -> {
                        MAIN.navController.navigate(R.id.action_routes2_to_buildRoutes)
                        true
                    }
                    R.id.toChangeRouteName -> {
                        Toast.makeText(MAIN, "Название изменено", Toast.LENGTH_SHORT).show()
                        true
                    }
                    R.id.deleteRoute -> {
                        val builderDeleteDialog: AlertDialog.Builder = AlertDialog.Builder(MAIN)
                        builderDeleteDialog
                            .setTitle("Вы уверены, что хотите удалить маршрут?")
                            .setCancelable(false)
                            .setPositiveButton("Да") { _, _ ->
                                Toast.makeText(MAIN, "Машрут удален", Toast.LENGTH_SHORT).show()
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