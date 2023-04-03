package com.example.todolistkotlin


import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var addButton: FloatingActionButton
    private lateinit var sortButton: Button
    private lateinit var myDB: TaskDbHelper
    private var taskId = ArrayList<String>()
    private var taskTitle = ArrayList<String>()
    private var taskContent = ArrayList<String>()
    private var taskDate = ArrayList<String>()
    private var taskState = ArrayList<String>()

    private lateinit var customAdapter: CustomAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerView)
        addButton = findViewById(R.id.addbutton)
        sortButton = findViewById(R.id.sortButton)

        sortButton.setOnClickListener {
            sortTasksByState()
        }

        addButton.setOnClickListener {
            val intent = Intent(this@MainActivity, AddActivities::class.java)
            startActivity(intent)
        }

        myDB = TaskDbHelper(this)
        stockData()

        customAdapter = CustomAdapter(this, taskId, taskTitle, taskContent, taskDate, taskState, myDB)
        recyclerView.adapter = customAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)
    }

    private fun stockData() {
        val cursor = myDB.readAllData()
        if (cursor != null) {
            if (cursor.count == 0) {
                Toast.makeText(this, "Pas de données", Toast.LENGTH_SHORT).show()
            } else {
                while (cursor.moveToNext()) {
                    taskId.add(cursor.getString(0))
                    taskTitle.add(cursor.getString(1))
                    taskContent.add(cursor.getString(2))
                    taskDate.add(cursor.getString(3))
                    taskState.add(cursor.getString(4))
                }
            }
        }
    }

    private var sortState = "default"

    private fun sortTasksByState() {
        when (sortState) {
            "default" -> {
                taskState.sort()
                sortState = "asc"
            }
            "asc" -> {
                taskState.sortByDescending { it == "À faire" }
                sortState = "desc"
            }
            "desc" -> {
                taskState.sortByDescending { it == "Fait" }
                sortState = "default"
            }
        }
        customAdapter.notifyDataSetChanged()
    }

    override fun onResume() {
        super.onResume()
        checkAllTasks()
    }

    private fun checkAllTasks() {
        for (position in taskId.indices) {
            customAdapter.checkTaskDate(position)
        }
    }


}

