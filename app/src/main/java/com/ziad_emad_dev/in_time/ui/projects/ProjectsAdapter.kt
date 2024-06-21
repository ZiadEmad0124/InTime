package com.ziad_emad_dev.in_time.ui.projects

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.google.android.material.card.MaterialCardView
import com.ziad_emad_dev.in_time.R
import com.ziad_emad_dev.in_time.network.project.Project
import com.ziad_emad_dev.in_time.ui.projects.project.ProjectPage

class ProjectsAdapter(private val context: Context, private val projects: ArrayList<Project>) :
    RecyclerView.Adapter<ProjectsAdapter.ProjectsViewHolder>() {

    class ProjectsViewHolder(itemView: View) : ViewHolder(itemView) {
        val projectCard: MaterialCardView = itemView.findViewById(R.id.projectCard)
        val projectName: TextView = itemView.findViewById(R.id.projectName)
        val projectCover: ImageView = itemView.findViewById(R.id.projectCover)
        val viewTasks: MaterialCardView = itemView.findViewById(R.id.viewTasks)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProjectsViewHolder {
        return ProjectsViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.layout_project, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return projects.size
    }

    override fun onBindViewHolder(holder: ProjectsViewHolder, position: Int) {
        val project = projects[position]

        holder.projectName.text = project.name
        Glide.with(context)
            .load("https://intime-9hga.onrender.com/api/v1/images/${project.photo}")
            .placeholder(R.drawable.project_image)
            .error(R.drawable.project_image)
            .into(holder.projectCover)

        holder.projectCard.setOnClickListener {
            val intent = Intent(context, ProjectPage::class.java)
            intent.putExtra("project", project)
            context.startActivity(intent)
        }

        holder.viewTasks.setOnClickListener {

        }
    }
}