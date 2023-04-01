package com.example.todolistkotlin

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class UpdateActivity : AppCompatActivity() {

    private lateinit var titleInput: EditText
    private lateinit var contentInput: EditText
    private lateinit var dateTextView: TextView
    private lateinit var calendar: Button
    private lateinit var updateButton: Button
    private lateinit var deleteButton: Button

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

        calendar.setOnClickListener {
            val intent = Intent(this@UpdateActivity, Calendar::class.java)
            startActivity(intent)
        }

        getAndSetIntentData()

        supportActionBar?.title = title

        updateButton.setOnClickListener {
            val myDB = TaskDbHelper(this)
            title = titleInput.text.toString().trim()
            content = contentInput.text.toString().trim()
            myDB.updateData(id, title, content)
            val intent = Intent(this@UpdateActivity, MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun getAndSetIntentData() {
        if (intent.hasExtra("id") && intent.hasExtra("title") && intent.hasExtra("content")) {
            // Getting Data from Intent
            id = intent.getStringExtra("id") ?: ""
            title = intent.getStringExtra("title") ?: ""
            content = intent.getStringExtra("content") ?: ""

            // Setting Intent Data
            titleInput.setText(title)
            contentInput.setText(content)
            Log.d("stev", "$title $content")

        } else {
            Toast.makeText(this, "No data.", Toast.LENGTH_SHORT).show()
        }
    }
}
