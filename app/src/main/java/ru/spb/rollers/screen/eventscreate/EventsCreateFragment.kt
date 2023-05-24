package ru.spb.rollers.screen.eventscreate

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.text.Editable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.android.material.button.MaterialButton
import ru.spb.rollers.MAIN
import ru.spb.rollers.R
import ru.spb.rollers.databinding.EventsCreateFragmentBinding
import ru.spb.rollers.model.Route

class EventsCreateFragment : Fragment() {

    companion object {
        fun newInstance() = EventsCreateFragment()
    }

    private var _binding: EventsCreateFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: EventsCreateViewModel
    private var routeList: List<Route> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = EventsCreateFragmentBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this)[EventsCreateViewModel::class.java]
        // TODO: Use the ViewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.imageButtonBack.setOnClickListener{
            MAIN.onSupportNavigateUp()
        }

        setInitialData()

        val btnAddRoute: MaterialButton = view.findViewById(R.id.btnAddRoute)
        btnAddRoute.setOnClickListener{ addRoute() }

        val btnSaveRoute: MaterialButton = view.findViewById(R.id.btnSaveRoute)
        btnSaveRoute.setOnClickListener{
            Toast.makeText(activity, "Мероприятие сохранено",
                Toast.LENGTH_SHORT).show()
            MAIN.onSupportNavigateUp()
        }

        binding.imageButtonDeleteEvent.setOnClickListener{
            val builderDeleteDialog: AlertDialog.Builder = AlertDialog.Builder(MAIN)
            builderDeleteDialog
                .setTitle("Вы уверены, что хотите удалить мероприятие?")
                .setCancelable(false)
                .setPositiveButton("Да") { _, _ ->
                    MAIN.onSupportNavigateUp()
                    Toast.makeText(MAIN, "Мероприятие удалено", Toast.LENGTH_SHORT).show()
                }
                .setNegativeButton("Отмена"){dialog, _ ->
                    dialog.cancel()
                }
            val alertDialogDeletePhoto: AlertDialog = builderDeleteDialog.create()
            alertDialogDeletePhoto.show()
        }
    }
    private fun addRoute(){
        val builder = AlertDialog.Builder(MAIN)
        builder.setTitle("Выберите машрут")
            .setSingleChoiceItems(routeList.map { route ->
                "${route.routeName} (${route.routeStartLocation} - ${route.routeEndLocation})"
            }.toTypedArray(), -1
            ) { _, item ->
                binding.tvEventStartLocation.visibility = View.VISIBLE
                binding.etEventStartLocation.visibility = View.VISIBLE
                binding.etEventStartLocation.text =
                    Editable.Factory.getInstance().newEditable(routeList[item].routeStartLocation)

                binding.tvEventEndLocation.visibility = View.VISIBLE
                binding.etEventEndLocation.visibility = View.VISIBLE
                binding.etEventEndLocation.text =
                    Editable.Factory.getInstance().newEditable(routeList[item].routeEndLocation)

                binding.tvEventDistance.visibility = View.VISIBLE
                binding.etEventDistance.visibility = View.VISIBLE
                binding.etEventDistance.text =
                    Editable.Factory.getInstance().newEditable(routeList[item].routeDistance)

                binding.btnAddRoute.text = "Изменить маршрут"
            }
            .setPositiveButton("OK"
            ) { _, _ ->
                Toast.makeText(activity, "Маршрут обновлен",
                    Toast.LENGTH_SHORT).show()
            }
        builder.create()
        builder.show()
    }

    private fun setInitialData() {
        routeList += Route(
            1, "Маршрут № 1", "Васька",
            "Петроградка",  "15 км")
        routeList += Route(
            2, "Маршрут № 2", "Васька",
            "Петроградка",  "15 км")
        routeList += Route(
            3, "Маршрут № 3", "Васька",
            "Петроградка",  "15 км")
        routeList += Route(
            4, "Маршрут № 4", "Васька",
            "Петроградка",  "15 км")
        routeList += Route(
            5, "Маршрут № 5", "Васька",
            "Петроградка",  "15 км")
        routeList += Route(
            6, "Маршрут № 6", "Васька",
            "Петроградка",  "15 км")
        routeList += Route(
            7, "Маршрут № 7", "Васька",
            "Петроградка",  "15 км")
        routeList += Route(
            8, "Маршрут № 8", "Васька",
            "Петроградка",  "15 км")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}