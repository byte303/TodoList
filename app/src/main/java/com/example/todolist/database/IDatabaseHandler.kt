package com.example.todolist.database

interface IDatabaseHandler {
    // Notes
    val allTodo: List<Todo>
    fun addTodo(todo: Todo)
    fun getTodo(id: Int): Todo
    fun updateTodo(todo: Todo): Int
    fun deleteTodo(todo: Todo)
}