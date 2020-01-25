package com.example.todolist

import android.app.Activity
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todolist.database.DatabaseHandler
import com.example.todolist.database.Todo
import kotlinx.android.synthetic.main.fragment_todo_list.view.*


class TodoListFragment : Fragment() {

    interface onTodoInterface {
        fun onCloseTodo()
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
        val root = inflater.inflate(R.layout.fragment_todo_list, container, false)

        root.fabMain.setOnClickListener {
            todoEventListener!!.onCloseTodo()
        }
        val activity = activity as AppCompatActivity
        activity.setSupportActionBar(root.mainToolbar)
        setHasOptionsMenu(true)

        val db = DatabaseHandler(context!!)

        root.listMain.layoutManager = LinearLayoutManager(context)
        root.listMain.adapter = AdapterListTodo(context!!,db.allTodo)

        return root
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_toolbar_main, menu)
    }
    var typeSort = false

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId === R.id.action_sort) {
            val db = DatabaseHandler(context!!)
            if(!typeSort) SortAscending(db.allTodo)
            else SortDescending(db.allTodo)
        }
        return true
    }

    private fun SortAscending(array: ArrayList<Todo>) {
        for (i in 1 until array.size) {
            val current = array[i]
            var j = i - 1
            while (j >= 0 && Data(current.getDate()).getDate() < Data(array[j].getDate()).getDate()) {
                array[j + 1] = array[j]
                j--
            }
            array[j + 1] = current
        }
        typeSort = true
        view!!.listMain.adapter = AdapterListTodo(context!!,array)
    }
    private fun SortDescending(array: ArrayList<Todo>) {
        for (i in 1 until array.size) {
            val current = array[i]
            var j = i - 1
            while (j >= 0 && Data(current.getDate()).getDate() > Data(array[j].getDate()).getDate()) {
                array[j + 1] = array[j]
                j--
            }
            array[j + 1] = current
        }
        typeSort = false
        view!!.listMain.adapter = AdapterListTodo(context!!,array)
    }
}
