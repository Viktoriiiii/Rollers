package ru.spb.rollers.screens

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.spb.rollers.MAIN
import ru.spb.rollers.R
import ru.spb.rollers.adapters.ContactAdapter
import ru.spb.rollers.databinding.FragmentViewParticipantsOnEventBinding
import ru.spb.rollers.model.Contact

class ViewParticipantsOnEvent : Fragment() {

    private lateinit var binding: FragmentViewParticipantsOnEventBinding
    private var contactList: List<Contact> = mutableListOf()
    private lateinit var recyclerView: RecyclerView
    private lateinit var contactAdapter: ContactAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentViewParticipantsOnEventBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.imageButtonBack.setOnClickListener{
            MAIN.navController.navigate(R.id.action_viewParticipantsOnEvent_to_eventView2)
        }

        setInitialData()
        recyclerView = view.findViewById(R.id.peopleList)
        contactAdapter = ContactAdapter(contactList)
        recyclerView.adapter = contactAdapter
    }

    private fun setInitialData() {
        contactList += Contact(1,
            1, "Иван","Иванов",true,
            "Московский","38 лет", contactGender = true, isContact = true, false)
        contactList += Contact(2,
            2, "Варя", "Токсик",true,
            "Васька","18 лет", contactGender = false, isContact = true, false)
        contactList += Contact(
            3, 1, "Ярик", "Сидоров",
            true, "Мурино","68 лет", contactGender = true, isContact = true, false)
    }
}