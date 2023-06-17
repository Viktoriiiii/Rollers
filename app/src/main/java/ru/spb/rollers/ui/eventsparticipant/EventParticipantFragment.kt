package ru.spb.rollers.ui.eventsparticipant

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.firebase.ui.database.FirebaseRecyclerOptions
import ru.spb.rollers.MAIN
import ru.spb.rollers.REF_DATABASE_EVENT_PARTICIPANT
import ru.spb.rollers.adapters.ContactAdapter
import ru.spb.rollers.databinding.EventParticipantFragmentBinding
import ru.spb.rollers.models.User

class EventParticipantFragment : Fragment() {

    private var _binding: EventParticipantFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: EventParticipantViewModel
    private lateinit var contactAdapter: ContactAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this)[EventParticipantViewModel::class.java]
        _binding = EventParticipantFragmentBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.imageButtonBack.setOnClickListener{
            MAIN.onSupportNavigateUp()
        }

        showMyContacts()
    }

    private fun showMyContacts(){
        val options =
            FirebaseRecyclerOptions.Builder<User>()
                .setQuery(REF_DATABASE_EVENT_PARTICIPANT.child(MAIN.appViewModel.event.id), User::class.java)
                .build()
        contactAdapter = ContactAdapter(options)
        binding.peopleList.adapter = contactAdapter
    }

    override fun onStart() {
        super.onStart()
        contactAdapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        contactAdapter.stopListening()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}