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
import ru.spb.rollers.databinding.FragmentContactSearchBinding
import ru.spb.rollers.model.Contact

class ContactSearch : Fragment() {

    private lateinit var binding: FragmentContactSearchBinding
    private var contactList: List<Contact> = mutableListOf()
    private lateinit var recyclerView: RecyclerView
    private lateinit var contactAdapter: ContactAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentContactSearchBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.imageViewBack.setOnClickListener{
            MAIN.navController.navigate(R.id.action_contactSearch_to_contacts)
        }
        setInitialData()
        recyclerView = view.findViewById(R.id.peopleList)
        contactAdapter = ContactAdapter(contactList)
        recyclerView.adapter = contactAdapter
    }

    private fun setInitialData() {
        contactList += Contact(1,
            1, "Иван","Иванов","ivanov200",true,
            "Московский","38 лет", contactGender = true, isContact = true, false)
        contactList += Contact(2,
            2, "Варя", "Токсик","jinx",true,
            "Васька","18 лет", contactGender = false, isContact = true, false)
        contactList += Contact(
            3, 1, "Ярик", "Сидоров","jaroslav",
            true, "Мурино","68 лет", contactGender = true, isContact = true, false)
        contactList += Contact(
            4,1, "Шуня", "Душный","shunya",true,
            "Фрунзенский","15 лет", contactGender = true, isContact = true, false)
        contactList += Contact(
            5,2,  "Петр", "Иванов","ivanov",
            true, "Фрунзенский","15 лет", contactGender = true,
            isContact = true, false)
        contactList += Contact(
            6,2, "Иван", "Петров","ivanov",
            true, "Фрунзенский","15 лет", contactGender = true,
            isContact = true, false)
        contactList += Contact(
            7, 2, "Иван", "Петров","ivanov",
            true, "Фрунзенский","15 лет", contactGender = true,
            isContact = true, false)
        contactList += Contact(
            8,1, "Иван", "Петров","ivanov",
            true, "Фрунзенский","15 лет", contactGender = true,
            isContact = true, false)
        contactList += Contact(
            9,1, "Иван", "Петров","ivanov",
            true, "Фрунзенский","15 лет", contactGender = true,
            isContact = true, false)
        contactList += Contact(
            9,2, "Имя школы", "ivanov", "Фрунзенский",
            isContact = true, true, "Описание школа", "Адрес школы")
    }
}