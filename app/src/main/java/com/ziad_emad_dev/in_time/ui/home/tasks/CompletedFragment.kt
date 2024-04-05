package com.ziad_emad_dev.in_time.ui.home.tasks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ziad_emad_dev.in_time.databinding.FragmentTaskCompletedBinding
import com.ziad_emad_dev.in_time.ui.tasks.TaskType
import com.ziad_emad_dev.in_time.ui.tasks.Tasks
import com.ziad_emad_dev.in_time.ui.tasks.TasksAdapter

class CompletedFragment : Fragment() {

    private var _binding: FragmentTaskCompletedBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTaskCompletedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerView.adapter = TasksAdapter(requireContext(), TaskType.COMPLETED, Tasks.getTasks())
        binding.recyclerView.setHasFixedSize(true)
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }

}