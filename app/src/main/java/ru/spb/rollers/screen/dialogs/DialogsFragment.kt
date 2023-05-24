package ru.spb.rollers.screen.dialogs

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.spb.rollers.R
import ru.spb.rollers.adapters.DialogAdapter
import ru.spb.rollers.databinding.DialogsFragmentBinding
import ru.spb.rollers.model.Dialog

class DialogsFragment : Fragment() {

    private var _binding: DialogsFragmentBinding? = null
    private val binding get() = _binding!!

    private var dialogList: List<Dialog> = mutableListOf()
    private lateinit var recyclerView: RecyclerView
    private lateinit var dialogAdapter: DialogAdapter

    companion object {
        fun newInstance() = DialogsFragment()
    }

    private lateinit var viewModel: DialogsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogsFragmentBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this)[DialogsViewModel::class.java]
        // TODO: Use the ViewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setInitialData()
        recyclerView = view.findViewById(R.id.messages_list)
        dialogAdapter = DialogAdapter(dialogList)
        recyclerView.adapter = dialogAdapter
    }
    private fun setInitialData() {
        dialogList += Dialog(
            1, "Иван Петров",
            "6 марта 2023 21:05", "текстовое сообщение...")
        dialogList += Dialog(
            2, "Иван Петров",
            "6 марта 2023 21:05", "текстовое сообщение...")
        dialogList += Dialog(
            3, "Иван Петров",
            "6 марта 2023 21:05", "текстовое сообщение...")
        dialogList += Dialog(
            4, "Иван Петров",
            "6 марта 2023 21:05", "текстовое сообщение...")
        dialogList += Dialog(
            5, "Иван Петров",
            "6 марта 2023 21:05", "текстовое сообщение...")
        dialogList += Dialog(
            6, "Иван Петров",
            "6 марта 2023 21:05", "текстовое сообщение...")
        dialogList += Dialog(
            7, "Иван Петров",
            "6 марта 2023 21:05", "текстовое сообщение...")
        dialogList += Dialog(
            8, "Иван Петров",
            "6 марта 2023 21:05", "текстовое сообщение...")
        dialogList += Dialog(
            9, "Иван Петров",
            "6 марта 2023 21:05", "текстовое сообщение...")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}