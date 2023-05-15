package ru.spb.rollers.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.yandex.mapkit.search.SuggestItem
import ru.spb.rollers.R

interface OnItemClickListener {
    fun onItemClick(item: SuggestItem)
}

class SearchAdapter(
    private var itemListSuggest: List<SuggestItem>,
): RecyclerView.Adapter<SearchAdapter.SearchViewHolder>() {

    private var onItemClickListener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        onItemClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_search, parent, false)
        return SearchViewHolder(view)
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        val item = itemListSuggest[position]
        holder.searchContainer
        holder.txvSearchTitle.text = item.title.text
        holder.txvSearchText.text = item.displayText


        holder.searchContainer.setOnClickListener {
            onItemClickListener?.onItemClick(item)
        }
    }

    override fun getItemCount(): Int {
        return itemListSuggest.size
    }

    inner class SearchViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val searchContainer: MaterialCardView = itemView.findViewById(R.id.searchContainer)
        val txvSearchTitle: TextView = itemView.findViewById(R.id.txvSearchTitle)
        val txvSearchText: TextView = itemView.findViewById(R.id.txvSearchText)
    }
}