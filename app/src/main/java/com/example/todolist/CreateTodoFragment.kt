package com.example.todolist

import android.R.attr.defaultValue
import android.R.attr.key
import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.TimePicker
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.example.todolist.database.DatabaseHandler
import com.example.todolist.database.Todo
import kotlinx.android.synthetic.main.fragment_create_todo.*
import kotlinx.android.synthetic.main.fragment_create_todo.view.*
import java.text.SimpleDateFormat
import java.util.*


class CreateTodoFragment : Fragment(),
    DatePickerDialog.OnDateSetListener,
    TimePickerDialog.OnTimeSetListener{

    private var mCalendar: Calendar = Calendar.getInstance()
    private var todoId = -1


    interface onTodoInterface {
        fun onOpenMain()
    }

    var todoEventListener: onTodoInterface? = null

    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        todoEventListener = try {
            activity as onTodoInterface
        } catch (e: ClassCastException) {
            throw ClassCastException("$activity must implement onTodoInterface")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_create_todo, container, false)

        val db = DatabaseHandler(context!!)

        root.toolbarTodo.setNavigationOnClickListener {
            todoEventListener!!.onOpenMain()
        }

        root.btnDate.setOnClickListener {
            TimePickerDialog(
                context!!,
                this,
                mCalendar.get(Calendar.HOUR_OF_DAY),
                mCalendar.get(Calendar.MINUTE),
                true
            ).show()
        }

        if(arguments != null){
            todoId = arguments!!.getInt("tag_fragment", -1)
            val todo = db.getTodo(todoId)

            root.edtTitle.setText(todo.getTitle())
            root.btnDate.text = Data(todo.getDate()).getDateString()
            root.edtSecond.setText(todo.getDescription())

            mCalendar = Data(todo.getDate()).getDate()

            root.toolbarTodo.title = "Редактирование задачи"
        }

        root.btnSave.setOnClickListener {
            if(root.edtSecond.text.trim().toString() != "" && root.edtTitle.text.trim().toString() != ""){
                if (mCalendar.time > Date()) {

                    val date = String.format("%s,%s,%s,%s,%s",
                        mCalendar.get(Calendar.YEAR),
                        mCalendar.get(Calendar.MONTH),
                        mCalendar.get(Calendar.DAY_OF_MONTH),
                        mCalendar.get(Calendar.HOUR_OF_DAY),
                        mCalendar.get(Calendar.MINUTE))

                    if(todoId == -1) {

                        db.addTodo(
                            Todo(
                                root.edtTitle.text.toString(),
                                date,
                                root.edtSecond.text.toString()
                            )
                        )
                        Toast.makeText(context!!, "Вы успешно создали задачу!", Toast.LENGTH_SHORT)
                            .show()
                        todoEventListener!!.onOpenMain()
                    }else{
                        db.updateTodo(
                            Todo(
                                todoId,
                                root.edtTitle.text.toString(),
                                date,
                                root.edtSecond.text.toString()
                            )
                        )
                        Toast.makeText(context!!, "Вы успешно изменили задачу!", Toast.LENGTH_SHORT).show()
                        todoEventListener!!.onOpenMain()
                    }
                }else
                    Toast.makeText(context,"Вы неверно ввели дату!",Toast.LENGTH_SHORT).show()
            }else{
                val dialog = AlertDialog.Builder(context!!)
                dialog.setTitle(R.string.text_error)
                dialog.setMessage(R.string.text_error_empty)
                dialog.setPositiveButton(R.string.text_close){ _, _ -> }

                dialog.show()
            }
        }

        return root
    }
    override fun onTimeSet(p0: TimePicker?, hourOfDay: Int, minute: Int) {
        mCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
        mCalendar.set(Calendar.MINUTE, minute)

        btnDate.text = SimpleDateFormat("H:mm", Locale.getDefault()).format(mCalendar.time)

        DatePickerDialog(
            context!!, this, mCalendar.get(Calendar.YEAR),
            mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }
    override fun onDateSet(p0: DatePicker?, year: Int, monthOfYear: Int, dayOfMonth: Int) {
        mCalendar.set(Calendar.YEAR, year)
        mCalendar.set(Calendar.MONTH, monthOfYear)
        mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

        if (mCalendar.time < Date()){
            Toast.makeText(
                context,
                "Дата не может быть в прошлом!",
                Toast.LENGTH_LONG
            ).show()
            Calendar.getInstance()
        }
        else
            btnDate.text = java.lang.String.format("Время: %s\nДата:%s",
                btnDate.text,
                SimpleDateFormat("dd MMM yyyy",Locale.getDefault()).format(mCalendar.time))
    }
}
