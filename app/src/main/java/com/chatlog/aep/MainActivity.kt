package com.chatlog.aep

import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONObject
import java.net.URL


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var listView = findViewById<ListView>(R.id.eventList)
        var arrEvent: ArrayList<Event> = ArrayList()
        eventsTask().execute()
        listView.adapter = CustomAdapter(applicationContext, arrEvent)
        if (Build.VERSION.SDK_INT > 9) {
            val gfgPolicy = ThreadPolicy.Builder().permitAll().build()
            StrictMode.setThreadPolicy(gfgPolicy)
        }
    }
    inner class eventsTask() : AsyncTask<String, Void, String>() {
        override fun onPreExecute() {
            super.onPreExecute()
        }

        override fun doInBackground(vararg params: String?): String? {
            var response:String?
            try {
                response = URL("https://chatlog.ru/api/aep/events").readText(
                    Charsets.UTF_8
                )
            } catch (e: Exception){
                response = null
            }
            return response
        }

        private fun convertMonth(month: String): String {
            var result: String = if(month.lowercase() == "март" || month.lowercase() == "август") {
                "${month}а"
            } else {
                "${month.dropLast(1)}я"
            }
            return result
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            try {
                /* Extracting JSON returns from the API */
                val jsonObj = JSONObject(result)
                println(jsonObj)
                var listView = findViewById<ListView>(R.id.eventList)
                var arrEvent: ArrayList<Event> = ArrayList()

                val events = jsonObj.getJSONArray("events")
                val listData = ArrayList<String>()
                if (events != null) {
                    for (i in 0 until events.length()) {
                        listData.add(events.getString(i))
                    }
                }
                for(i in 1..listData.count()) {
                    val item = JSONObject(listData[i-1])
                    val text = item.getString("text")
                    val day = "${item.getString("day")} ${convertMonth(item.getString("month"))}"
                    val time = item.getString("time")
                    val image = item.getString("image")
                    arrEvent.add(Event(text, image, day, time))
                }

                listView.adapter = CustomAdapter(applicationContext, arrEvent)

            } catch (e: Exception) {
            }

        }
    }
}