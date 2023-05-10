package ru.spb.rollers.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.view.menu.MenuBuilder
import androidx.appcompat.view.menu.MenuPopupHelper
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.google.android.material.textview.MaterialTextView
import ru.spb.rollers.MAIN
import ru.spb.rollers.R
import ru.spb.rollers.model.Dialog

class DialogAdapter(
    private var itemListDialog: List<Dialog>,
): RecyclerView.Adapter<DialogAdapter.DialogViewHolder>() {

    override fun onCreateViewHolder (parent: ViewGroup, viewType: Int): DialogViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_list_messages, parent, false)
        return DialogViewHolder(view)
    }

    override fun onBindViewHolder(holder: DialogViewHolder, position: Int) {
        val item = itemListDialog[position]
        holder.dialogContainer
        holder.mtvName.text = item.dialogName
        holder.mtvMessage.text = item.dialogMessage
        holder.mtvDate.text = item.dialogDate
        holder.ivPhoto.setImageResource(R.drawable.logo)
    }

    override fun getItemCount(): Int {
        return itemListDialog.size
    }

    inner class DialogViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnLongClickListener, View.OnClickListener{
        val dialogContainer: MaterialCardView = itemView.findViewById(R.id.dialogContainer)
        val mtvName: MaterialTextView = itemView.findViewById(R.id.mtvName)
        val mtvMessage: MaterialTextView = itemView.findViewById(R.id.mtvMessage)
        val mtvDate: MaterialTextView = itemView.findViewById(R.id.mtvDate)
        val ivPhoto: ImageView = itemView.findViewById(R.id.imageViewPhoto)

        init {
            itemView.setOnClickListener(this)
            itemView.setOnLongClickListener(this)
        }

        override fun onLongClick(p0: View): Boolean {
            showPopupMenu(p0)
            return true
        }

        override fun onClick(p0: View) {
            MAIN.navController.navigate(R.id.action_homePage_to_messages)
        }

        @SuppressLint("RestrictedApi")
        private fun showPopupMenu(view: View) {
            val popupMenu = PopupMenu(view.context, view)
            popupMenu.inflate(R.menu.dialog_popup_menu)
            popupMenu.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.pinDialog -> {
                        // Обработка выбора пункта меню 1
                        true
                    }
                    R.id.deleteMessages -> {
                        // Обработка выбора пункта меню 2
                        true
                    }
                    R.id.deleteDialog -> {
                        // Обработка выбора пункта меню 2
                        true
                    }
                    // Добавьте обработку других пунктов меню по мере необходимости
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
