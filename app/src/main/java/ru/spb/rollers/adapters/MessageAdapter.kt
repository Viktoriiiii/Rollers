package ru.spb.rollers.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.android.material.card.MaterialCardView
import ru.spb.rollers.MAIN
import ru.spb.rollers.R
import ru.spb.rollers.models.Dialog
import ru.spb.rollers.models.User
import ru.spb.rollers.oldmodel.Message

class MessageAdapter (options: FirebaseRecyclerOptions<Dialog>):
    FirebaseRecyclerAdapter<Dialog, MessageAdapter.MessageViewHolder>(options)
{

    private var self: Int = 786
    private lateinit var model: Dialog

    override fun getItemViewType(position: Int): Int {
        val message = model.message[position]
        if (message.from == MAIN.appViewModel.user.id) {
            return self;
        }
        return position
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val itemView: View = if (viewType == self)
            LayoutInflater.from(parent.context).inflate(R.layout.chat_my_message, parent, false)
        else
            LayoutInflater.from(parent.context).inflate(R.layout.chat_others_message, parent, false)
        return MessageViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int, model: Dialog) {
        this.model = model

//        holder.messageContainer
//        holder.textViewSender.text = item.messageSender
//        holder.textViewMessage.text = item.messageText
//        holder.textViewDate.text = item.messageDate
    }

    inner class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val messageContainer: MaterialCardView = itemView.findViewById(R.id.messagesContainer)
        val textViewSender: TextView = itemView.findViewById(R.id.textViewSender)
        val textViewMessage: TextView = itemView.findViewById(R.id.textViewMessage)
        val textViewDate: TextView = itemView.findViewById(R.id.textViewDate)
    }
}