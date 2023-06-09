package ru.spb.rollers.ui.messages

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.ViewModelProvider
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
import ru.spb.rollers.models.User
import java.util.*

class MessagesFragment : Fragment() {

    private var _binding: MessagesFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: MessagesViewModel

//    private var messageList: List<Message> = mutableListOf()
//    private lateinit var recyclerView: RecyclerView
//    private lateinit var messageAdapter: MessageAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this)[MessagesViewModel::class.java]

        println(object : Any() {}.javaClass.enclosingMethod?.name+ "Fragment")
        println(MAIN.appViewModel.liveData.value)

        _binding = MessagesFragmentBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        println(object : Any() {}.javaClass.enclosingMethod?.name+ "Fragment")
        println(MAIN.appViewModel.liveData.value)

        MAIN.setBottomNavigationVisible(false)

        binding.imageViewBack.setOnClickListener{
            MAIN.onSupportNavigateUp()
            MAIN.setBottomNavigationVisible(true)
        }

        // Загрузка инфо получателя в toolbar
        REF_DATABASE_USER.child(MAIN.appViewModel.contactForMessages.id).addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue<User>()!!
                MAIN.appViewModel.contactForMessages = user
                Glide.with(MAIN)
                    .load(user.photo)
                    .placeholder(R.drawable.avatar)
                    .into(binding.ivPhoto)

                if (user.role == "Организатор") {
                    binding.txvName.text = if (user.schoolName.isNullOrEmpty()) "Неизвестный организатор"
                        else user.schoolName
                }
                else {
                    binding.txvName.text = if (user.lastName.isNullOrEmpty() &&
                        user.firstName.isNullOrEmpty()) "Неизвестный пользователь" else user.lastName + " " + user.firstName
                }
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })

//        setInitialData()
//        recyclerView = view.findViewById(R.id.contactsList)
//        messageAdapter = MessageAdapter(messageList, 2)
//        recyclerView.adapter = messageAdapter

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

        val mapMessageC = hashMapOf<String, Any>()
        mapMessageC["from"] = curUser
        mapMessageC["text"] = binding.etMessage.text.toString()
        mapMessageC["timeStamp"] = ServerValue.TIMESTAMP
        mapMessageC["read"] = false

        val mapDialog = hashMapOf<String,Any>()

        mapDialog["$refDialogUser/id"] = contUser
        mapDialog["$refDialogUser/pinned"] = false
        mapDialog["$refDialogUser/$messageKey"] = mapMessageI

        mapDialog["$refDialogReceivingUser/id"] = curUser
        mapDialog["$refDialogReceivingUser/pinned"] = false
        mapDialog["$refDialogReceivingUser/$messageKey"] = mapMessageC

        REF_DATABASE_ROOT.updateChildren(mapDialog)
            .addOnSuccessListener {  }
            .addOnFailureListener {  }

        binding.etMessage.text.clear()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        println(object : Any() {}.javaClass.enclosingMethod?.name+ "Fragment")
        println(MAIN.appViewModel.liveData.value)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        println(object : Any() {}.javaClass.enclosingMethod?.name+ "Fragment")
        println(MAIN.appViewModel.liveData.value)
    }

    override fun onStart() {
        super.onStart()
        println(object : Any() {}.javaClass.enclosingMethod?.name+ "Fragment")
        println(MAIN.appViewModel.liveData.value)
    }

    override fun onStop() {
        super.onStop()
        println(object : Any() {}.javaClass.enclosingMethod?.name+ "Fragment")
        println(MAIN.appViewModel.liveData.value)
    }

    override fun onPause() {
        super.onPause()
        println(object : Any() {}.javaClass.enclosingMethod?.name+ "Fragment")
        println(MAIN.appViewModel.liveData.value)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        println(object : Any() {}.javaClass.enclosingMethod?.name+ "Fragment")
        println(MAIN.appViewModel.liveData.value)
    }

    override fun onDetach() {
        super.onDetach()
        println(object : Any() {}.javaClass.enclosingMethod?.name+ "Fragment")
        println(MAIN.appViewModel.liveData.value)
    }

    override fun onDestroy() {
        super.onDestroy()
        println(object : Any() {}.javaClass.enclosingMethod?.name+ "Fragment")
        println(MAIN.appViewModel.liveData.value)
    }

    override fun onResume() {
        super.onResume()
        println(object : Any() {}.javaClass.enclosingMethod?.name + "Fragment")
        println(MAIN.appViewModel.liveData.value)
        MAIN.setBottomNavigationVisible(false)
    }
}