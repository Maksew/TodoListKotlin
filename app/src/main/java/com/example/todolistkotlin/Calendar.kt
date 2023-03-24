package com.example.todolistkotlin

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CalendarView
import android.widget.CalendarView.OnDateChangeListener
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class Calendar : AppCompatActivity() {
    private lateinit var date: TextView
    private lateinit var calendarView: CalendarView
    private lateinit var saveButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar)

        date = findViewById(R.id.idDate)
        calendarView = findViewById(R.id.calendarView)

        calendarView.setOnDateChangeListener(
            OnDateChangeListener { view, year, month, dayOfMonth ->
                val Date = (dayOfMonth.toString() + "-"
                        + (month + 1) + "-" + year)

                date.setText(Date)
            })

        saveButton = findViewById(R.id.saveButtonCalendar)
        saveButton.setOnClickListener {
            val selectedDate = date.text.toString()
            val intent = Intent(this@Calendar, AddActivities::class.java)
            intent.putExtra("selectedDate", selectedDate)
            startActivity(intent)
        }
    }
}