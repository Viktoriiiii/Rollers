package ru.spb.rollers.screens

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.spb.rollers.MAIN
import ru.spb.rollers.R
import ru.spb.rollers.adapters.EventAdapter
import ru.spb.rollers.databinding.FragmentEventSearchBinding
import ru.spb.rollers.model.Event

class EventSearch : Fragment() {


    private lateinit var binding: FragmentEventSearchBinding
    private var eventList: List<Event> = mutableListOf()
    private lateinit var recyclerView: RecyclerView
    private lateinit var eventAdapter: EventAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEventSearchBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.imageButtonBack.setOnClickListener{
            MAIN.navController.navigate(R.id.action_eventSearch_to_events2)
        }
        setInitialData()
        recyclerView = view.findViewById(R.id.eventsList)
        eventAdapter = EventAdapter(eventList)
        recyclerView.adapter = eventAdapter
    }

    private fun setInitialData() {
        eventList += Event(
            1, "Покатушка на роликах","Васька",
            "Петроградка", "Петя Иванов", "03.05.2023 13:00",
            "13:00", "13:30", "10 км/ч", "15 км",
            "Описание какое-нибудь", false)
        eventList += Event(
            2, "Покатушка на роликах","Васька",
            "Петроградка", "Петя Иванов", "03.05.2023 13:00",
            "13:00", "13:30", "10 км/ч", "15 км",
            "Описание какое-нибудь", false)
        eventList += Event(
            3, "Покатушка на роликах","Васька",
            "Петроградка", "Петя Иванов", "03.05.2023 13:00",
            "13:00", "13:30", "10 км/ч", "15 км",
            "Описание какое-нибудь", false)
        eventList += Event(
            4, "Покатушка на роликах","Васька",
            "Петроградка", "Петя Иванов", "03.05.2023 13:00",
            "13:00", "13:30", "10 км/ч", "15 км",
            "Описание какое-нибудь", false)
        eventList += Event(
            5, "Покатушка на роликах","Васька",
            "Петроградка", "Петя Иванов", "03.05.2023 13:00",
            "13:00", "13:30", "10 км/ч", "15 км",
            "Описание какое-нибудь", true)
        eventList += Event(
            6, "Покатушка на роликах","Васька",
            "Петроградка", "Петя Иванов", "03.05.2023 13:00",
            "13:00", "13:30", "10 км/ч", "15 км",
            "Описание какое-нибудь", true)
    }
}