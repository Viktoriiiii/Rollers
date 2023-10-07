package ru.spb.rollers.ui

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ServerValue
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import ru.spb.rollers.*
import ru.spb.rollers.databinding.MessagesFragmentBinding
import ru.spb.rollers.models.Message
import ru.spb.rollers.models.User
import ru.spb.rollers.adapters.MessageAdapter
import java.util.*

class MessagesFragment : Fragment() {

    private var  _binding: MessagesFragmentBinding? = null
    private val binding get() = _binding!!

    private var messageList = emptyList<Message>()
    private lateinit var messageAdapter: MessageAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MessagesFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.ivBack.setOnClickListener{
            MAIN.onSupportNavigateUp()

            // установить viewedDialog в "non"
            // записать в поле бд viewedDialog какой чат открывается
            REF_DATABASE_USER.child(MAIN.appViewModel.user.id).child("viewedDialog")
                .setValue("non")
        }

        binding.btnSend.setOnClickListener {
            if (binding.etMessage.text.toString().isNotEmpty()){
                sendMessage()
            }
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun sendMessage(){
        val curUser = MAIN.appViewModel.user.id
        val contUser = MAIN.appViewModel.contactForMessages.id

        val refDialogUser = "Dialog/$curUser/$contUser"
        val refDialogReceivingUser = "Dialog/$contUser/$curUser"
        val messageKey = REF_DATABASE_ROOT.child(refDialogUser).push().key

        val mapMessageI = hashMapOf<String, Any>()
        mapMessageI["from"] = curUser
        mapMessageI["text"] = binding.etMessage.text.toString()
        mapMessageI["timeStamp"] = ServerValue.TIMESTAMP
        mapMessageI["read"] = true
        mapMessageI["id"] = messageKey.toString()

        val mapMessageC = hashMapOf<String, Any>()
        mapMessageC["from"] = curUser
        mapMessageC["text"] = binding.etMessage.text.toString()
        mapMessageC["timeStamp"] = ServerValue.TIMESTAMP
        mapMessageC["id"] = messageKey.toString()

        val mapDialog = hashMapOf<String,Any>()
        mapDialog["$refDialogUser/id"] = contUser
        mapDialog["$refDialogUser/pinned"] = false
        mapDialog["$refDialogUser/Messages/$messageKey"] = mapMessageI

        // получить просмотриваемый пользователем диалог
        REF_DATABASE_USER.child(contUser).child("viewedDialog")
            .addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val id = snapshot.getValue<String>()!!
                    mapMessageC["read"] = id == MAIN.appViewModel.user.id
                    mapDialog["$refDialogReceivingUser/Messages/$messageKey"] = mapMessageC
                    REF_DATABASE_ROOT.updateChildren(mapDialog)
                        .addOnSuccessListener {  }
                        .addOnFailureListener {  }
                }
                override fun onCancelled(error: DatabaseError) {
                }
            })

        mapDialog["$refDialogReceivingUser/id"] = curUser
        mapDialog["$refDialogReceivingUser/pinned"] = false
        mapDialog["$refDialogReceivingUser/Messages/$messageKey"] = mapMessageC

        REF_DATABASE_ROOT.updateChildren(mapDialog)
            .addOnSuccessListener {  }
            .addOnFailureListener {  }

        binding.etMessage.text.clear()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onStop() {
        super.onStop()
        REF_DATABASE_USER.child(MAIN.appViewModel.user.id).child("viewedDialog")
            .setValue("non")
    }

    override fun onResume() {
        super.onResume()
        initRecyclerView()

        // Загрузка инфо получателя в toolbar
        REF_DATABASE_USER.child(MAIN.appViewModel.contactForMessages.id).addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue<User>()!!

                if (user.id.isNotEmpty()) {
                        MAIN.appViewModel.contactForMessages = user

                try {
                    Glide.with(MAIN)
                        .load(user.photo)
                        .placeholder(R.drawable.avatar)
                        .into(binding.ivPhoto)
                    if (user.role == "Организатор") {
                        binding.tvName.text = if (user.schoolName.isNullOrEmpty()) "Неизвестный организатор"
                        else user.schoolName
                    }
                    else {
                        binding.tvName.text = if (user.lastName.isNullOrEmpty() &&
                            user.firstName.isNullOrEmpty()) "Неизвестный пользователь" else user.lastName + " " + user.firstName
                    }
                }
                catch (_: Exception){ }
                }
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    private fun initRecyclerView() {

        // прочитать все непрочитанные сообщения в диалоге

        // Получение ссылки на диалог
        val dialogRef = REF_DATABASE_DIALOG.child(MAIN.appViewModel.user.id)
            .child(MAIN.appViewModel.contactForMessages.id)
        // Обновление поля "read" во всех сообщениях диалога
        dialogRef.child("Messages").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (messageSnapshot in dataSnapshot.children) {
                    val messageKey = messageSnapshot.key
                    val messageRef = dialogRef.child("Messages").child(messageKey!!)
                    messageRef.child("read").setValue(true)
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {}
        })

        // записать в поле бд viewedDialog какой чат открывается
        REF_DATABASE_USER.child(MAIN.appViewModel.user.id).child("viewedDialog")
            .setValue(MAIN.appViewModel.contactForMessages.id)

        messageAdapter = MessageAdapter(messageList)

        val ref = REF_DATABASE_DIALOG
            .child(MAIN.appViewModel.user.id)
            .child(MAIN.appViewModel.contactForMessages.id)
            .child("Messages")

        binding.contactList.adapter = messageAdapter

        ref.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                messageList = snapshot.children.map { it.getMessageModel() }
                messageAdapter.setList(messageList)
                try {
                    binding.contactList.smoothScrollToPosition(messageAdapter.itemCount)
                }
                catch (_:Exception){}
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
    }
}

