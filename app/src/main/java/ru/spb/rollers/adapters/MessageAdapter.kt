package ru.spb.rollers.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import ru.spb.rollers.MAIN
import ru.spb.rollers.R
import ru.spb.rollers.asTime
import ru.spb.rollers.models.Message

class MessageAdapter (private var itemListMessage: List<Message>):
RecyclerView.Adapter<MessageAdapter.MessageViewHolder>(){

    private val self: Int = 786

    override fun getItemViewType(position: Int): Int {
        val message: Message = itemListMessage[position]
        if (message.from == MAIN.appViewModel.user.id) {
            return self
        }
        return position
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val itemView: View = if (viewType == self)
            LayoutInflater.from(parent.context).inflate(R.layout.chat_my_message, parent, false)
        else
            LayoutInflater.from(parent.context).inflate(R.layout.chat_others_message, parent, false)
        return MessageViewHolder(itemView)    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val item = itemListMessage[position]
        holder.messageContainer

        if (item.from == MAIN.appViewModel.user.id)
            holder.textViewSender.text = "Я"
        else {
            val contact = MAIN.appViewModel.contactForMessages
            if (!contact.lastName.isNullOrEmpty() || !contact.firstName.isNullOrEmpty() ||
                    !contact.schoolName.isNullOrEmpty())
                holder.textViewSender.text = "${contact.schoolName}${contact.lastName} ${contact.firstName}"
            else
                holder.textViewSender.text = "Неизвестный пользователь"
        }

        holder.textViewMessage.text = item.text
        holder.textViewDate.text = item.timeStamp.toString().asTime()
    }

    override fun getItemCount(): Int {
        return itemListMessage.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setList(list: List<Message>) {
        itemListMessage = list
        notifyDataSetChanged()
    }

    inner class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val messageContainer: CardView = itemView.findViewById(R.id.messagesContainer)
        val textViewSender: TextView = itemView.findViewById(R.id.textViewSender)
        val textViewMessage: TextView = itemView.findViewById(R.id.textViewMessage)
        val textViewDate: TextView = itemView.findViewById(R.id.textViewDate)
    }
}