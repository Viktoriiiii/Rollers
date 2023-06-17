package ru.spb.rollers.ui.contacts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import ru.spb.rollers.*
import ru.spb.rollers.adapters.ContactAdapter
import ru.spb.rollers.databinding.ContactsFragmentBinding
import ru.spb.rollers.holders.UserViewHolder
import ru.spb.rollers.models.User

class ContactsFragment : Fragment() {

    private var _binding: ContactsFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapterContacts: FirebaseRecyclerAdapter<User, UserViewHolder>
    private lateinit var viewModel: ContactsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this)[ContactsViewModel::class.java]
        _binding = ContactsFragmentBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.ivSearch.setOnClickListener{
            MAIN.navController.navigate(R.id.action_contacts_to_contactsSearchFragment)
        }

        showMyContacts()
    }

    private fun showMyContacts(){
        val options =
            FirebaseRecyclerOptions.Builder<User>()
                .setQuery(REF_DATABASE_CONTACT.child(MAIN.appViewModel.user.id), User::class.java)
                .build()
        adapterContacts = ContactAdapter(options)
        binding.contactsList.adapter = adapterContacts
    }

    override fun onStart() {
        super.onStart()
        adapterContacts.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapterContacts.stopListening()
    }
}


