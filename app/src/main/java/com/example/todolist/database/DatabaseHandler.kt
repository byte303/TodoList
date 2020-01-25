package com.example.todolist.database

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHandler(context: Context) :
    SQLiteOpenHelper(context,
        DATABASE_NAME, null,
        DATABASE_VERSION
    ), IDatabaseHandler {

    override fun onCreate(db: SQLiteDatabase) {
        onCreateTodo(db)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_TODO")

        onCreate(db)
    }
    private fun onCreateTodo(db: SQLiteDatabase){
        val createNotesTable = ("CREATE TABLE " + TABLE_TODO + "("
                + "$KEY_ID_TODO INTEGER PRIMARY KEY,"
                + "$KEY_TITLE TEXT,"
                + "$KEY_DATE TEXT,"
                + "$KEY_DESCRIPTION TEXT)")
        db.execSQL(createNotesTable)
    }

    override val allTodo: ArrayList<Todo>
        @SuppressLint("Recycle")
        get() {
            val todoList = ArrayList<Todo>()
            val selectQuery = "SELECT  * FROM $TABLE_TODO"

            val db = this.writableDatabase
            val cursor = db.rawQuery(selectQuery, null)

            if (cursor.moveToFirst()) {
                do {
                    val todoTable = Todo()
                    todoTable.setID(Integer.parseInt(cursor.getString(0)))
                    todoTable.setTitle(cursor.getString(1))
                    todoTable.setDate(cursor.getString(2))
                    todoTable.setDescription(cursor.getString(3))
                    todoList.add(todoTable)
                } while (cursor.moveToNext())
            }

            return todoList
        }

    override fun addTodo(todo: Todo) {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(KEY_TITLE, todo.getTitle())
        values.put(KEY_DATE, todo.getDate())
        values.put(KEY_DESCRIPTION, todo.getDescription())

        db.insert(TABLE_TODO, null, values)
        db.close()
    }

    @SuppressLint("Recycle")
    override fun getTodo(id: Int): Todo {
        val db = this.readableDatabase

        val cursor = db.query(
            TABLE_TODO, arrayOf(
                KEY_ID_TODO,
                KEY_TITLE,
                KEY_DATE,
                KEY_DESCRIPTION
            ), "$KEY_ID_TODO=?",
            arrayOf(id.toString()), null, null, null, null
        )

        cursor?.moveToFirst()

        return Todo(
            Integer.parseInt(cursor!!.getString(0)),
            cursor.getString(1),
            cursor.getString(2),
            cursor.getString(3)
        )
    }

    override fun updateTodo(todo: Todo): Int {
        val db = this.writableDatabase

        val values = ContentValues()
        values.put(KEY_TITLE, todo.getTitle())
        values.put(KEY_DATE, todo.getDate())
        values.put(KEY_DESCRIPTION, todo.getDescription())

        return db.update(
            TABLE_TODO, values, "$KEY_ID_TODO = ?",
            arrayOf(java.lang.String.valueOf(todo.getID()))
        )
    }

    override fun deleteTodo(todo: Todo) {
        val db = this.writableDatabase
        db.delete(TABLE_TODO, "$KEY_ID_TODO = ?", arrayOf(java.lang.String.valueOf(todo.getID())))
        db.close()
    }


    companion object {

        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "DataBase.db"
        // TO_DO
        private const val TABLE_TODO = "Todo"
        private const val KEY_ID_TODO = "id"
        private const val KEY_TITLE = "title"
        private const val KEY_DATE = "date"
        private const val KEY_DESCRIPTION = "description"
    }
}