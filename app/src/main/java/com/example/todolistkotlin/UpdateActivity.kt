package com.example.todolistkotlin

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.*

class UpdateActivity : AppCompatActivity() {

    private lateinit var titleInput: EditText
    private lateinit var contentInput: EditText
    private lateinit var dateTextView: TextView
    private lateinit var calendar: Button
    private lateinit var updateButton: Button
    private lateinit var deleteButton: Button
    private lateinit var date: String
    private lateinit var taskState: String
    private lateinit var markAsDoneButton: Button

    private var id: String = ""
    private var title: String = ""
    private var content: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update)

        titleInput = findViewById(R.id.titleInput2)
        contentInput = findViewById(R.id.contentInput2)
        dateTextView = findViewById(R.id.dateTextView2)
        updateButton = findViewById(R.id.updateButton)
        calendar = findViewById(R.id.calendar2)
        deleteButton = findViewById(R.id.deleteButton)
        markAsDoneButton = findViewById(R.id.markAsDoneButton) // Ajouter cette ligne pour le bouton "Fait"

        calendar.setOnClickListener {
            val intent = Intent(this@UpdateActivity, Calendar::class.java)
            startActivityForResult(intent, 1)
        }

        getAndSetIntentData()

        supportActionBar?.title = title

        updateButton.setOnClickListener {
            val myDB = TaskDbHelper(this)
            title = titleInput.text.toString().trim()
            content = contentInput.text.toString().trim()
            myDB.updateData(id, title, content, date, taskState)
            val intent = Intent(this@UpdateActivity, MainActivity::class.java)
            startActivity(intent)
        }

        // Ajouter ce bloc pour gérer le clic sur le bouton "Fait"
        markAsDoneButton.setOnClickListener {
            val myDB = TaskDbHelper(this)
            title = titleInput.text.toString().trim()
            content = contentInput.text.toString().trim()
            taskState = "Fait"
            myDB.updateData(id, title, content, date, taskState)
            val intent = Intent(this@UpdateActivity, MainActivity::class.java)
            startActivity(intent)
        }

        deleteButton.setOnClickListener {
            confirmationText(this, title, id)
        }
    }


    private fun getAndSetIntentData() {
        if (intent.hasExtra("id") && intent.hasExtra("title") && intent.hasExtra("content") && intent.hasExtra("date") && intent.hasExtra("state")) {
            // Getting Data from Intent
            id = intent.getStringExtra("id") ?: ""
            title = intent.getStringExtra("title") ?: ""
            content = intent.getStringExtra("content") ?: ""
            date = intent.getStringExtra("date") ?: ""
            taskState = intent.getStringExtra("state") ?: ""

            // Setting Intent Data
            titleInput.setText(title)
            contentInput.setText(content)
            dateTextView.text = date
            Log.d("stev", "$title $content $date")
        } else {
            Toast.makeText(this, "Pas de données.", Toast.LENGTH_SHORT).show()
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
            if (selectedDate != null) {
                date = selectedDate
                dateTextView.text = date

                // Mettre à jour l'état de la tâche en fonction de la nouvelle date
                taskState = determineTaskState(date)
            }
        }
    }

    fun confirmationText(context: Context, title: String, row_id: String) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Supprimer $title ?")
        builder.setMessage("Etes vous sur de vouloir supprimer $title ?")
        builder.setPositiveButton("Oui") { dialog, which ->
            val myDB = TaskDbHelper(context)
            myDB.deleteTask(context as Activity, row_id)
            context.startActivity(Intent(context, MainActivity::class.java))
            finish()
        }
        builder.setNegativeButton("Non") { dialog, which ->
            dialog.dismiss()
        }
        builder.show()
    }
}
