package ru.spb.rollers.ui.contactssearch

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import ru.spb.rollers.MAIN
import ru.spb.rollers.R
import ru.spb.rollers.REF_DATABASE_USER
import ru.spb.rollers.adapters.UserAdapter
import ru.spb.rollers.databinding.ContactsSearchFragmentBinding
import ru.spb.rollers.models.User
import java.util.*

class ContactsSearchFragment : Fragment() {

    private lateinit var binding: ContactsSearchFragmentBinding
    private var eventListener: ValueEventListener? = null
    private var listUsers: MutableList<User> = mutableListOf()
    private lateinit var adapter: UserAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ContactsSearchFragmentBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.searchView.setOnSearchClickListener {
            binding.txvTitle.visibility = View.GONE
        }

        binding.searchView.setOnCloseListener {
            binding.txvTitle.visibility = View.VISIBLE
            binding.searchView.onActionViewCollapsed()
            true
        }

        binding.imageButtonBack.setOnClickListener{
            MAIN.navController.navigate(R.id.action_contactsSearchFragment_to_contacts)
        }

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null) {
                    searchList(newText)
                }
                return true
            }
        })

        binding.contactsList.layoutManager = LinearLayoutManager(MAIN)
        adapter = UserAdapter(listUsers)
        binding.contactsList.adapter = adapter
        eventListener = REF_DATABASE_USER.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                listUsers.clear()
                for (itemSnapshot in snapshot.children) {
                    val user: User = itemSnapshot.getValue<User>()!!
                    listUsers += user
                }
                binding.contactsList.post {
                    adapter = UserAdapter(listUsers)
                    binding.contactsList.adapter = adapter
                }
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    fun searchList(text: String) {
        val searchList: ArrayList<User> = ArrayList()
        for (user in listUsers) {
            if (user.lastName?.lowercase(Locale.getDefault())
                    ?.contains(text.lowercase(Locale.getDefault())) == true ||
                user.firstName?.lowercase(Locale.getDefault())
                    ?.contains(text.lowercase(Locale.getDefault())) == true ||
                user.schoolName?.lowercase(Locale.getDefault())
                    ?.contains(text.lowercase(Locale.getDefault())) == true ||
                user.description?.lowercase(Locale.getDefault())
                    ?.contains(text.lowercase(Locale.getDefault())) == true ||
                user.district?.lowercase(Locale.getDefault())
                    ?.contains(text.lowercase(Locale.getDefault())) == true ||
                user.address?.lowercase(Locale.getDefault())
                    ?.contains(text.lowercase(Locale.getDefault())) == true){
                searchList.add(user)
            }
        }
        adapter.searchDataList(searchList)
    }
}