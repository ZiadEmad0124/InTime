package com.ziad_emad_dev.in_time.ui.project_tasks

class ProjectTasks {
    companion object {
        fun getProjectTasks(): ArrayList<ProjectTask> {
            return arrayListOf(
                ProjectTask(
                    "Code",
                    com.ziad_emad_dev.in_time.R.color.primary,
                    com.ziad_emad_dev.in_time.R.drawable.ic_profile,
                    "Ziad",
                    "End Today",
                    "Android",
                    true,
                    ProjectTaskStatus.FINISHED
                ),
                ProjectTask(
                    "Code",
                    com.ziad_emad_dev.in_time.R.color.primary,
                    com.ziad_emad_dev.in_time.R.drawable.ic_profile,
                    "Ahmed",
                    "Yesterday",
                    "Back End",
                    false,
                    ProjectTaskStatus.WAIT_TO_START
                ),
                ProjectTask(
                    "Code",
                    com.ziad_emad_dev.in_time.R.color.primary,
                    com.ziad_emad_dev.in_time.R.drawable.ic_profile,
                    "Abdalla",
                    "Tomorrow",
                    "Front End",
                    false,
                    ProjectTaskStatus.IN_PROGRESS
                ),
                ProjectTask(
                    "Code",
                    com.ziad_emad_dev.in_time.R.color.primary,
                    com.ziad_emad_dev.in_time.R.drawable.ic_profile,
                    "Abd El-rahman",
                    "Tomorrow",
                    "Front End",
                    false,
                    ProjectTaskStatus.IN_PROGRESS
                ),
                ProjectTask(
                    "Design",
                    com.ziad_emad_dev.in_time.R.color.sky,
                    com.ziad_emad_dev.in_time.R.drawable.ic_profile,
                    "Islam",
                    "Today",
                    "UI / UX Design",
                    false,
                    ProjectTaskStatus.WAIT_TO_START
                )
            )
        }
    }
}