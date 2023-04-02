package com.example.todolistkotlin

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CustomAdapter(
    private val context: Context,
    private val taskId: ArrayList<String>,
    private val taskTitle: ArrayList<String>,
    private val taskContent: ArrayList<String>,
    private val taskDate: ArrayList<String>,
    private val taskState: ArrayList<String>
) : RecyclerView.Adapter<CustomAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.row, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.taskTitle.text = taskTitle[position]
        holder.taskContent.text = taskContent[position]
        holder.taskDate.text = taskDate[position]
        holder.taskState.text = taskState[position]

        // Ajout de la gestion des couleurs en fonction de l'état
        when (taskState[position]) {
            "À faire" -> holder.taskState.setTextColor(Color.parseColor("#90EE90")) // Vert clair
            "En retard" -> holder.taskState.setTextColor(Color.RED)
            "Réalisée" -> holder.taskState.setTextColor(Color.GRAY)
            else -> holder.taskState.setTextColor(Color.BLACK)
        }


        holder.mainLayout.setOnClickListener {
            val intent = Intent(context, UpdateActivity::class.java)
            intent.putExtra("id", taskId[position])
            intent.putExtra("title", taskTitle[position])
            intent.putExtra("content", taskContent[position])
            intent.putExtra("date", taskDate[position])
            intent.putExtra("state", taskState[position])
            context.startActivity(intent)
        }
    }




    override fun getItemCount(): Int {
        return taskId.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val taskTitle: TextView = itemView.findViewById(R.id.taskTitle_txt)
        val taskContent: TextView = itemView.findViewById(R.id.taskContent_txt)
        val taskDate: TextView = itemView.findViewById(R.id.date_txt)
        val taskState: TextView = itemView.findViewById(R.id.taskState_txt)
        val mainLayout: LinearLayout = itemView.findViewById(R.id.mainLayout)
    }


}
