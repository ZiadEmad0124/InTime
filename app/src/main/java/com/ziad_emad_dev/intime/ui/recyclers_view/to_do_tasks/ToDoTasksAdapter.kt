package com.ziad_emad_dev.intime.ui.recyclers_view.to_do_tasks

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.ziad_emad_dev.intime.R

class ToDoTasksAdapter(private val toDoTasks: ArrayList<ToDoTask>) :
    RecyclerView.Adapter<ToDoTasksAdapter.ToDoTaskViewHolder>() {

    class ToDoTaskViewHolder(itemView: View) : ViewHolder(itemView) {
        val taskDate: TextView = itemView.findViewById(R.id.taskDate)
        val taskTitle: TextView = itemView.findViewById(R.id.taskTitle)
        val taskDescription: TextView = itemView.findViewById(R.id.taskDescription)
        val toDoTaskOptions: ImageView = itemView.findViewById(R.id.toDoTaskOptions)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToDoTaskViewHolder {
        return ToDoTaskViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.layout_to_do_task, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return toDoTasks.size
    }

    override fun onBindViewHolder(holder: ToDoTaskViewHolder, position: Int) {
        val toDoTask = toDoTasks[position]
        holder.taskDate.text = "${toDoTask.startTime} - ${toDoTask.endTime}"
        holder.taskTitle.text = toDoTask.title
        holder.taskDescription.text = toDoTask.description

        holder.toDoTaskOptions.setOnClickListener {

        }
    }

}