package com.example.todolist

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.todolist.database.Todo


class MainActivity : AppCompatActivity(),
    TodoListFragment.onTodoInterface,
    CreateTodoFragment.onTodoInterface,
    AdapterListTodo.IAdapterTodo{

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction().replace(
                R.id.fragment_container,
                TodoListFragment()
            ).commit()
        }
    }

    override fun onCloseTodo() {
        supportFragmentManager.beginTransaction().replace(
            R.id.fragment_container,
            CreateTodoFragment()
        ).commit()
    }

    override fun onOpenMain() {
        supportFragmentManager.beginTransaction().replace(
            R.id.fragment_container,
            TodoListFragment()
        ).commit()
    }

    override fun onEditTodo(todo: Todo) {
        val mArg = Bundle()
        mArg.putInt("tag_fragment", todo.getID())
        val mFrg: Fragment = CreateTodoFragment()
        mFrg.arguments = mArg
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, mFrg).commit()
    }
}
