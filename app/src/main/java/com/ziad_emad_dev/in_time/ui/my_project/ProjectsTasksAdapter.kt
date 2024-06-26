package com.ziad_emad_dev.in_time.ui.my_project

import android.content.Context
import android.content.Intent
import android.graphics.Color
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
            val colorName = projectTask.tag?.color
            for ((key, value) in colorMap) {
                if (colorName == key) {
                    holder.taskTagColor.setCardBackgroundColor(Color.parseColor(value))
                    break
                }
            }
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
                intent.putExtra("taskCompleted", projectTask.completed)
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