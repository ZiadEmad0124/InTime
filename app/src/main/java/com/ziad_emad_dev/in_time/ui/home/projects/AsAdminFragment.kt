package com.ziad_emad_dev.in_time.ui.home.projects

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ziad_emad_dev.in_time.databinding.FragmentProjectsAsAdminBinding
import com.ziad_emad_dev.in_time.ui.projects.Projects
import com.ziad_emad_dev.in_time.ui.projects.ProjectsAdapter

class AsAdminFragment : Fragment() {

    private var _binding: FragmentProjectsAsAdminBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProjectsAsAdminBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerView.adapter = ProjectsAdapter(requireContext(), Projects.getProjects())
        binding.recyclerView.setHasFixedSize(true)
    }

    override fun onDestroy() {
        super.onDestroy()

        _binding = null
    }
}