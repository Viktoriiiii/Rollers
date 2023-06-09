package ru.spb.rollers.ui.users

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import ru.spb.rollers.*
import ru.spb.rollers.adapters.UserAdapter
import ru.spb.rollers.databinding.UsersFragmentBinding
import ru.spb.rollers.models.User
import java.util.*

class UsersFragment : Fragment() {

    private var _binding: UsersFragmentBinding? = null
    private val binding get() = _binding!!
    var eventListener: ValueEventListener? = null
    private var listUsers: List<User> = ArrayList()
    private lateinit var adapter: UserAdapter

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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun searchList(text: String) {
        val searchList: ArrayList<User> = ArrayList()
        for (user in listUsers) {
            if (user.lastName?.toLowerCase()?.contains(text.lowercase(Locale.getDefault())) == true ||
                user.firstName?.toLowerCase()?.contains(text.lowercase(Locale.getDefault())) == true ||
                user.schoolName?.toLowerCase()?.contains(text.lowercase(Locale.getDefault())) == true ||
                user.description?.toLowerCase()?.contains(text.lowercase(Locale.getDefault())) == true ||
                user.district?.toLowerCase()?.contains(text.lowercase(Locale.getDefault())) == true ||
                user.address?.toLowerCase()?.contains(text.lowercase(Locale.getDefault())) == true ||
                user.email?.toLowerCase()?.contains(text.lowercase(Locale.getDefault())) == true) {
                searchList.add(user)
            }
        }
        adapter.searchDataList(searchList)
    }
}