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
import ru.spb.rollers.databinding.FragmentContactsBinding
import ru.spb.rollers.model.Contact

class Contacts : Fragment() {

    private lateinit var binding: FragmentContactsBinding
    private var contactList: List<Contact> = mutableListOf()
    private lateinit var recyclerView: RecyclerView
    private lateinit var contactAdapter: ContactAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentContactsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.imageViewBack.setOnClickListener{
            MAIN.navController.navigate(R.id.action_contacts_to_homePage)
        }

        binding.imageViewSearchContacts.setOnClickListener{
            MAIN.navController.navigate(R.id.action_contacts_to_contactSearch)
        }

        setInitialData()
        recyclerView = view.findViewById(R.id.contactsList)
        contactAdapter = ContactAdapter(contactList)
        recyclerView.adapter = contactAdapter
    }

    private fun setInitialData() {
        contactList += Contact(
            1, "Иван Петров",true, "Московский",
            "38 лет", true)
        contactList += Contact(
            2, "Варя Токсик",true, "Васька",
            "18 лет", true)
        contactList += Contact(
            3, "Ярик Сидоров",true, "Мурино",
            "68 лет", true)
        contactList += Contact(
            4, "Шуня Душный",true, "Фрунзенский",
            "15 лет", true)
        contactList += Contact(
            5, "Петр Иванов",false, "Фрунзенский",
            "15 лет", true)
        contactList += Contact(
            6, "Иван Петров",true, "Фрунзенский",
            "15 лет", true)
        contactList += Contact(
            7, "Иван Петров",true, "Фрунзенский",
            "15 лет", true)
        contactList += Contact(
            8, "Иван Петров",true, "Фрунзенский",
            "15 лет", true)
        contactList += Contact(
            9, "Иван Петров",true, "Фрунзенский",
            "15 лет", true)
    }
}