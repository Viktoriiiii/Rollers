package ru.spb.rollers.adapters

import android.annotation.SuppressLint
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import ru.spb.rollers.R
import ru.spb.rollers.model.Waypoint

class PointAdapter (private var itemListPoint: List<Waypoint>
): RecyclerView.Adapter<PointAdapter.PointViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PointViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_point, parent, false)
        return PointViewHolder(view)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: PointViewHolder, position: Int) {
        val item = itemListPoint[position]
        holder.pointsContainer
        holder.imageViewLocation.setImageResource(R.drawable.ic_location_foreground)
        holder.imageViewDelete.setImageResource(R.drawable.ic_close_foreground)

        holder.imageViewDelete.setOnClickListener{
            if (position == 0 || position == 1){
                holder.editTextLocation.text.clear()
                item.waypointName = null
            }
            if (itemListPoint.size > 2 )
                deletePoint(itemListPoint[position])
        }

        if (position == 0)
            holder.editTextLocation.hint = "Откуда"
        if (position > 0)
            holder.editTextLocation.hint = "Куда"

        if (!item.waypointName.isNullOrEmpty())
            holder.editTextLocation.text = Editable.Factory.getInstance().newEditable(item.waypointName)
    }

    override fun getItemCount(): Int {
        return itemListPoint.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addPoint(waypoint: Waypoint) {
        itemListPoint += waypoint
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun deletePoint(waypoint: Waypoint){
        itemListPoint -= waypoint
        notifyDataSetChanged()
    }

    inner class PointViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val pointsContainer: MaterialCardView = itemView.findViewById(R.id.pointsContainer)
        val imageViewLocation: ImageView = itemView.findViewById(R.id.imageViewLocation)
        val editTextLocation: EditText = itemView.findViewById(R.id.editTextLocation)
        val imageViewDelete: ImageView = itemView.findViewById(R.id.imageViewDelete)
    }
}