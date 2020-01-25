package com.example.todolist

import android.app.Activity
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todolist.database.DatabaseHandler
import com.example.todolist.database.Todo
import kotlinx.android.synthetic.main.fragment_todo_list.view.*


class TodoListFragment : Fragment(){

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

        root.listMain.layoutManager = LinearLayoutManager(context)
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val db = DatabaseHandler(context!!)
        sortInserting(db.allTodo)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_toolbar_main, menu)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId === R.id.action_sort) {
            val db = DatabaseHandler(context!!)
            if(checkSort) upendArray(db.allTodo)
            else sortInserting(db.allTodo)
        }
        return true
    }
    private var checkSort = false
    private fun sortInserting(array: ArrayList<Todo>) {
        for (i in 1 until array.size) {
            val current = array[i]
            var j = i - 1
            while (j >= 0 && Data(current.getDate()).getDate() < Data(array[j].getDate()).getDate()) {
                array[j + 1] = array[j]
                j--
            }
            array[j + 1] = current
        }
        checkSort = true
        view!!.listMain.adapter = AdapterListTodo(context!!,array)
    }

    private fun upendArray(massive: ArrayList<Todo>) {
        for (i in 0 until massive.size / 2) {
            val tmp = massive[i]
            massive[i] = massive[massive.size - i - 1]
            massive[massive.size - i - 1] = tmp
        }
        checkSort = false
        view!!.listMain.adapter = AdapterListTodo(context!!,massive)
    }
}
