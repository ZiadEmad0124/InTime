package com.ziad_emad_dev.in_time.ui.tasks

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.ziad_emad_dev.in_time.databinding.FragmentTaskBacklogBinding
import com.ziad_emad_dev.in_time.ui.create.CreateTask
import com.ziad_emad_dev.in_time.ui.tasks.task.TasksAdapter
import com.ziad_emad_dev.in_time.viewmodels.TaskViewModel

class BacklogFragment : Fragment() {

    private var _binding: FragmentTaskBacklogBinding? = null
    private val binding get() = _binding!!

    private val viewModel by lazy {
        TaskViewModel(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTaskBacklogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        newTask()

        getTasks()
    }

    private fun newTask() {
        binding.newTaskButton.setOnClickListener {
            val intent = Intent(requireContext(), CreateTask::class.java)
            startActivity(intent)
        }
    }

    private fun getTasks() {
        viewModel.getTasks()
        viewModel.getTasksMessage.observe(viewLifecycleOwner) { message ->
            if (message == "true") {
                viewModel.getTasks.observe(viewLifecycleOwner) { tasks ->
                    val backlogTasks = tasks.filter { it.backlog }
                    stopLoading()
                    if (backlogTasks.isEmpty()) {
                        binding.noProjects.visibility = View.VISIBLE
                    } else {
                        binding.recyclerView.adapter = TasksAdapter(requireContext(), "Backlog", backlogTasks as ArrayList)
                        binding.recyclerView.setHasFixedSize(true)
                    }
                }
            } else {
                noConnection()
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
            }
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
            viewModel.getTasks()
            binding.blockingViewNoConnection.visibility = View.GONE
            binding.blockingView.visibility = View.VISIBLE
        }
    }

    override fun onResume() {
        super.onResume()
        binding.blockingView.visibility = View.VISIBLE
        getTasks()
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }
}