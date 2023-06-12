package ru.spb.rollers.ui.events

import android.graphics.drawable.PictureDrawable
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.caverock.androidsvg.SVG
import com.caverock.androidsvg.SVGParseException
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import okhttp3.*
import okio.IOException
import org.json.JSONObject
import ru.spb.rollers.*
import ru.spb.rollers.oldadapters.EventAdapter
import ru.spb.rollers.databinding.EventsFragmentBinding
import ru.spb.rollers.oldmodel.Event

class EventsFragment : Fragment()
{
    private var _binding: EventsFragmentBinding? = null
    private val binding get() = _binding!!

    private var eventList: List<Event> = mutableListOf()
    private lateinit var recyclerView: RecyclerView
    private lateinit var eventAdapter: EventAdapter

    private val WEATHER_API_KEY = "43cf8001-588a-477e-b84f-0a140208c3de"
    var temp: Int = 0
    var condition: String = ""
    var imageUrl: String = "https://yastatic.net/weather/i/icons/funky/dark/"

    private lateinit var viewModel: EventsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this)[EventsViewModel::class.java]
        _binding = EventsFragmentBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        MAIN.setBottomNavigationVisible(true)

        binding.imageButtonAddEvent.setOnClickListener{
            MAIN.navController.navigate(R.id.action_events_to_eventsCreateFragment)
            titleEvents = "Создание события"
        }

        setInitialData()
        recyclerView = view.findViewById(R.id.eventsList)
        eventAdapter = EventAdapter(eventList, 2)
        recyclerView.adapter = eventAdapter

        binding.searchView.setOnSearchClickListener{
            binding.txvTitle.visibility = View.GONE
            binding.txvWeatherTemp.visibility = View.GONE
            binding.ivWeather.visibility = View.GONE
        }

        binding.searchView.setOnCloseListener {
            binding.txvTitle.visibility = View.VISIBLE
            binding.txvWeatherTemp.visibility = View.VISIBLE
            binding.ivWeather.visibility = View.VISIBLE
            binding.searchView.onActionViewCollapsed()
            true
        }

        if (!MAIN.appViewModel.user.isManager)
            binding.rlAddEvent.visibility = View.GONE

        getWeather()

        getCountUnreadMessageDialogs()
    }

    private fun getCountUnreadMessageDialogs() {

        REF_DATABASE_DIALOG.child(MAIN.appViewModel.user.id).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var unreadDialogsCount = 0
                for (dialogSnapshot in snapshot.children) {
                    val dialogId = dialogSnapshot.key
                    // Получение списка сообщений для текущего диалога
                    val messagesRef = REF_DATABASE_DIALOG.child(MAIN.appViewModel.user.id)
                        .child(dialogId!!).child("Messages")
                    messagesRef.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(messagesSnapshot: DataSnapshot) {
                            var count = 0
                            val messages = messagesSnapshot.children.map { it.getMessageModel() }
                            count = messages.count{ !it.read  && it.from != MAIN.appViewModel.user.id}

                            if (count > 0)
                                unreadDialogsCount++

                            if (unreadDialogsCount == 0){
                                MAIN.bottomNavigationView.removeBadge(R.id.dialogs)
                            }
                            else
                                MAIN.bottomNavigationView.getOrCreateBadge(R.id.dialogs).number = unreadDialogsCount
                        }
                        override fun onCancelled(error: DatabaseError) {}
                    })
                }
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }


    private fun getWeather() {
        val url = "https://api.weather.yandex.ru/v2/forecast?lat=59.939427&lon=30.309217&extra=true"

        val request: Request = Request.Builder()
            .url(url)
            .addHeader("X-Yandex-API-Key", WEATHER_API_KEY)
            .build()

        val client = OkHttpClient()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }
            override fun onResponse(call: Call, response: Response) {
                val responseBody = response.body?.string()
                val obj = JSONObject(responseBody)
                val fact = obj.getJSONObject("fact")
                temp = fact.optInt("temp")
                condition = fact.optString("condition")
                imageUrl = "$imageUrl${fact.optString("icon")}.svg"

                activity?.runOnUiThread {
                    binding.txvWeatherTemp.text = if (temp > 0) "+${temp} ℃" else "-${temp} ℃"
                    loadSvgImage(imageUrl, binding.ivWeather)
                }
            }
        })
    }

    fun loadSvgImage(svgUrl: String, imageView: ImageView) {
        val request = Request.Builder().url(svgUrl).build()
        val client = OkHttpClient()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }
            override fun onResponse(call: Call, response: Response) {
                val responseBody = response.body?.string()
                responseBody?.let {
                    try {
                        val svg = SVG.getFromString(it)
                        val picture = svg.renderToPicture()
                        val drawable = PictureDrawable(picture)
                        imageView.post {
                            imageView.setImageDrawable(drawable)

                        }
                    } catch (e: SVGParseException) {
                        e.printStackTrace()
                    }
                }
            }
        })
    }

    private fun setInitialData() {
        eventList += Event(
            1, "Покатушка на роликах","Дворцовая площадь",
            "ст.м. Крестовский остров", 1, "03.05.2023 13:00",
            "13:00", "13:30", "10 км/ч", "15 км",
            "Описание какое-нибудь", 0.0, true)
        eventList += Event(
            2, "Покатушка на роликах","Васька",
            "Петроградка", 2, "03.05.2023 13:00",
            "13:00", "13:30", "10 км/ч", "15 км",
            "Описание какое-нибудь", 550.0,true)
        eventList += Event(
            3, "Покатушка на роликах","Васька",
            "Петроградка", 3, "03.05.2023 13:00",
            "13:00", "13:30", "10 км/ч", "15 км",
            "Описание какое-нибудь", 0.0, true)
        eventList += Event(
            4, "Покатушка на роликах","Васька",
            "Петроградка", 4, "03.05.2023 13:00",
            "13:00", "13:30", "10 км/ч", "15 км",
            "Описание какое-нибудь", 1000.0,true)
        eventList += Event(
            5, "Покатушка на роликах","Васька",
            "Петроградка", 5, "03.05.2023 13:00",
            "13:00", "13:30", "10 км/ч", "15 км",
            "Описание какое-нибудь", 0.0,true)
        eventList += Event(
            6, "Покатушка на роликах","Васька",
            "Петроградка", 6, "03.05.2023 13:00",
            "13:00", "13:30", "10 км/ч", "15 км",
            "Описание какое-нибудь", 0.0,true)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

