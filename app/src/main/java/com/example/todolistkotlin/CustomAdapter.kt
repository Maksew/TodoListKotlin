package com.example.todolistkotlin

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CustomAdapter(private val context: Context, private val taskId: ArrayList<String>,
                    private val taskTitle: ArrayList<String>,
                    private val taskContent: ArrayList<String>) : RecyclerView.Adapter<CustomAdapter.MyViewHolder>() {

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var taskId_txt: TextView = itemView.findViewById(R.id.taskId_txt)
        var taskTitle_txt: TextView = itemView.findViewById(R.id.taskTitle_txt)
        var taskContent_txt: TextView = itemView.findViewById(R.id.taskContent_txt)
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
        holder.taskId_txt.text = taskId[position]
        holder.taskTitle_txt.text = taskTitle[position]
        holder.taskContent_txt.text = taskContent[position]
    }
}
