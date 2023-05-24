package ru.spb.rollers.screen.routes

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.spb.rollers.databinding.RoutesFragmentBinding

class RoutesFragment : Fragment() {

    private lateinit var binding: RoutesFragmentBinding

    companion object {
        fun newInstance() = RoutesFragment()
    }

    private lateinit var viewModel: RoutesViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = RoutesFragmentBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this)[RoutesViewModel::class.java]
        // TODO: Use the ViewModel
    }

}