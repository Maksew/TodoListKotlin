package com.example.todolistkotlin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import java.text.SimpleDateFormat
import java.util.*

class AddActivities : AppCompatActivity() {

    private lateinit var titleInput: EditText
    private lateinit var contentInput: EditText
    private lateinit var saveButton: Button
    private lateinit var calendarView: Button
    private var date: String = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
    private var taskState: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)

        titleInput = findViewById(R.id.titleInput)
        contentInput = findViewById(R.id.contentInput)
        calendarView = findViewById(R.id.calendar)
        val dateTextView = findViewById<TextView>(R.id.dateTextView)

        val taskId = intent.getStringExtra("id")
        val taskTitle = intent.getStringExtra("title")
        val taskContent = intent.getStringExtra("content")
        val taskDate = intent.getStringExtra("date")
        taskState = intent.getStringExtra("state") ?: ""

        titleInput.setText(taskTitle)
        contentInput.setText(taskContent)
        if (taskDate != null) {
            date = taskDate
            dateTextView.text = date
        } else {
            dateTextView.text = date
        }

        // Ouverture du calendrier
        calendarView.setOnClickListener {
            val intent = Intent(this@AddActivities, Calendar::class.java)
            startActivityForResult(intent, 1)
        }

        // Enregistrement de la tâche
        saveButton = findViewById(R.id.saveButton)
        saveButton.setOnClickListener {
            val myDB = TaskDbHelper(this)
            val lastId = myDB.getLastId()
            val modifiedAt = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault()).format(Date())

            // Déterminer l'état de la tâche en fonction de la date
            taskState = determineTaskState(date)

            myDB.addTask(
                Task(
                    lastId + 1, // ID de la tâche
                    titleInput.text.toString().trim(), // Titre de la tâche
                    contentInput.text.toString().trim(), // Description de la tâche
                    date, // Date de la tâche
                    taskState, // Etat de la tâche
                    modifiedAt // date de modification de la tâche
                )
            )
            val intent = Intent(this@AddActivities, MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun determineTaskState(taskDate: String): String {
        val currentDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
        val taskDateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val currentDateTime = taskDateFormat.parse(currentDate) ?: return "À faire"
        val taskDateTime = taskDateFormat.parse(taskDate) ?: return "À faire"

        return if (taskDateTime.before(currentDateTime)) {
            "En retard"
        } else {
            "À faire"
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Si une date a été sélectionnée dans le calendrier, on l'affiche
        if (requestCode == 1 && resultCode == RESULT_OK) {
            val selectedDate = data?.getStringExtra("selectedDate")
            val dateTextView = findViewById<TextView>(R.id.dateTextView)
            if (selectedDate != null) {
                date = selectedDate
                dateTextView.text = date
            }
        }
    }
}


