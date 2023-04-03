package com.example.todolistkotlin

import NotificationHelper
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList

class CustomAdapter(
    private val context: Context,
    private val taskId: ArrayList<String>,
    private val taskTitle: ArrayList<String>,
    private val taskContent: ArrayList<String>,
    private val taskDate: ArrayList<String>,
    private val taskState: ArrayList<String>,
    private val myDB: TaskDbHelper,
    private val notificationHelper: NotificationHelper = NotificationHelper()
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

        checkTaskDate(position)

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

    fun checkTaskDate(position: Int): Boolean {
        val date = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).parse(taskDate[position])
        if (date != null) {
            val days = TimeUnit.DAYS.convert(date.time - System.currentTimeMillis(), TimeUnit.MILLISECONDS)
            if (days < 0) {
                if (taskState[position] != "En retard") {
                    taskState[position] = "En retard"
                    myDB.updateTaskState(taskId[position], "En retard") // Mettre à jour l'état de la tâche dans la base de données
                    // holder.taskState.text = "En retard" // Mettre à jour le texte de l'état de la tâche
                    // holder.taskState.setTextColor(Color.RED) // Changer la couleur du texte de l'état de la tâche
                }
                notificationHelper.showNotification(context, "Tâche en retard", "${taskTitle[position]} est en retard.")
                return true
            } else if (days < 3) {
                notificationHelper.showNotification(context, "Tâche bientôt expirée", "${taskTitle[position]} expire dans $days jour(s).")
                return true
            }
        }
        return false
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
