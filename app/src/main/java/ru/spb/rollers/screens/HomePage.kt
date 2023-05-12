package ru.spb.rollers.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.SwitchCompat
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationView
import ru.spb.rollers.MAIN
import ru.spb.rollers.R
import ru.spb.rollers.adapters.DialogAdapter
import ru.spb.rollers.databinding.FragmentHomePageBinding
import ru.spb.rollers.model.Dialog

class HomePage : Fragment(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: FragmentHomePageBinding
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var toolbar: Toolbar
    private var dialogList: List<Dialog> = mutableListOf()
    private lateinit var recyclerView: RecyclerView
    private lateinit var dialogAdapter: DialogAdapter
    private lateinit var switchStatus: SwitchCompat

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomePageBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbar = view.findViewById(R.id.toolbar)
        MAIN.setSupportActionBar(toolbar)
        drawerLayout = view.findViewById(R.id.drawer_layout)
        toggle = ActionBarDrawerToggle(activity, drawerLayout, toolbar, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        binding.navigationView.setNavigationItemSelectedListener(this)

        setInitialData()
        recyclerView = view.findViewById(R.id.messages_list)
        dialogAdapter = DialogAdapter(dialogList)
        recyclerView.adapter = dialogAdapter

        val header: View = binding.navigationView.getHeaderView(0)
        switchStatus = header.findViewById(R.id.switchStatus)
        switchStatus.setOnCheckedChangeListener {  buttonView, isChecked ->
            buttonView.text = if (isChecked) "На роликах" else "Не активен"
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.exit -> {
                MAIN.navController.navigate(R.id.action_homePage_to_authorization)
            }
            R.id.profile -> {
                MAIN.navController.navigate(R.id.action_homePage_to_profile2)
            }
            R.id.contacts ->
                MAIN.navController.navigate(R.id.action_homePage_to_contacts)
            R.id.events ->
                MAIN.navController.navigate(R.id.action_homePage_to_events2)
            R.id.routes ->
                MAIN.navController.navigate(R.id.action_homePage_to_mapFragment)
        }
        return true
    }

    private fun setInitialData() {
        dialogList += Dialog(
                1, "Иван Петров",
                "6 марта 2023 21:05", "текстовое сообщение...")
        dialogList += Dialog(
                2, "Иван Петров",
                "6 марта 2023 21:05", "текстовое сообщение...")
        dialogList += Dialog(
                3, "Иван Петров",
                "6 марта 2023 21:05", "текстовое сообщение...")
        dialogList += Dialog(
            4, "Иван Петров",
            "6 марта 2023 21:05", "текстовое сообщение...")
        dialogList += Dialog(
            5, "Иван Петров",
            "6 марта 2023 21:05", "текстовое сообщение...")
        dialogList += Dialog(
            6, "Иван Петров",
            "6 марта 2023 21:05", "текстовое сообщение...")
        dialogList += Dialog(
            7, "Иван Петров",
            "6 марта 2023 21:05", "текстовое сообщение...")
        dialogList += Dialog(
            8, "Иван Петров",
            "6 марта 2023 21:05", "текстовое сообщение...")
        dialogList += Dialog(
            9, "Иван Петров",
            "6 марта 2023 21:05", "текстовое сообщение...")
    }
}


