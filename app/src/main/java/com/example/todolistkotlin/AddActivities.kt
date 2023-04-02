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
    private lateinit var date: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)

        // Récupération des vues
        titleInput = findViewById(R.id.titleInput)
        contentInput = findViewById(R.id.contentInput)
        calendarView = findViewById(R.id.calendar)
        val dateTextView = findViewById<TextView>(R.id.dateTextView)

        // Récupérer les données de l'intention
        val taskId = intent.getStringExtra("id")
        val taskTitle = intent.getStringExtra("title")
        val taskContent = intent.getStringExtra("content")
        val taskDate = intent.getStringExtra("date")

        // Mettre à jour les vues avec les données récupérées
        titleInput.setText(taskTitle)
        contentInput.setText(taskContent)
        if (taskDate != null) {
            date = taskDate
            dateTextView.text = date
        } else {
            date = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
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
            myDB.addTask(
                Task(
                    lastId + 1, // ID de la tâche
                    titleInput.text.toString().trim(), // Titre de la tâche
                    contentInput.text.toString().trim(), // Description de la tâche
                    date // Date de la tâche
                )
            )
            val intent = Intent(this@AddActivities, MainActivity::class.java)
            startActivity(intent)
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