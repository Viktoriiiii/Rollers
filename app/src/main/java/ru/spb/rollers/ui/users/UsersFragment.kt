package ru.spb.rollers.ui.users

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import ru.spb.rollers.*
import ru.spb.rollers.adapters.UserAdapter
import ru.spb.rollers.databinding.UsersFragmentBinding
import ru.spb.rollers.holders.UserViewHolder
import ru.spb.rollers.models.User

class UsersFragment : Fragment() {

    private var _binding: UsersFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapterUsers: FirebaseRecyclerAdapter<User, UserViewHolder>

    companion object {
        fun newInstance() = UsersFragment()
    }

    private lateinit var viewModel: UsersViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this).get(UsersViewModel::class.java)
        _binding = UsersFragmentBinding.inflate(layoutInflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showAllUsers()
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
            MAIN.finish()
            startActivity(Intent(MAIN, AppActivity::class.java)  )
            delayMillis = 0
        }
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}