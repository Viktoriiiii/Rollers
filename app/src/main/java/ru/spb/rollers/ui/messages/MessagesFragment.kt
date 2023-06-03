package ru.spb.rollers.ui.messages

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import ru.spb.rollers.MAIN
import ru.spb.rollers.R
import ru.spb.rollers.adapters.MessageAdapter
import ru.spb.rollers.databinding.MessagesFragmentBinding
import ru.spb.rollers.model.Message

class MessagesFragment : Fragment() {

    companion object {
        fun newInstance() = MessagesFragment()
    }

    private var _binding: MessagesFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: MessagesViewModel

    private var messageList: List<Message> = mutableListOf()
    private lateinit var recyclerView: RecyclerView
    private lateinit var messageAdapter: MessageAdapter

    lateinit var materialButtonSend: MaterialButton
    lateinit var editTextSendMessage: EditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        println(object : Any() {}.javaClass.enclosingMethod?.name+ "Fragment")
        println(MAIN.appViewModel.liveData.value)

        _binding = MessagesFragmentBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this)[MessagesViewModel::class.java]
        // TODO: Use the ViewModel
        println(object : Any() {}.javaClass.enclosingMethod?.name+ "Fragment")
        println(MAIN.appViewModel.liveData.value)
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

        setInitialData()
        recyclerView = view.findViewById(R.id.contactsList)
        messageAdapter = MessageAdapter(messageList, 2)
        recyclerView.adapter = messageAdapter

        materialButtonSend = view.findViewById(R.id.buttonSend)
        editTextSendMessage = view.findViewById(R.id.editTextMessage)
        materialButtonSend.setOnClickListener {
            if (!editTextSendMessage.text.toString().isNullOrEmpty()){
                var newMessage: Message = Message(10, "29.04.2023 22:58",
                    "Просто сообщение", "Я", 2)
                newMessage.messageText = editTextSendMessage.text.toString()
                messageList += newMessage
                recyclerView = view.findViewById(R.id.contactsList)
                messageAdapter = MessageAdapter(messageList, 2)
                recyclerView.adapter = messageAdapter
                editTextSendMessage.text.clear()
            }
        }
    }

    private fun setInitialData() {
        messageList += Message(
            1, "29.04.2023 20.50",
            "Сообщение очень длинное, чтобы проверить как работает",
            "Иван Иванов", 1)
        messageList += Message(
            2, "29.04.2023 20.51",
            "Сообщение очень длинное, чтобы проверить как работает",
            "Иван Иванов", 1)
        messageList += Message(
            3, "29.04.2023 20.52",
            "Сообщение очень длинное, чтобы проверить как работает",
            "Иван Иванов", 1)
        messageList += Message(
            4, "29.04.2023 20.53",
            "Сообщение очень длинное, чтобы проверить как работает",
            "Иван Иванов", 1)
        messageList += Message(
            5, "29.04.2023 20.54",
            "Сообщение очень длинное, чтобы проверить как работает",
            "Иван Иванов", 1)
        messageList += Message(
            6, "29.04.2023 20.55",
            "Сообщение очень длинное, чтобы проверить как работает",
            "Иван Иванов", 1)
        messageList += Message(
            7, "29.04.2023 20.56",
            "Короткое сообщение", "Я", 2)
        messageList += Message(
            8, "29.04.2023 20.57",
            "Короткое сообщение", "Я", 2)
        messageList += Message(
            9, "29.04.2023 20.58",
            "Короткое сообщение", "Я", 2)
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