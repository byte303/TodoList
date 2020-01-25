package com.example.todolist.database

class Todo {
    var id: Int = 0
    private lateinit var date: String
    private lateinit var title: String
    private lateinit var description:String

    constructor ()

    constructor (id: Int, title: String, date:String,description:String) {
        this.id = id
        this.date = date
        this.title = title
        this.description = description
    }

    constructor (title:String,date: String, description:String) {
        this.date = date
        this.title = title
        this.description = description
    }

    fun getID(): Int {
        return this.id
    }

    fun setID(id: Int) {
        this.id = id
    }

    fun getDate() : String{
        return this.date
    }
    fun setDate(date : String){
        this.date = date
    }

    fun getTitle() : String{
        return this.title
    }
    fun setTitle(title : String){
        this.title = title
    }

    fun getDescription() : String{
        return this.description
    }
    fun setDescription(description : String){
        this.description = description
    }
}