package ru.spb.rollers.screen.eventsparticipant

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.spb.rollers.MAIN
import ru.spb.rollers.R
import ru.spb.rollers.adapters.ContactAdapter
import ru.spb.rollers.databinding.EventParticipantFragmentBinding
import ru.spb.rollers.model.Contact

class EventParticipantFragment : Fragment() {

    companion object {
        fun newInstance() = EventParticipantFragment()
    }

    private var _binding: EventParticipantFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: EventParticipantViewModel
    private var contactList: List<Contact> = mutableListOf()
    private lateinit var recyclerView: RecyclerView
    private lateinit var contactAdapter: ContactAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = EventParticipantFragmentBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this)[EventParticipantViewModel::class.java]
        // TODO: Use the ViewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.imageButtonBack.setOnClickListener{
            MAIN.onSupportNavigateUp()
        }

        setInitialData()
        recyclerView = view.findViewById(R.id.peopleList)
        contactAdapter = ContactAdapter(contactList)
        recyclerView.adapter = contactAdapter
    }

    private fun setInitialData() {
        contactList += Contact(
            1,1, "Иван","Иванов","ivanov200",true,
            "Московский","38 лет", contactGender = true, isContact = true)
        contactList += Contact(
            2,2, "Варя", "Токсик","jinx",true,
            "Васька","18 лет", contactGender = false, isContact = true)
        contactList += Contact(
            3,1, "Ярик", "Сидоров","jaroslav",
            true, "Мурино","68 лет", contactGender = true, isContact = true)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}