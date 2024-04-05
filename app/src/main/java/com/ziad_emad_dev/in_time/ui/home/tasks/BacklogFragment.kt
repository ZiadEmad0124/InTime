package com.ziad_emad_dev.in_time.ui.home.tasks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ziad_emad_dev.in_time.databinding.FragmentTaskBacklogBinding
import com.ziad_emad_dev.in_time.ui.tasks.TaskType
import com.ziad_emad_dev.in_time.ui.tasks.Tasks
import com.ziad_emad_dev.in_time.ui.tasks.TasksAdapter

class BacklogFragment : Fragment() {

    private var _binding: FragmentTaskBacklogBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTaskBacklogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerView.adapter = TasksAdapter(requireContext(), TaskType.BACKLOG, Tasks.getTasks())
        binding.recyclerView.setHasFixedSize(true)
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }

}