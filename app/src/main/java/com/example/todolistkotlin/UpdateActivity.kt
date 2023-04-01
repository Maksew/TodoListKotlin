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
        deleteButton = findViewById(R.id.deleteButton)

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
            finish()
        }

        deleteButton.setOnClickListener {
            confirmationText(this, title, id)
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
            Toast.makeText(this, "Pas de données.", Toast.LENGTH_SHORT).show()
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