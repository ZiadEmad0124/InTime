package com.ziad_emad_dev.in_time.ui.my_project

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ziad_emad_dev.in_time.databinding.FragmentProjectWaitToStartTasksBinding
import com.ziad_emad_dev.in_time.ui.project_tasks.ProjectTaskStatus
import com.ziad_emad_dev.in_time.ui.project_tasks.ProjectTasks
import com.ziad_emad_dev.in_time.ui.project_tasks.ProjectsTasksAdapter

class ProjectWaitToStartTasks : Fragment() {

    private var _binding: FragmentProjectWaitToStartTasksBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProjectWaitToStartTasksBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val waitToStartTasks = ArrayList(ProjectTasks.getProjectTasks().filter {
            it.taskStatus == ProjectTaskStatus.WAIT_TO_START }
        )

        binding.recyclerView.adapter = ProjectsTasksAdapter(requireContext(), waitToStartTasks)
        binding.recyclerView.setHasFixedSize(true)
    }

    override fun onDestroy() {
        super.onDestroy()

        _binding = null
    }
}