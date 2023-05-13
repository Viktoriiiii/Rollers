package ru.spb.rollers.screens

import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.google.android.material.button.MaterialButton
import ru.spb.rollers.MAIN
import ru.spb.rollers.R
import ru.spb.rollers.databinding.FragmentEventAddingBinding
import ru.spb.rollers.model.Route

class EventAdding : Fragment() {

    private lateinit var binding: FragmentEventAddingBinding
    private var routeList: List<Route> = mutableListOf()
    private lateinit var routeName: Array<String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEventAddingBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.imageButtonBack.setOnClickListener{
            MAIN.navController.navigate(R.id.action_eventAdding_to_events2)
        }

        setInitialData()
//        routeName = routeList.map { route ->
//            "${route.routeName} (${route.routeStartLocation} - ${route.routeEndLocation})"
//        }.toTypedArray()

        val btnAddRoute: MaterialButton = view.findViewById(R.id.btnAddRoute)
        btnAddRoute.setOnClickListener{ addRoute(view) }

        val btnSaveRoute: MaterialButton = view.findViewById(R.id.btnSaveRoute)
        btnSaveRoute.setOnClickListener{
            Toast.makeText(activity, "Мероприятие сохранено",
                Toast.LENGTH_SHORT).show()
            MAIN.navController.navigate(R.id.action_eventAdding_to_events2)
        }
    }

    private fun addRoute(view: View){
        val builder = AlertDialog.Builder(MAIN)
        builder.setTitle("Выберите машрут")
            .setSingleChoiceItems(routeList.map { route ->
                "${route.routeName} (${route.routeStartLocation} - ${route.routeEndLocation})"
            }.toTypedArray(), -1
            ) { dialog, item ->
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
            ) { dialog, id ->
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
}