package com.ziad_emad_dev.in_time.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ziad_emad_dev.in_time.databinding.LayoutTaskBinding
import com.ziad_emad_dev.in_time.network.tasks.Task
import java.text.SimpleDateFormat
import java.util.Locale

class TaskAdapter : ListAdapter<Task, TaskAdapter.TaskViewHolder>(TaskDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        return TaskViewHolder(LayoutTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = getItem(position)
        holder.bind(task)
    }

    class TaskViewHolder(private val binding: LayoutTaskBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(task: Task) {
            binding.taskName.text = task.name

            binding.taskTag.text = task.tag?.name?.ifEmpty { "No Tag"}

            binding.taskDescription.text = task.disc.ifEmpty { "No Description" }

            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
            val outputFormat = SimpleDateFormat("HH:mm dd MMM yyyy", Locale.getDefault())
            val date = inputFormat.parse(task.endAt)
            binding.taskEndDate.text = "End At: ${outputFormat.format(date!!)}"

//            binding.taskCard.setOnClickListener {
//                val intent = Intent(context, TaskPage::class.java)
//                intent.putExtra("task", task)
//                context.startActivity(intent)
//            }
        }
    }

    class TaskDiffCallback : DiffUtil.ItemCallback<Task>() {
        override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
            return oldItem == newItem
        }
    }
}