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
import ru.spb.rollers.databinding.FragmentEventsBinding
import ru.spb.rollers.model.Event

class Events : Fragment() {

    private lateinit var binding: FragmentEventsBinding
    private var eventList: List<Event> = mutableListOf()
    private lateinit var recyclerView: RecyclerView
    private lateinit var eventAdapter: EventAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEventsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.imageButtonBack.setOnClickListener{
            MAIN.navController.navigate(R.id.action_events2_to_homePage)
        }

        binding.imageButtonSearchEvents.setOnClickListener{
            MAIN.navController.navigate(R.id.action_events2_to_eventSearch)
        }

        binding.imageButtonAddEvent.setOnClickListener{
            MAIN.navController.navigate(R.id.action_events2_to_eventAdding)
        }

        setInitialData()
        recyclerView = view.findViewById(R.id.eventsList)
        eventAdapter = EventAdapter(eventList, 2)
        recyclerView.adapter = eventAdapter
    }

    private fun setInitialData() {
        eventList += Event(
            1, "Покатушка на роликах","Васька",
            "Петроградка", 1, "03.05.2023 13:00",
            "13:00", "13:30", "10 км/ч", "15 км",
            "Описание какое-нибудь", 0.0, true)
        eventList += Event(
            2, "Покатушка на роликах","Васька",
            "Петроградка", 2, "03.05.2023 13:00",
            "13:00", "13:30", "10 км/ч", "15 км",
            "Описание какое-нибудь", 550.0,true)
        eventList += Event(
            3, "Покатушка на роликах","Васька",
            "Петроградка", 3, "03.05.2023 13:00",
            "13:00", "13:30", "10 км/ч", "15 км",
            "Описание какое-нибудь", 0.0, true)
        eventList += Event(
            4, "Покатушка на роликах","Васька",
            "Петроградка", 4, "03.05.2023 13:00",
            "13:00", "13:30", "10 км/ч", "15 км",
            "Описание какое-нибудь", 1000.0,true)
        eventList += Event(
            5, "Покатушка на роликах","Васька",
            "Петроградка", 5, "03.05.2023 13:00",
            "13:00", "13:30", "10 км/ч", "15 км",
            "Описание какое-нибудь", 0.0,true)
        eventList += Event(
            6, "Покатушка на роликах","Васька",
            "Петроградка", 6, "03.05.2023 13:00",
            "13:00", "13:30", "10 км/ч", "15 км",
            "Описание какое-нибудь", 0.0,true)
    }
}