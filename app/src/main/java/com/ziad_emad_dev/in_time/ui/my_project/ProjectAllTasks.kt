package com.ziad_emad_dev.in_time.ui.my_project

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ziad_emad_dev.in_time.databinding.FragmentProjectAllTasksBinding
import com.ziad_emad_dev.in_time.ui.project_tasks.ProjectTasks
import com.ziad_emad_dev.in_time.ui.project_tasks.ProjectsTasksAdapter

class ProjectAllTasks : Fragment() {

    private var _binding: FragmentProjectAllTasksBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProjectAllTasksBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerView.adapter = ProjectsTasksAdapter(requireContext(), ProjectTasks.getProjectTasks())
        binding.recyclerView.setHasFixedSize(true)
    }

    override fun onDestroy() {
        super.onDestroy()

        _binding = null
    }
}