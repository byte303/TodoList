package com.example.todolist

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.database.DatabaseHandler
import com.example.todolist.database.Todo

class AdapterListTodo(
    private val context : Context,
    private val array : ArrayList<Todo>) : RecyclerView.Adapter<AdapterListTodo.ViewHolders>()  {

    private var listener: IAdapterTodo

    init {
        listener = context as IAdapterTodo
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolders {
        return ViewHolders(
            LayoutInflater.from(context).inflate(
                R.layout.custom_list_todo,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return array.count()
    }

    override fun onBindViewHolder(holder: ViewHolders, position: Int) {
        holder.txtDate.text = Data(array[position].getDate()).getDateString()
        holder.txtTitle.text = array[position].getTitle()
        holder.txtDescription.text = array[position].getDescription()
        holder.linear.setOnClickListener {
            listener.onEditTodo(array[position])
        }
        holder.imgDelete.setOnClickListener {
            val dialog = AlertDialog.Builder(context)
            dialog.setMessage("Вы действительно хотите удалить данную задачу?")
            dialog.setTitle("Удаление")
            dialog.setPositiveButton("Удалить"){ _, _ ->
                val db = DatabaseHandler(context)

                val todo = db.getTodo(array[position].getID())

                db.deleteTodo(todo)
                array.removeAt(position)
                notifyItemRemoved(position)
            }
            dialog.setNegativeButton("Отмена"){_,_ -> }
            dialog.show()

        }
    }

    class ViewHolders (view: View) : RecyclerView.ViewHolder(view) {
        val txtTitle : TextView =  view.findViewById(R.id.txtTitle)
        val txtDescription : TextView =  view.findViewById(R.id.txtSecond)
        val txtDate : TextView =  view.findViewById(R.id.txtDate)
        val linear : RelativeLayout = view.findViewById(R.id.linear)
        val imgDelete : ImageView = view.findViewById(R.id.imgDelete)
    }

    internal interface IAdapterTodo{
        fun onEditTodo(todo : Todo)
    }
}