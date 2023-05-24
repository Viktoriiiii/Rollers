package ru.spb.rollers.screen.events

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.SwitchCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationView
import ru.spb.rollers.MAIN
import ru.spb.rollers.R
import ru.spb.rollers.adapters.EventAdapter
import ru.spb.rollers.databinding.EventsFragmentBinding
import ru.spb.rollers.model.Event

class EventsFragment : Fragment(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: EventsFragmentBinding
    private var eventList: List<Event> = mutableListOf()
    private lateinit var recyclerView: RecyclerView
    private lateinit var eventAdapter: EventAdapter

    companion object {
        fun newInstance() = EventsFragment()
    }

    private lateinit var viewModel: EventsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        MAIN.setBottomNavigationVisible(true)
        binding = EventsFragmentBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this)[EventsViewModel::class.java]
        // TODO: Use the ViewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val toolbar = binding.toolbar
        MAIN.setSupportActionBar(toolbar)
        val drawerLayout = binding.drawerLayout
        val toggle = ActionBarDrawerToggle(activity, drawerLayout, toolbar, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        binding.navigationView.setNavigationItemSelectedListener(this)

        setInitialData()
        recyclerView = view.findViewById(R.id.eventsList)
        eventAdapter = EventAdapter(eventList, 2)
        recyclerView.adapter = eventAdapter

        val header: View = binding.navigationView.getHeaderView(0)
        val switchStatus: SwitchCompat = header.findViewById(R.id.switchStatus)
        switchStatus.setOnCheckedChangeListener {  buttonView, isChecked ->
            buttonView.text = if (isChecked) "На роликах" else "Не активен"
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.exit -> {
                MAIN.navController.navigate(R.id.action_homePage_to_authorization)
            }
            R.id.profile -> {
                MAIN.navController.navigate(R.id.action_homePage_to_profile2)
            }
            R.id.contacts ->
                MAIN.navController.navigate(R.id.action_homePage_to_contacts)
            R.id.events ->
                MAIN.navController.navigate(R.id.action_homePage_to_events2)
            R.id.routes ->
                MAIN.navController.navigate(R.id.action_homePage_to_mapFragment)
        }
        return true
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

