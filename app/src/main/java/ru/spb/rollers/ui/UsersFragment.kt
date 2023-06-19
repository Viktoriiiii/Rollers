package ru.spb.rollers.ui

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import ru.spb.rollers.*
import ru.spb.rollers.adapters.UserAdapter
import ru.spb.rollers.databinding.UsersFragmentBinding
import ru.spb.rollers.models.User
import java.util.*

class UsersFragment : Fragment() {

    private var _binding: UsersFragmentBinding? = null
    private val binding get() = _binding!!
    private var listUsers: MutableList<User> = ArrayList()
    private lateinit var adapter: UserAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = UsersFragmentBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        MAIN.setBottomNavigationVisible(false)

        showAllUsers()

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
    }

    private fun showAllUsers(){
        adapter = UserAdapter(listUsers)
        binding.contactsList.adapter = adapter
        REF_DATABASE_USER.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val listUser = snapshot.children.map { it.getUserModel() }
                    adapter.setList(listUser as MutableList<User>)
                    listUsers = listUser
                }
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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
                    ?.contains(text.lowercase(Locale.getDefault())) == true ||
                user.email?.lowercase(Locale.getDefault())
                    ?.contains(text.lowercase(Locale.getDefault())) == true) {
                searchList.add(user)
            }
        }
        adapter.setList(searchList)
    }
}