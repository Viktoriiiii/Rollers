package ru.spb.rollers.ui.eventsviewroute

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.spb.rollers.MAIN
import ru.spb.rollers.databinding.EventsViewRouteFragmentBinding

class EventsViewRouteFragment : Fragment() {

    companion object {
        fun newInstance() = EventsViewRouteFragment()
    }

    private var _binding: EventsViewRouteFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: EventsViewRouteViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = EventsViewRouteFragmentBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this)[EventsViewRouteViewModel::class.java]
        // TODO: Use the ViewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.imageViewBack.setOnClickListener{
            MAIN.onSupportNavigateUp()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}