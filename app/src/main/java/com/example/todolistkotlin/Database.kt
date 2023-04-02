package com.example.todolistkotlin

import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast
import java.text.SimpleDateFormat
import java.util.*

data class Task(var id: Int, var title: String, var content: String, var date: String, var state: String, var modifiedAt: String)


class TaskDbHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        const val DATABASE_NAME = "task_database"
        const val DATABASE_VERSION = 5
        const val TABLE_NAME = "task_table"
        const val COLUMN_ID = "id"
        const val COLUMN_TITLE = "title"
        const val COLUMN_CONTENT = "content"
        const val COLUMN_DATE = "date"
        const val COLUMN_STATE = "state"
        const val COLUMN_MODIFIED_AT = "modified_at"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTableSql =
            "CREATE TABLE $TABLE_NAME ($COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, $COLUMN_TITLE TEXT, $COLUMN_CONTENT TEXT, $COLUMN_DATE TEXT, $COLUMN_STATE TEXT, $COLUMN_MODIFIED_AT TEXT)"
        db?.execSQL(createTableSql)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun addTask(task: Task)  {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_ID, task.id)
            put(COLUMN_TITLE, task.title)
            put(COLUMN_CONTENT, task.content)
            put(COLUMN_DATE, task.date)
            put(COLUMN_STATE, task.state)
            put(COLUMN_MODIFIED_AT, task.modifiedAt)
        }
        val result = db.insert(TABLE_NAME, null, values)
        db.close()

        if (result == -1L) {
            println("Echec de l'ajout de la tâche")
        } else {
            println("Tâche ajoutée avec succès")
        }
    }

    fun readAllData(): Cursor? {
        val query = "SELECT * FROM $TABLE_NAME"
        val myDB = this.readableDatabase

        var cursor: Cursor? = null
        myDB?.let {
            cursor = it.rawQuery(query, null)
        }
        return cursor
    }

    fun getLastId(): Int {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT MAX($COLUMN_ID) FROM $TABLE_NAME", null)
        var lastId = 0
        if (cursor.moveToFirst()) {
            lastId = cursor.getInt(0)
        }
        cursor.close()
        return lastId
    }

    fun updateData(
        id: String,
        title: String,
        content: String,
        date: String,
        state: String
    ): Boolean {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_TITLE, title)
            put(COLUMN_CONTENT, content)
            put(COLUMN_DATE, date)
            put(COLUMN_STATE, state)
            put(
                COLUMN_MODIFIED_AT,
                SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault()).format(Date())
            )
        }
        val result = db.update(TABLE_NAME, values, "$COLUMN_ID=?", arrayOf(id))
        db.close()
        return result != -1
    }

    fun deleteTask(activity: Activity, row_id: String) {
        val db = this.writableDatabase
        val result = db.delete(TABLE_NAME, "$COLUMN_ID=?", arrayOf(row_id))
        if (result == -1) {
            Toast.makeText(activity, "Echec de la suppression", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(activity, "Supprimé avec succès", Toast.LENGTH_SHORT).show()
            activity.finish()
        }
    }
}
