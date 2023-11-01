package com.example.todoii.adapter

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.todoii.R
import com.example.todoii.databinding.TodoItemBinding
import com.example.todoii.util.ToDoData
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class ToDoAdapter(private val listTasks:MutableList<ToDoData>):RecyclerView.Adapter<ToDoAdapter.ToDoViewHolder>(){
    private var listener :ToDoAdapterClicksInterface?=null
    fun setListener(listener:ToDoAdapterClicksInterface){
        this.listener = listener
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToDoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.todo_item,parent,false)
        return ToDoViewHolder(view)
    }
    override fun getItemCount() = listTasks.size
    override fun onBindViewHolder(holder: ToDoViewHolder, position: Int) {
        val currentTask = listTasks[position]
        holder.binding.apply {
            textViewTaskName.text = currentTask.task
            descriptionTextView.text = currentTask.description
            timeTextView.text = currentTask.timeCreated

            delete.setOnClickListener {
                listener?.onDeleteTask(currentTask)
            }
            update.setOnClickListener {
                listener?.onEditTask(currentTask)
            }
            layout.setOnClickListener{
                if (descriptionTextView.visibility ==View.VISIBLE){
                    descriptionTextView.visibility = View.GONE
                    layout.setBackgroundResource(R.drawable.shape_item_note_clicked)
                }else{
                    descriptionTextView.visibility = View.VISIBLE
                    layout.setBackgroundResource(R.drawable.shape_item_note)
                }
            }
        }

    }
     class ToDoViewHolder(view: View):RecyclerView.ViewHolder(view){
        val binding = TodoItemBinding.bind(view)
    }

    interface ToDoAdapterClicksInterface{
        fun onDeleteTask(task:ToDoData)
        fun onEditTask(task:ToDoData)
    }
}