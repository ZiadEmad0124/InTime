package com.ziad_emad_dev.in_time.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import com.ziad_emad_dev.in_time.R
import com.ziad_emad_dev.in_time.databinding.FragmentHomeBinding
import com.ziad_emad_dev.in_time.network.profile.ProfileManager
import com.ziad_emad_dev.in_time.network.tasks.Task
import com.ziad_emad_dev.in_time.ui.TaskAdapter
import com.ziad_emad_dev.in_time.ui.tasks.task.TaskPage
import com.ziad_emad_dev.in_time.viewmodels.HomeViewModel
import java.text.SimpleDateFormat
import java.util.Locale

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var profileManager: ProfileManager

    private lateinit var taskAdapter: TaskAdapter

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

        searchBar()

        taskAdapter = TaskAdapter()

        binding.searchRecyclerView.adapter = taskAdapter

        completedTasks()
        totalScore()
        inProgress()

        startLoading()

        getFirstTwoTasks()
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
                        }
                    }
                }
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

        binding.progressText1.text = getString(R.string.task_progress, "60%")

        binding.progressBar1.progress = 60

        binding.priority2.setImageResource(setPriority(task.priority))

        binding.points1.text = getString(R.string.task_points, "60", "100")
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

        binding.progressText2.text = getString(R.string.task_progress, "25%")

        binding.progressBar2.progress = 25

        binding.priority2.setImageResource(setPriority(task.priority))

        binding.points2.text = getString(R.string.task_points, "25", "100")
    }

    private fun setPriority(num: Int) : Int {
        val flag = when (num) {
            1 -> R.drawable.ic_flag_red
            2 -> R.drawable.ic_flag_orange
            3 -> R.drawable.ic_flag_yellow
            else -> R.drawable.circle_gray
        }
        return flag
    }

    private fun startLoading() {
        binding.blockingView.visibility = View.VISIBLE
    }

    private fun stopLoading() {
        binding.blockingView.visibility = View.GONE
    }

    private fun searchBar() {
        binding.searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let { taskName ->
                    if (taskName.isNotEmpty()) {
                        viewModel.searchTask(taskName)
                    }
                }
                return false
            }
        })

//        binding.searchBar.setOnSearchClickListener {
//            binding.searchRecyclerView.visibility = View.VISIBLE
//        }
//
//        binding.searchBar.setOnCloseListener {
//            binding.searchRecyclerView.visibility = View.GONE
//            false
//        }

        viewModel.getSearchTask.observe(viewLifecycleOwner) { tasks ->
            updateTaskList(tasks)
        }
    }

    private fun updateTaskList(tasks: List<Task>) {
        taskAdapter.submitList(tasks)
    }

    override fun onDestroy() {
        super.onDestroy()

        _binding = null
    }
}