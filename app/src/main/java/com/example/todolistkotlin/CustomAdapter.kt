package com.example.todolistkotlin

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CustomAdapter(private val context: Context, private val taskId: ArrayList<String>,
                    private val taskTitle: ArrayList<String>,
                    private val taskContent: ArrayList<String>,
                    private val taskDate: ArrayList<String>) : RecyclerView.Adapter<CustomAdapter.MyViewHolder>() {

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var taskTitle_txt: TextView = itemView.findViewById(R.id.taskTitle_txt)
        var taskContent_txt: TextView = itemView.findViewById(R.id.taskContent_txt)
        var taskDate_txt: TextView = itemView.findViewById(R.id.taskDate_txt)
        var mainLayout: LinearLayout = itemView.findViewById(R.id.mainLayout)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.row, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return taskId.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.taskTitle_txt.text = taskTitle[position]
        holder.taskContent_txt.text = taskContent[position]
        holder.taskDate_txt.text = taskDate[position]

        val dbHelper = TaskDbHelper(context)
        val cursor = dbHelper.readAllData()
        if (cursor != null && cursor.moveToFirst()) {
            cursor.moveToPosition(position)
            holder.taskDate_txt.text = cursor.getString(cursor.getColumnIndexOrThrow(TaskDbHelper.COLUMN_DATE))
        }
        cursor?.close()

        holder.mainLayout.setOnClickListener {
            val intent = Intent(context, UpdateActivity::class.java)
            intent.putExtra("id", taskId[position].toString())
            intent.putExtra("title", taskTitle[position].toString())
            intent.putExtra("content", taskContent[position].toString())
            context.startActivity(intent)
        }
    }

}