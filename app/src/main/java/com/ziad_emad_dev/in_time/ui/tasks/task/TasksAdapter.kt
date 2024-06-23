package com.ziad_emad_dev.in_time.ui.tasks.task

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.google.android.material.card.MaterialCardView
import com.ziad_emad_dev.in_time.R
import com.ziad_emad_dev.in_time.network.tasks.Task
import java.text.SimpleDateFormat
import java.util.Locale

class TasksAdapter(private val context: Context, private val taskType: String, private val tasks: ArrayList<Task>) :
    RecyclerView.Adapter<TasksAdapter.TasksViewHolder>() {

    class TasksViewHolder(itemView: View) : ViewHolder(itemView) {
        val taskCard: MaterialCardView = itemView.findViewById(R.id.taskCard)
        val taskIndicator: View = itemView.findViewById(R.id.taskIndicator)
        val taskEndDate: TextView = itemView.findViewById(R.id.taskEndDate)
        val taskName: TextView = itemView.findViewById(R.id.taskName)
        val taskTag: TextView = itemView.findViewById(R.id.taskTag)
        val taskDescription: TextView = itemView.findViewById(R.id.taskDescription)
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

        val color = when (taskType) {
            "In Progress" -> {

                R.color.orange
            }

            "Completed" -> {

                R.color.green
            }

            else -> {

                R.color.red
            }
        }

        holder.taskCard.strokeColor = context.getColor(color)
        holder.taskIndicator.setBackgroundColor(context.getColor(color))

        val task = tasks[position]

        holder.taskName.text = task.name

        holder.taskTag.text = task.tag?.name?.ifEmpty { "No Tag"}

        holder.taskDescription.text = task.disc.ifEmpty { "No Description" }

        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        val outputFormat = SimpleDateFormat("HH:mm dd MMM yyyy", Locale.getDefault())
        val date = inputFormat.parse(task.endAt)
        holder.taskEndDate.text = context.getString(R.string.end_at, outputFormat.format(date!!))

        holder.taskCard.setOnClickListener {
            val intent = Intent(context, TaskPage::class.java)
            intent.putExtra("task", task)
            context.startActivity(intent)
        }
    }
}