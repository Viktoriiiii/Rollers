package ru.spb.rollers.ui.dialogs

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import ru.spb.rollers.*
import ru.spb.rollers.adapters.DialogAdapter
import ru.spb.rollers.databinding.DialogsFragmentBinding
import ru.spb.rollers.models.Dialog

class DialogsFragment : Fragment() {

    private var _binding: DialogsFragmentBinding? = null
    private val binding get() = _binding!!

    private var listDialogs: MutableList<Dialog> = mutableListOf()
    private lateinit var dialogAdapter: DialogAdapter
    private lateinit var viewModel: DialogsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this)[DialogsViewModel::class.java]
        _binding = DialogsFragmentBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
    }

    private fun initRecyclerView() {
        dialogAdapter = DialogAdapter(listDialogs)

        val ref = REF_DATABASE_DIALOG
            .child(MAIN.appViewModel.user.id)

        binding.dialogList.adapter = dialogAdapter

        ref.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val dialogList = snapshot.children.map { it.getDialogModel() }
                dialogAdapter.setList(dialogList)
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
    }
}