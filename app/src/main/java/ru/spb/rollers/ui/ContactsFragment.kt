package ru.spb.rollers.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import ru.spb.rollers.*
import ru.spb.rollers.adapters.UserAdapter
import ru.spb.rollers.databinding.ContactsFragmentBinding
import ru.spb.rollers.models.User

class ContactsFragment : Fragment() {

    private var _binding: ContactsFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: UserAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ContactsFragmentBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        MAIN.appViewModel.setPhoto(binding.ivPhoto)

        binding.ivSearch.setOnClickListener{
            MAIN.navController.navigate(R.id.action_contacts_to_contactsSearchFragment)
        }

        showMyContacts()
    }

    private fun showMyContacts(){
        // добыть список id контактов и по ним добавить в список юзеров
        REF_DATABASE_CONTACT.child(MAIN.appViewModel.user.id).addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val listUsers: MutableList<User> = mutableListOf()
                for (e in snapshot.children){
                    val user = e.key.toString()
                    REF_DATABASE_USER.child(user).addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            if (snapshot.exists()){
                                val contact = snapshot.getValue<User>()!!
                                listUsers.add(contact)
                            }
                            adapter = UserAdapter(listUsers)
                            binding.contactsList.adapter = adapter
                        }
                        override fun onCancelled(error: DatabaseError) {}
                    })
                }
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }
}


