package ru.spb.rollers.ui.contacts

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

        binding.searchView.setOnSearchClickListener{
            binding.txvTitle.visibility = View.GONE
        }

        binding.searchView.setOnCloseListener {
            binding.txvTitle.visibility = View.VISIBLE
            binding.searchView.onActionViewCollapsed()
            true
        }
    }

    private fun setInitialData() {
        contactList += Contact(1,
            1, "Иван","Иванов",true,
            "Московский","38 лет", contactGender = true, isContact = true, false)
        contactList += Contact(2,
            2, "Варя", "Федорова",true,
            "Васька","18 лет", contactGender = false, isContact = true, false)
        contactList += Contact(
            3, 1, "Ярик", "Сидоров",
            true, "Мурино","68 лет", contactGender = true, isContact = true, false)
        contactList += Contact(
            4,1, "Шуня", "Шуняков",true,
            "Фрунзенский","15 лет", contactGender = true, isContact = true, false)
        contactList += Contact(
            5,2,  "Петр", "Иванов",
            true, "Фрунзенский","15 лет", contactGender = true,
            isContact = true, false)
        contactList += Contact(
            6,2, "Иван", "Петров",
            true, "Фрунзенский","15 лет", contactGender = true,
            isContact = true, false)
        contactList += Contact(
            7, 2, "Иван", "Петров",
            true, "Фрунзенский","15 лет", contactGender = true,
            isContact = true, false)
        contactList += Contact(
            8,1, "Иван", "Петров",
            true, "Фрунзенский","15 лет", contactGender = true,
            isContact = true, false)
        contactList += Contact(
            9,1, "Иван", "Петров",
            true, "Фрунзенский","15 лет", contactGender = true,
            isContact = true, false)
        contactList += Contact(
            9,2, "Роллер-школа Фантаст", "Фрунзенский",
            isContact = true, true, "Обучение катанию на роликах детей и взрослых: свой роллердром / скейт-парк, опытные инструкторы по роликам, доступные цены.", "Адрес школы")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}