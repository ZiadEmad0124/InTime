package com.ziad_emad_dev.in_time.ui.home.tasks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ziad_emad_dev.in_time.databinding.FragmentTaskInProgressBinding
import com.ziad_emad_dev.in_time.ui.tasks.TaskType
import com.ziad_emad_dev.in_time.ui.tasks.Tasks
import com.ziad_emad_dev.in_time.ui.tasks.TasksAdapter

class InProgressFragment : Fragment() {

    private var _binding: FragmentTaskInProgressBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTaskInProgressBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerView.adapter = TasksAdapter(requireContext(), TaskType.IN_PROGRESS, Tasks.getTasks())
        binding.recyclerView.setHasFixedSize(true)
    }

    override fun onDestroy() {
        super.onDestroy()

        _binding = null
    }

}