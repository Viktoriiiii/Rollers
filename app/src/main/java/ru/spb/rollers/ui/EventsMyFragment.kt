package ru.spb.rollers.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import ru.spb.rollers.*
import ru.spb.rollers.adapters.EventAdapter
import ru.spb.rollers.databinding.EventsMyFragmentBinding
import ru.spb.rollers.models.Event

class EventsMyFragment : Fragment() {

    private var _binding: EventsMyFragmentBinding? = null
    private val binding get() = _binding!!

    private var eventList: MutableList<Event> = mutableListOf()
    private lateinit var eventAdapter: EventAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = EventsMyFragmentBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        MAIN.appViewModel.route = ru.spb.rollers.models.Route()
        MAIN.appViewModel.points.clear()
        MAIN.appViewModel.event = Event()
        binding.imageViewBack.setOnClickListener {
            MAIN.onSupportNavigateUp()
        }
    }

    override fun onResume() {
        super.onResume()
        initEvents()
    }

    private fun initEvents(){
        eventAdapter = EventAdapter(eventList)
        binding.eventsList.adapter = eventAdapter

        REF_DATABASE_EVENT_USER.child(MAIN.appViewModel.user.id)
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()){
                        val events: MutableList<Event> = mutableListOf()
                        for (e in snapshot.children){
                            val ev = e.key.toString()
                            REF_DATABASE_EVENT.child(ev).addValueEventListener(object :ValueEventListener{
                                override fun onDataChange(snapshotChild: DataSnapshot) {
                                    if (snapshotChild.exists()){
                                        val event = snapshotChild.getEventModel()
                                        events.add(event)
                                        eventAdapter.setList(events)
                                        eventList = events
                                    }
                                }
                                override fun onCancelled(error: DatabaseError) {}
                            })
                        }
                    }
                }
                override fun onCancelled(error: DatabaseError) {}
            })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}