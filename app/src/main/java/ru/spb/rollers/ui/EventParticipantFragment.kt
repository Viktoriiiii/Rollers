package ru.spb.rollers.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import ru.spb.rollers.MAIN
import ru.spb.rollers.REF_DATABASE_EVENT_PARTICIPANT
import ru.spb.rollers.REF_DATABASE_USER
import ru.spb.rollers.adapters.UserAdapter
import ru.spb.rollers.databinding.EventParticipantFragmentBinding
import ru.spb.rollers.models.User

class EventParticipantFragment : Fragment() {

    private var _binding: EventParticipantFragmentBinding? = null
    private val binding get() = _binding!!

    private val adapter: UserAdapter = UserAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = EventParticipantFragmentBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.imageButtonBack.setOnClickListener{
            MAIN.onSupportNavigateUp()
        }

        showMyParticipants()
    }

    private fun showMyParticipants(){
        // добыть список id контактов и по ним добавить в список участников
        REF_DATABASE_EVENT_PARTICIPANT.child(MAIN.appViewModel.event.id).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val listUsers: MutableList<User> = mutableListOf()
                for (e in snapshot.children){
                    val user = e.key.toString()
                    REF_DATABASE_USER.child(user).addListenerForSingleValueEvent(object :
                        ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            if (snapshot.exists()){
                                val contact = snapshot.getValue<User>()!!
                                listUsers.add(contact)
                            }
                            binding.peopleList.adapter = adapter
                            adapter.itemsUser = listUsers
                        }
                        override fun onCancelled(error: DatabaseError) {}
                    })
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