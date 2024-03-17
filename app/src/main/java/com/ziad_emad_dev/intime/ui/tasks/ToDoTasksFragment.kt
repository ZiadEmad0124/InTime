package com.ziad_emad_dev.intime.ui.tasks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ziad_emad_dev.intime.databinding.FragmentToDoBinding
import com.ziad_emad_dev.intime.ui.recyclers_view.to_do_tasks.ToDoTasks
import com.ziad_emad_dev.intime.ui.recyclers_view.to_do_tasks.ToDoTasksAdapter

class ToDoTasksFragment : Fragment() {

    private var _binding: FragmentToDoBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentToDoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.ToDoRecyclerView.adapter = ToDoTasksAdapter(ToDoTasks.getToDoTasks())
        binding.ToDoRecyclerView.setHasFixedSize(true)

    }

    override fun onDestroy() {
        super.onDestroy()

        _binding = null
    }
}