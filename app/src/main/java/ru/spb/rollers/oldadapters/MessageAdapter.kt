package ru.spb.rollers.oldadapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import ru.spb.rollers.R
import ru.spb.rollers.oldmodel.Message

class MessageAdapter (private var itemListMessage: List<Message>, var userID: Int):
RecyclerView.Adapter<MessageAdapter.MessageViewHolder>(){

    private val self: Int = 786

    override fun getItemViewType(position: Int): Int {
        val message: Message = itemListMessage[position];
        if (message.userID == userID) {
            return self;
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
        holder.textViewSender.text = item.messageSender
        holder.textViewMessage.text = item.messageText
        holder.textViewDate.text = item.messageDate
    }

    override fun getItemCount(): Int {
        return itemListMessage.size
    }

    inner class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val messageContainer: MaterialCardView = itemView.findViewById(R.id.messagesContainer)
        val textViewSender: TextView = itemView.findViewById(R.id.textViewSender)
        val textViewMessage: TextView = itemView.findViewById(R.id.textViewMessage)
        val textViewDate: TextView = itemView.findViewById(R.id.textViewDate)
    }
}