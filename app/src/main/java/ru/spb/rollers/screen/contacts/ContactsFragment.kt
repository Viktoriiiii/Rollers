package ru.spb.rollers.screen.contacts

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.spb.rollers.R
import ru.spb.rollers.adapters.ContactAdapter
import ru.spb.rollers.databinding.ContactsFragmentBinding
import ru.spb.rollers.model.Contact

class ContactsFragment : Fragment() {

    private var _binding: ContactsFragmentBinding? = null
    private val binding get() = _binding!!

    private var contactList: List<Contact> = mutableListOf()
    private lateinit var recyclerView: RecyclerView
    private lateinit var contactAdapter: ContactAdapter

    companion object {
        fun newInstance() = ContactsFragment()
    }

    private lateinit var viewModel: ContactsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ContactsFragmentBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this)[ContactsViewModel::class.java]
        // TODO: Use the ViewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setInitialData()
        recyclerView = view.findViewById(R.id.contactsList)
        contactAdapter = ContactAdapter(contactList)
        recyclerView.adapter = contactAdapter
    }

    private fun setInitialData() {
        contactList += Contact(1,
            1, "Иван","Иванов","ivanov200",true,
            "Московский","38 лет", contactGender = true, isContact = true)
        contactList += Contact(2,
            2, "Варя", "Токсик","jinx",true,
            "Васька","18 лет", contactGender = false, isContact = true)
        contactList += Contact(
            3, 1, "Ярик", "Сидоров","jaroslav",
            true, "Мурино","68 лет", contactGender = true, isContact = true)
        contactList += Contact(
            4,1, "Шуня", "Душный","shunya",true,
            "Фрунзенский","15 лет", contactGender = true, isContact = true)
        contactList += Contact(
            5,1,  "Петр", "Иванов","ivanov",
            true, "Фрунзенский","15 лет", contactGender = true,
            isContact = true)
        contactList += Contact(
            6,1, "Иван", "Петров","ivanov",
            true, "Фрунзенский","15 лет", contactGender = true,
            isContact = true)
        contactList += Contact(
            7, 1, "Иван", "Петров","ivanov",
            true, "Фрунзенский","15 лет", contactGender = true,
            isContact = true)
        contactList += Contact(
            8,1, "Иван", "Петров","ivanov",
            true, "Фрунзенский","15 лет", contactGender = true,
            isContact = true)
        contactList += Contact(
            9,1, "Иван", "Петров","ivanov",
            true, "Фрунзенский","15 лет", contactGender = true,
            isContact = true)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}