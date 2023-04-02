package com.example.todolistkotlin

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CalendarView
import android.widget.CalendarView.OnDateChangeListener
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.*

class Calendar : AppCompatActivity() {
    private lateinit var date: TextView
    private lateinit var calendarView: CalendarView
    private lateinit var saveButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar)

        date = findViewById(R.id.idDate)
        calendarView = findViewById(R.id.calendarView)

        // Initialisation de la variable date avec la date du jour
        val currentDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
        date.text = currentDate

        calendarView.setOnDateChangeListener(
            OnDateChangeListener { view, year, month, dayOfMonth ->
                val date = (dayOfMonth.toString() + "/"
                        + (month + 1) + "/" + year)

                this.date.text = date
            })

        saveButton = findViewById(R.id.saveButtonCalendar)
        saveButton.setOnClickListener {
            val selectedDate = date.text.toString()
            val intent = Intent()
            intent.putExtra("selectedDate", selectedDate)
            setResult(RESULT_OK, intent)
            finish()
        }

    }
}