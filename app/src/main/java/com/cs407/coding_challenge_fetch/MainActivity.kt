package com.cs407.coding_challenge_fetch

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = ItemAdapter()
        recyclerView.adapter = adapter

        fetchData()
    }

    private fun fetchData() {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val url = URL("https://fetch-hiring.s3.amazonaws.com/hiring.json")
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "GET"
                connection.connect()

                if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                    val stream = connection.inputStream.bufferedReader().use { it.readText() }
                    val itemType = object : TypeToken<List<Item>>() {}.type
                    val items: List<Item> = Gson().fromJson(stream, itemType)

                    // Process data: Filter out empty names, sort by listId then name
                    val filteredAndSortedItems = items.filter { !it.name.isNullOrBlank() }
                        .sortedWith(compareBy({ it.listId }, { it.name }))

                    withContext(Dispatchers.Main) {
                        adapter.setData(filteredAndSortedItems)
                    }
                }
            } catch (e: Exception) {
                Log.e("MainActivity", "Error fetching data", e)
            }
        }
    }
}