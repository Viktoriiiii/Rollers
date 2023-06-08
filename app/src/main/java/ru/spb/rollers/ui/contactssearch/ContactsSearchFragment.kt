package ru.spb.rollers.ui.contactssearch

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import ru.spb.rollers.MAIN
import ru.spb.rollers.R
import ru.spb.rollers.REF_DATABASE_USER
import ru.spb.rollers.adapters.UserAdapter
import ru.spb.rollers.databinding.ContactsSearchFragmentBinding
import ru.spb.rollers.holders.UserViewHolder
import ru.spb.rollers.models.User

class ContactsSearchFragment : Fragment() {

    private lateinit var binding: ContactsSearchFragmentBinding
    private lateinit var adapterUsers: FirebaseRecyclerAdapter<User, UserViewHolder>

    companion object {
        fun newInstance() = ContactsSearchFragment()
    }

    private lateinit var viewModel: ContactsSearchViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this)[ContactsSearchViewModel::class.java]
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

        showAllUsers()
    }

    private fun showAllUsers() {
        val options =
            FirebaseRecyclerOptions.Builder<User>()
                .setQuery(REF_DATABASE_USER, User::class.java)
                .build()
        adapterUsers = UserAdapter(options)
        binding.contactsList.adapter = adapterUsers
    }

    override fun onStart() {
        super.onStart()
        adapterUsers.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapterUsers.stopListening()
    }
}