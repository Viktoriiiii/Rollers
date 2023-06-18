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
import ru.spb.rollers.models.EventUser

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

        eventList.clear()

        eventAdapter = EventAdapter(eventList)
        binding.eventsList.adapter = eventAdapter

        initEvents()
    }

    private fun initEvents(){
        REF_DATABASE_EVENT_USER.child(MAIN.appViewModel.user.id)
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()){
                        eventList.clear()
                        eventAdapter.setList(eventList)
                        val events = snapshot.children.map { it.getEventUserModel() } as MutableList<EventUser>
                        for (e in events){
                            REF_DATABASE_EVENT.child(e.id).addValueEventListener(object :ValueEventListener{
                                override fun onDataChange(snapshotChild: DataSnapshot) {
                                    if (snapshotChild.exists()){
                                        val event = snapshotChild.getEventModel()
                                        eventList.add(event)
                                        eventAdapter.setList(eventList)
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