package com.ziad_emad_dev.in_time.ui.project_tasks

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.ziad_emad_dev.in_time.R

class ProjectsTasksAdapter(
    private val context: Context,
    private val projectTasks: ArrayList<ProjectTask>
) :
    RecyclerView.Adapter<ProjectsTasksAdapter.ProjectsTasksViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProjectsTasksViewHolder {
        return ProjectsTasksViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.layout_project_task, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return projectTasks.size
    }

    class ProjectsTasksViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val projectTaskCard: MaterialCardView = itemView.findViewById(R.id.projectTaskCard)
        val taskTypeCard: MaterialCardView = itemView.findViewById(R.id.taskTypeCard)
        val taskType: TextView = itemView.findViewById(R.id.taskType)
        val taskUserImage: ImageView = itemView.findViewById(R.id.taskUserImage)
        val taskUser: TextView = itemView.findViewById(R.id.taskUser)
        val taskDate: TextView = itemView.findViewById(R.id.taskDate)
        val taskTitle: TextView = itemView.findViewById(R.id.taskTitle)
        val isTaskMineCard: MaterialCardView = itemView.findViewById(R.id.isTaskMineCard)
        val isTaskMine: TextView = itemView.findViewById(R.id.isTaskMine)
        val taskStatusCard: MaterialCardView = itemView.findViewById(R.id.taskStatusCard)
        val taskStatus: TextView = itemView.findViewById(R.id.taskStatus)
    }

    override fun onBindViewHolder(holder: ProjectsTasksViewHolder, position: Int) {

        val projectTask = projectTasks[position]

        when (projectTask.taskStatus) {
            ProjectTaskStatus.FINISHED -> {
                holder.projectTaskCard.strokeColor = ContextCompat.getColor(context, R.color.green)
                holder.taskStatusCard.setCardBackgroundColor(ContextCompat.getColor(context, R.color.green))
                holder.taskStatus.text = ProjectTaskStatus.FINISHED
            }

            ProjectTaskStatus.IN_PROGRESS -> {
                holder.projectTaskCard.strokeColor = ContextCompat.getColor(context, R.color.secondary)
                holder.taskStatusCard.setCardBackgroundColor(ContextCompat.getColor(context, R.color.secondary))
                holder.taskStatus.text = ProjectTaskStatus.IN_PROGRESS
            }

            ProjectTaskStatus.WAIT_TO_START -> {
                holder.projectTaskCard.strokeColor = ContextCompat.getColor(context, R.color.secondary)
                holder.taskStatusCard.setCardBackgroundColor(ContextCompat.getColor(context, R.color.secondary))
                holder.taskStatus.text = ProjectTaskStatus.WAIT_TO_START
            }
        }

        holder.taskTypeCard.setCardBackgroundColor(ContextCompat.getColor(context, projectTask.taskTypeColor))
        holder.taskType.text = projectTask.taskType
        holder.taskUserImage.setImageResource(projectTask.taskUserImage)
        holder.taskUser.text = projectTask.taskUser
        holder.taskDate.text = projectTask.taskDate
        holder.taskTitle.text = projectTask.taskTitle
        if (!projectTask.isTaskMine) {
            holder.isTaskMineCard.visibility = View.GONE
            holder.isTaskMine.visibility = View.GONE
        }
    }
}