package ru.spb.rollers.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import ru.spb.rollers.*
import ru.spb.rollers.adapters.UserAdapter
import ru.spb.rollers.databinding.ContactsSearchFragmentBinding
import ru.spb.rollers.models.User
import java.util.*

class ContactsSearchFragment : Fragment() {

    private lateinit var binding: ContactsSearchFragmentBinding
    private var listUsers: MutableList<User> = mutableListOf()
    private val adapter: UserAdapter = UserAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ContactsSearchFragmentBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showAllUsers()

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
    }

    private fun showAllUsers(){
        binding.contactsList.adapter = adapter
        REF_DATABASE_USER.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val listUser = snapshot.children.map { it.getUserModel() } as MutableList<User>
                    listUser.removeAll { it.id == MAIN.appViewModel.user.id || it.role == "Администратор"}
                    adapter.itemsUser = listUser
                    listUsers = listUser
                }
            }
            override fun onCancelled(error: DatabaseError) {}
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
        adapter.itemsUser = searchList
    }
}