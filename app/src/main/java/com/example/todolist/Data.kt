package com.example.todolist

import java.text.SimpleDateFormat
import java.util.*

class Data(date : String) {

    private val mCalendar: Calendar = Calendar.getInstance()

    init {
        val array = date.split(",")

        mCalendar.set(Calendar.YEAR, array[0].toInt())
        mCalendar.set(Calendar.MONTH, array[1].toInt())
        mCalendar.set(Calendar.DAY_OF_MONTH, array[2].toInt())
        mCalendar.set(Calendar.HOUR_OF_DAY, array[3].toInt())
        mCalendar.set(Calendar.MINUTE, array[4].toInt())
    }

    fun getDateString() : String{
        /*
        0 - Год
        1 - Месяц
        2 - День
        3 - час
        4 - минута
         */
        return String.format("Время: %s\nДата: %s",
            SimpleDateFormat("dd MMM yyyy",Locale.getDefault()).format(mCalendar.time),
            SimpleDateFormat("H:mm", Locale.getDefault()).format(mCalendar.time))
    }
    fun getDate() : Calendar {
        /*
        0 - Год
        1 - Месяц
        2 - День
        3 - час
        4 - минута
         */
        return mCalendar
    }
}