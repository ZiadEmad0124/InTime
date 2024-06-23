package com.ziad_emad_dev.in_time.ui.projects

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.ziad_emad_dev.in_time.databinding.FragmentProjectsAsMemberBinding
import com.ziad_emad_dev.in_time.ui.create.CreateProject
import com.ziad_emad_dev.in_time.ui.projects.project.ProjectsAdapter
import com.ziad_emad_dev.in_time.viewmodels.ProjectViewModel

class AsMemberFragment : Fragment() {

    private var _binding: FragmentProjectsAsMemberBinding? = null
    private val binding get() = _binding!!

    private val viewModel by lazy {
        ProjectViewModel(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProjectsAsMemberBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        newProject()

        getProjects()
    }

    private fun getProjects() {
        viewModel.getProjects("user")
        viewModel.getProjectsMessage.observe(viewLifecycleOwner) { message ->
            if (message == "Get all projects success") {
                viewModel.getProjects.observe(viewLifecycleOwner) { projects ->
                    stopLoading()
                    if (projects.isEmpty()) {
                        binding.noProjects.visibility = View.VISIBLE
                    } else {
                        binding.recyclerView.adapter = ProjectsAdapter(requireContext(), projects as ArrayList)
                        binding.recyclerView.setHasFixedSize(true)
                    }
                }
            } else {
                noConnection()
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun newProject() {
        binding.newProjectButton.setOnClickListener {
            val intent = Intent(requireContext(), CreateProject::class.java)
            startActivity(intent)
            requireActivity().finish()
        }
    }

    private fun stopLoading() {
        binding.blockingView.visibility = View.GONE
        binding.blockingViewNoConnection.visibility = View.GONE
    }

    private fun noConnection() {
        binding.blockingView.visibility = View.GONE
        binding.blockingViewNoConnection.visibility = View.VISIBLE
        binding.noConnection.setOnClickListener {
            viewModel.getProjects("member")
            binding.blockingViewNoConnection.visibility = View.GONE
            binding.blockingView.visibility = View.VISIBLE
        }
    }

    override fun onResume() {
        super.onResume()
        binding.blockingView.visibility = View.VISIBLE
        getProjects()
    }

    override fun onDestroy() {
        super.onDestroy()

        _binding = null
    }
}