package com.ziad_emad_dev.in_time.ui.projects

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.ziad_emad_dev.in_time.R

class ProjectsAdapter(
    private val context: Context,
    private val projects: ArrayList<Project>
) :
    RecyclerView.Adapter<ProjectsAdapter.ProjectsAllViewHolder>() {

    class ProjectsAllViewHolder(itemView: View) : ViewHolder(itemView) {
        val projectName: TextView = itemView.findViewById(R.id.projectName)
        val projectPhoto: ImageView = itemView.findViewById(R.id.projectPhoto)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProjectsAllViewHolder {
        return ProjectsAllViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.layout_project, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return projects.size
    }

    override fun onBindViewHolder(holder: ProjectsAllViewHolder, position: Int) {
        val project = projects[position]
        holder.projectName.text = project.projectName
        holder.projectPhoto.setImageResource(project.projectPhoto)
    }
}