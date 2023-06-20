package ru.spb.rollers.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import ru.spb.rollers.*
import ru.spb.rollers.adapters.DialogAdapter
import ru.spb.rollers.databinding.DialogsFragmentBinding
import ru.spb.rollers.models.Dialog
import ru.spb.rollers.models.User
import java.util.*

class DialogsFragment : Fragment() {

    private var _binding: DialogsFragmentBinding? = null
    private val binding get() = _binding!!

    private var listDialogs: MutableList<Dialog> = mutableListOf()
    private lateinit var dialogAdapter: DialogAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogsFragmentBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        MAIN.appViewModel.setPhoto(binding.ivPhoto)

        binding.searchView.setOnSearchClickListener{
            binding.txvTitle.visibility = View.GONE
        }

        binding.searchView.setOnCloseListener {
            binding.txvTitle.visibility = View.VISIBLE
            binding.searchView.onActionViewCollapsed()
            true
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        initRecyclerView()

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null && listDialogs.isNotEmpty()) {
                    searchList(newText)
                }
                return true
            }
        })
    }

    private fun initRecyclerView() {
        dialogAdapter = DialogAdapter(listDialogs)
        binding.dialogList.adapter = dialogAdapter

        REF_DATABASE_DIALOG
            .child(MAIN.appViewModel.user.id).addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val dialogList = snapshot.children.map { it.getDialogModel() }
                dialogAdapter.setList(dialogList.sortedByDescending {it.pinned  })
                listDialogs = dialogList.toMutableList()
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    fun searchList(text: String) {
        val searchList: MutableList<Dialog> = mutableListOf()
        val idList = listDialogs.map { it.id }

        val userQuery = REF_DATABASE_USER.orderByKey().startAt(idList.first()).endAt(idList.last())

        userQuery.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val userList: MutableList<User> = mutableListOf()
                for (userSnapshot in snapshot.children) {
                    val user = userSnapshot.getValue<User>()!!
                    if (idList.contains(user.id))
                        userList.add(user)
                }

                for (user in userList){
                    if (user.lastName?.lowercase(Locale.getDefault())
                            ?.contains(text.lowercase(Locale.getDefault())) == true ||
                        user.firstName?.lowercase(Locale.getDefault())
                            ?.contains(text.lowercase(Locale.getDefault())) == true ||
                        user.schoolName?.lowercase(Locale.getDefault())
                            ?.contains(text.lowercase(Locale.getDefault())) == true){
                        val foundElement = listDialogs.firstOrNull { it.id in user.id }
                        if (foundElement != null)
                            searchList.add(foundElement)
                    }
                }
                dialogAdapter.setList(searchList.sortedByDescending {it.pinned  })
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }
}