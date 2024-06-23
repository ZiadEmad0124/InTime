package com.ziad_emad_dev.in_time.ui.my_project

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.card.MaterialCardView
import com.ziad_emad_dev.in_time.R
import com.ziad_emad_dev.in_time.network.profile.ProfileManager
import com.ziad_emad_dev.in_time.network.project.Project
import com.ziad_emad_dev.in_time.network.project.tasks.Record
import com.ziad_emad_dev.in_time.ui.my_project.project_task.AdminEditProjectTask
import com.ziad_emad_dev.in_time.ui.my_project.project_task.MemberProjectTask
import java.text.SimpleDateFormat
import java.util.Locale

class ProjectsTasksAdapter(private val context: Context, private val projectTasks: ArrayList<Record>,
                           private val projectId: String, private val project: Project,
                           private val admin: Boolean, private val adminId: String,
                           private val membersName: ArrayList<String>, private val membersImage: ArrayList<String>, private val membersId: ArrayList<String>) :
    RecyclerView.Adapter<ProjectsTasksAdapter.ProjectsTasksViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProjectsTasksViewHolder {
        return ProjectsTasksViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.layout_project_task, parent, false))
    }

    override fun getItemCount(): Int {
        return projectTasks.size
    }

    class ProjectsTasksViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val projectTaskCard: MaterialCardView = itemView.findViewById(R.id.projectTaskCard)
        val taskTagColor: MaterialCardView = itemView.findViewById(R.id.taskTagColor)
        val taskTagName: TextView = itemView.findViewById(R.id.taskTagName)
        val taskMemberImage: ImageView = itemView.findViewById(R.id.taskMemberImage)
        val taskMemberName: TextView = itemView.findViewById(R.id.taskMemberName)
        val taskEndDate: TextView = itemView.findViewById(R.id.taskEndDate)
        val taskName: TextView = itemView.findViewById(R.id.taskName)
        val isTaskMine: MaterialCardView = itemView.findViewById(R.id.isTaskMine)
        val taskStatusCard: MaterialCardView = itemView.findViewById(R.id.taskStatusCard)
        val taskStatus: TextView = itemView.findViewById(R.id.taskStatus)
    }

    private fun convertDateFormat(dateString: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", Locale.getDefault())
        val outputFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        val date = inputFormat.parse(dateString)
        return outputFormat.format(date!!)
    }

    override fun onBindViewHolder(holder: ProjectsTasksViewHolder, position: Int) {

        val projectTask = projectTasks[position]

        if (projectTask.tag?.name.isNullOrEmpty()) {
            holder.taskTagColor.visibility = View.GONE
        } else {
            holder.taskTagColor.setCardBackgroundColor(ContextCompat.getColor(context, R.color.primary))
            holder.taskTagName.text = projectTask.tag?.name
        }

        var index = -1

        for (memberId in membersId ) {
            index++
            if (memberId == projectTask.userId) {
                holder.taskMemberName.text = membersName[index]

                Glide.with(context)
                    .load("https://intime-9hga.onrender.com/api/v1/images/${membersImage[index]}")
                    .placeholder(R.drawable.project_image)
                    .error(R.drawable.project_image)
                    .into(holder.taskMemberImage)
                break
            }
        }

        holder.taskEndDate.text = convertDateFormat(projectTask.endAt)

        holder.taskName.text = projectTask.name

        holder.isTaskMine.visibility = if (projectTask.userId == ProfileManager(context).getProfileId()) View.VISIBLE else View.GONE

        if (projectTask.completed) {
            holder.projectTaskCard.strokeColor = ContextCompat.getColor(context, R.color.green)
            holder.taskStatusCard.setCardBackgroundColor(ContextCompat.getColor(context, R.color.green))
            holder.taskStatus.text = context.getString(R.string.completed)
        } else {
            holder.projectTaskCard.strokeColor = ContextCompat.getColor(context, R.color.primary)
            holder.taskStatusCard.setCardBackgroundColor(ContextCompat.getColor(context, R.color.primary))
            holder.taskStatus.text = context.getString(R.string.in_progress)
        }

        holder.projectTaskCard.setOnClickListener {
            if (admin) {
                val intent = Intent(context, AdminEditProjectTask::class.java)
                intent.putExtra("projectTask", projectTask)
                intent.putExtra("project", project)
                intent.putExtra("projectId", projectId)
                intent.putExtra("taskId", projectTask.id)
                intent.putExtra("taskName", projectTask.name)
                intent.putExtra("taskDescription", projectTask.disc)
                intent.putExtra("taskEndAt", projectTask.endAt)
                intent.putExtra("taskStartAt", projectTask.startAt)
                context.startActivity(intent)
            } else if (projectTask.userId == ProfileManager(context).getProfileId()) {
                val intent = Intent(context, MemberProjectTask::class.java)
                intent.putExtra("projectTask", projectTask)
                intent.putExtra("project", project)
                intent.putExtra("projectId", projectId)
                intent.putExtra("taskId", projectTask.id)
                context.startActivity(intent)
            }
        }
    }
}