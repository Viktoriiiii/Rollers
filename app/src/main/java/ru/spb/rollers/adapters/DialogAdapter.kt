package ru.spb.rollers.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
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
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_dialog, parent, false)
        return DialogViewHolder(view)
    }

    override fun onBindViewHolder(holder: DialogViewHolder, position: Int) {
        val item = itemListDialog[position]
        holder.dialogContainer
        holder.mtvName.text = item.dialogName
        holder.mtvMessage.text = item.dialogMessage
        holder.mtvDate.text = item.dialogDate
        holder.ivPhoto.setImageResource(R.drawable.logo)

        holder.txvContMessage.text = "1"

        holder.dialogContainer.setOnClickListener{
            MAIN.navController.navigate(R.id.action_dialogs_to_messagesFragment)
        }

        holder.dialogContainer.setOnLongClickListener{
            showPopupMenu(it)
            true
        }
    }

    @SuppressLint("RestrictedApi")
    private fun showPopupMenu(view: View) {
        val popupMenu = PopupMenu(view.context, view)
        popupMenu.inflate(R.menu.dialog_popup_menu)
        popupMenu.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.pinDialog -> {
                    Toast.makeText(MAIN, "Диалог закреплен", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.deleteMessages -> {
                    Toast.makeText(MAIN, "Сообщения удалены", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.deleteDialog -> {
                    Toast.makeText(MAIN, "Диалог удален", Toast.LENGTH_SHORT).show()
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
        return itemListDialog.size
    }

    inner class DialogViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val dialogContainer: MaterialCardView = itemView.findViewById(R.id.dialogContainer)
        val mtvName: MaterialTextView = itemView.findViewById(R.id.mtvName)
        val mtvMessage: MaterialTextView = itemView.findViewById(R.id.mtvMessage)
        val mtvDate: MaterialTextView = itemView.findViewById(R.id.mtvDate)
        val txvContMessage: MaterialTextView = itemView.findViewById(R.id.txvContMessage)
        val ivPhoto: ImageView = itemView.findViewById(R.id.ivPhoto)

    }
}
