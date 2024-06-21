package com.ziad_emad_dev.in_time.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ziad_emad_dev.in_time.R
import com.ziad_emad_dev.in_time.databinding.FragmentHomeBinding
import com.ziad_emad_dev.in_time.network.profile.ProfileManager
import com.ziad_emad_dev.in_time.network.tasks.Task
import com.ziad_emad_dev.in_time.ui.tasks.task.TaskPage
import com.ziad_emad_dev.in_time.viewmodels.HomeViewModel
import java.text.SimpleDateFormat
import java.util.Locale

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var profileManager: ProfileManager

    private val viewModel by lazy {
        HomeViewModel(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        profileManager = ProfileManager(requireContext())

        startLoading()

        fetchHome()
    }

    private fun fetchHome() {
        viewModel.fetchProfile()
        viewModel.fetchProfileMessage.observe(viewLifecycleOwner) { message ->
            if (message == "true") {
                completedTasks()
                totalScore()
                inProgress()
                getFirstTwoTasks()
            } else {
                stopLoading()
            }
        }
    }

    private fun completedTasks() {
        binding.completedTasks.let {
            it.icon.setImageResource(R.drawable.ic_completed)
            it.icon.setBackgroundColor(resources.getColor(R.color.primary, null))
            it.name.text = getString(R.string.completed)
            it.points.text = profileManager.getCompletedTasks().toString()
            it.details.text = getString(R.string.tasks)
        }
    }

    private fun totalScore() {
        binding.totalScore.let {
            it.icon.setImageResource(R.drawable.ic_score)
            it.icon.setBackgroundColor(resources.getColor(R.color.orange, null))
            it.name.text = getString(R.string.total_score)
            it.points.text = profileManager.getTotalPoints().toString()
            it.details.text = getString(R.string.points)
        }
    }

    private fun inProgress() {
        binding.inProgress.let {
            it.icon.setImageResource(R.drawable.ic_in_progress)
            it.name.text = getString(R.string.in_progress)
            it.points.text = profileManager.getOnGoingTasks().toString()
            it.details.text = getString(R.string.tasks)
        }
    }

    private fun getFirstTwoTasks() {
        viewModel.getTasks()
        viewModel.getTasksMessage.observe(viewLifecycleOwner) { message ->
            if (message == "true") {
                viewModel.getTasks.observe(viewLifecycleOwner) { tasks ->
                    val myTasks = tasks.filter { !it.completed }
                    if (myTasks.isNotEmpty()) {
                        binding.noTasksLayout.visibility = View.GONE
                        binding.firstTask.visibility = View.VISIBLE
                        myFirstTasks(myTasks[0])
                        stopLoading()
                        if (myTasks.size > 1) {
                            binding.secondTask.visibility = View.VISIBLE
                            mySecondTasks(myTasks[1])
                            stopLoading()
                        }
                    } else {
                        stopLoading()
                    }
                }
            } else {
                stopLoading()
            }
        }
    }

    private fun myFirstTasks(task: Task) {

        binding.firstTask.setOnClickListener {
            val intent = Intent(context, TaskPage::class.java)
            intent.putExtra("task", task)
            startActivity(intent)
        }

        binding.title1.text = task.name

        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        val outputFormat = SimpleDateFormat("HH:mm dd MMM yyyy", Locale.getDefault())
        val endDate = inputFormat.parse(task.endAt)
        binding.date1.text = outputFormat.format(endDate!!)

        var completedSteps = 0

        for (step in task.steps) {
            if (step.completed) {
                completedSteps++
            }
        }

        binding.progressText1.text = getString(R.string.task_progress, completedSteps.toString())

        binding.progressBar1.progress = completedSteps

        when (task.priority) {
            0 -> binding.priority1.setImageResource(R.drawable.ic_flag_red)
            1 -> binding.priority1.setImageResource(R.drawable.ic_flag_orange)
            2 -> binding.priority1.setImageResource(R.drawable.ic_flag_yellow)
            else -> binding.priority1.setImageResource(R.drawable.ic_flag_grey)
        }

        if (task.steps.isEmpty()) {
          binding.points1.visibility = View.GONE
        } else {
            binding.points1.text = getString(R.string.task_points, completedSteps.toString(), task.steps.size.toString())
        }
    }

    private fun mySecondTasks(task: Task) {

        binding.secondTask.setOnClickListener {
            val intent = Intent(context, TaskPage::class.java)
            intent.putExtra("task", task)
            startActivity(intent)
        }

        binding.title2.text = task.name

        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        val outputFormat = SimpleDateFormat("HH:mm dd MMM yyyy", Locale.getDefault())
        val endDate = inputFormat.parse(task.endAt)
        binding.date2.text = outputFormat.format(endDate!!)

        var completedSteps = 0

        for (step in task.steps) {
            if (step.completed) {
                completedSteps++
            }
        }

        binding.progressText2.text = getString(R.string.task_progress, completedSteps.toString())

        binding.progressBar2.progress = completedSteps

        when (task.priority) {
            0 -> binding.priority2.setImageResource(R.drawable.ic_flag_red)
            1 -> binding.priority2.setImageResource(R.drawable.ic_flag_orange)
            2 -> binding.priority2.setImageResource(R.drawable.ic_flag_yellow)
            else -> binding.priority2.setImageResource(R.drawable.ic_flag_grey)
        }

        if (task.steps.isEmpty()) {
            binding.points2.visibility = View.GONE
        } else {
            binding.points2.text = getString(R.string.task_points, completedSteps.toString(), task.steps.size.toString())
        }
    }

    private fun startLoading() {
        binding.blockingView.visibility = View.VISIBLE
    }

    private fun stopLoading() {
        binding.blockingView.visibility = View.GONE
    }

    override fun onResume() {
        super.onResume()
        startLoading()
        fetchHome()
    }

    override fun onDestroy() {
        super.onDestroy()

        _binding = null
    }
}