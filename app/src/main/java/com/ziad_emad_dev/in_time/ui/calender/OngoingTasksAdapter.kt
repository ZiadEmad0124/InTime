package com.ziad_emad_dev.in_time.ui.calender

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ziad_emad_dev.in_time.R
import com.ziad_emad_dev.in_time.databinding.ItemOngoingTaskBinding
import com.ziad_emad_dev.in_time.network.tasks.Task

class OngoingTasksAdapter(private var tasks: List<Task>) : RecyclerView.Adapter<OngoingTasksAdapter.TaskViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val binding = ItemOngoingTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TaskViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(tasks[position])
    }

    override fun getItemCount(): Int = tasks.size

    inner class TaskViewHolder(private val binding: ItemOngoingTaskBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("ResourceAsColor")
        fun bind(task: Task) {

            if (task.tag?.name.isNullOrEmpty()) {
                binding.ongoingTaskCard.setCardBackgroundColor(R.color.primary)
            } else {
                val colorName = task.tag?.color
                for ((key, value) in colorMap) {
                    if (colorName == key) {
                        binding.ongoingTaskCard.setCardBackgroundColor(Color.parseColor(value))
                        break
                    }
                }
            }

            binding.taskName.text = task.name
            binding.taskDetails.text = task.disc.ifEmpty { "No Description" }
            binding.taskTime.text = task.endAt.substring(11, 16)
            if (task.projectTask) {
                binding.groupIcon.visibility = ViewGroup.VISIBLE
            }
        }
    }

    private val colorMap = mapOf(
        "darkseagreen" to "#8FBC8F",
        "crimson" to "#DC143C",
        "chocolate" to "#D2691E",
        "mediumseagreen" to "#3CB371",
        "dark-turquoise" to "#00CED1",
        "mediumorchid" to "#BA55D3",
        "slateblue" to "#6A5ACD",
        "cyan" to "#00FFFF",
        "saddlebrown" to "#8B4513",
        "firebrick" to "#B22222",
        "lime" to "#00FF00",
        "salmon" to "#FA8072",
        "mediumvioletred" to "#C71585",
        "teal" to "#008080",
        "gold" to "#FFD700",
        "orchid" to "#DA70D6",
        "darkred" to "#8B0000",
        "darkgoldenrod" to "#B8860B",
        "purple" to "#800080",
        "darkolivegreen" to "#556B2F",
        "navy" to "#000080",
        "darkorange" to "#FF8C00",
        "mediumblue" to "#0000CD",
        "seagreen" to "#2E8B57",
        "maroon" to "#800000",
        "sienna" to "#A0522D",
        "magenta" to "#FF00FF",
        "indianred" to "#CD5C5C",
        "steelblue" to "#4682B4",
        "sandybrown" to "#F4A460",
        "yellow" to "#FFFF00",
        "blue" to "#0000FF",
        "violet" to "#EE82EE",
        "coral" to "#FF7F50",
        "mediumslateblue" to "#7B68EE",
        "pink" to "#FFC0CB",
        "dimgray" to "#696969",
        "indigo" to "#4B0082",
        "royalblue" to "#4169E1",
        "red" to "#FF0000",
        "turquoise" to "#40E0D0",
        "green" to "#008000",
        "tomato" to "#FF6347",
        "darkviolet" to "#9400D3",
        "slategray" to "#708090",
        "dodgerblue" to "#1E90FF",
        "mediumpurple" to "#9370DB",
        "brown" to "#A52A2A",
        "orange" to "#FFA500",
        "forestgreen" to "#228B22"
    )
}