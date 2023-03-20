package com.example.todolistkotlin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText

class AddActivities : AppCompatActivity() {

    private lateinit var titleInput: EditText
    private lateinit var contentInput: EditText
    private lateinit var saveButton: Button
    private lateinit var calendarView: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)

        titleInput = findViewById(R.id.titleInput)
        contentInput = findViewById(R.id.contentInput)
        calendarView = findViewById(R.id.calendar)
        calendarView.setOnClickListener {
            val intent = Intent(this@AddActivities, Calendar::class.java)
            startActivity(intent)
        }
        saveButton = findViewById(R.id.saveButton)
        saveButton.setOnClickListener {
            val myDB = TaskDbHelper(this)
            val lastId = myDB.getLastId()
            myDB.addTask(Task(lastId + 1, titleInput.text.toString().trim(),
                contentInput.text.toString().trim()))
        }
    }
}