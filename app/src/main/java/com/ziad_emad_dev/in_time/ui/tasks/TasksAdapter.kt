package com.ziad_emad_dev.in_time.ui.tasks

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.google.android.material.card.MaterialCardView
import com.ziad_emad_dev.in_time.R

class TasksAdapter(
    private val context: Context,
    private val taskType: String,
    private val tasks: ArrayList<Task>
) :
    RecyclerView.Adapter<TasksAdapter.TasksViewHolder>() {

    class TasksViewHolder(itemView: View) : ViewHolder(itemView) {
        val taskCard: MaterialCardView = itemView.findViewById(R.id.taskCard)
        val taskIndicator: View = itemView.findViewById(R.id.taskIndicator)
        val taskDate: TextView = itemView.findViewById(R.id.taskDate)
        val taskTitle: TextView = itemView.findViewById(R.id.taskTitle)
        val taskDescription: TextView = itemView.findViewById(R.id.taskDescription)
        val taskOptions: ImageView = itemView.findViewById(R.id.taskOptions)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TasksViewHolder {
        return TasksViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.layout_task, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return tasks.size
    }

    override fun onBindViewHolder(holder: TasksViewHolder, position: Int) {

        when (taskType) {
            TaskStatus.BACKLOG -> {
                holder.taskCard.strokeColor = ContextCompat.getColor(context, R.color.red)
                holder.taskIndicator.setBackgroundResource(R.color.red)
            }

            TaskStatus.IN_PROGRESS -> {
                holder.taskCard.strokeColor = ContextCompat.getColor(context, R.color.secondary)
                holder.taskIndicator.setBackgroundResource(R.color.secondary)
            }

            TaskStatus.COMPLETED -> {
                holder.taskCard.strokeColor = ContextCompat.getColor(context, R.color.green)
                holder.taskIndicator.setBackgroundResource(R.color.green)
            }
        }

        val task = tasks[position]
        holder.taskDate.text = context.getString(R.string.to_do_task_date, "11.30Am", "12.30 Pm")
        holder.taskTitle.text = task.title
        holder.taskDescription.text = task.description

        holder.taskOptions.setOnClickListener {

        }
    }
}