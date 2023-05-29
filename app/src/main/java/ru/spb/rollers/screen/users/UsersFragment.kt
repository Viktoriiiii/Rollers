package ru.spb.rollers.screen.users

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.spb.rollers.AppActivity
import ru.spb.rollers.MAIN
import ru.spb.rollers.R
import ru.spb.rollers.adapters.ContactAdapter
import ru.spb.rollers.databinding.UsersFragmentBinding
import ru.spb.rollers.delayMillis
import ru.spb.rollers.model.Contact

class UsersFragment : Fragment() {

    private var _binding: UsersFragmentBinding? = null
    private val binding get() = _binding!!

    private var contactList: List<Contact> = mutableListOf()
    private lateinit var recyclerView: RecyclerView
    private lateinit var contactAdapter: ContactAdapter

    companion object {
        fun newInstance() = UsersFragment()
    }

    private lateinit var viewModel: UsersViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = UsersFragmentBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(UsersViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setInitialData()
        recyclerView = view.findViewById(R.id.contactsList)
        contactAdapter = ContactAdapter(contactList)
        recyclerView.adapter = contactAdapter

        MAIN.setBottomNavigationVisible(false)

        binding.searchView.setOnSearchClickListener{
            binding.txvTitle.visibility = View.GONE
        }

        binding.searchView.setOnCloseListener {
            binding.txvTitle.visibility = View.VISIBLE
            binding.searchView.onActionViewCollapsed()
            true
        }

        binding.ivExit.setOnClickListener{
            MAIN.navController.navigate(R.id.action_usersFragment_to_authorizationFragment)
            MAIN.setBottomNavigationVisible(false)
            MAIN.finish()
            startActivity(Intent(MAIN, AppActivity::class.java)  )
            delayMillis = 0
        }
    }

    private fun setInitialData() {
        contactList += Contact(1,
            1, "Иван","Иванов","ivanov200",true,
            "Московский","38 лет", contactGender = true, isContact = false, false)
        contactList += Contact(2,
            2, "Варя", "Федорова","jinx",true,
            "Васька","18 лет", contactGender = false, isContact = false, false)
        contactList += Contact(
            3, 1, "Ярик", "Сидоров","jaroslav",
            true, "Мурино","68 лет", contactGender = true, isContact = false, false)
        contactList += Contact(
            4,1, "Шуня", "Веселый","shunya",true,
            "Фрунзенский","15 лет", contactGender = true, isContact = false, false)
        contactList += Contact(
            5,2,  "Петр", "Иванов","ivanov",
            true, "Фрунзенский","15 лет", contactGender = true,
            isContact = false, false)
        contactList += Contact(
            6,2, "Иван", "Петров","ivanov",
            true, "Фрунзенский","15 лет", contactGender = true,
            isContact = false, false)
        contactList += Contact(
            7, 2, "Иван", "Петров","ivanov",
            true, "Фрунзенский","15 лет", contactGender = true,
            isContact = false, false)
        contactList += Contact(
            8,1, "Иван", "Петров","ivanov",
            true, "Фрунзенский","15 лет", contactGender = true,
            isContact = false, false)
        contactList += Contact(
            9,1, "Иван", "Петров","ivanov",
            true, "Фрунзенский","15 лет", contactGender = true,
            isContact = false, false)
        contactList += Contact(
            9,2, "Роллер-школа Фантаст", "ivanov", "Фрунзенский",
            isContact = false, true, "Обучение катанию на роликах детей и взрослых: свой роллердром / скейт-парк, опытные инструкторы по роликам, доступные цены.", "Адрес школы")
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}