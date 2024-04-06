package com.ziad_emad_dev.in_time.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ziad_emad_dev.in_time.R
import com.ziad_emad_dev.in_time.databinding.FragmentHomeBinding
import com.ziad_emad_dev.in_time.ui.notification.NotificationActivity
import com.ziad_emad_dev.in_time.ui.profile.ProfileActivity

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        myToolbar()

        completedTasks()
        totalScore()
        inProgress()

        myFirstTasks()
        mySecondTasks()

        return binding.root
    }

    private fun myToolbar() {
        binding.myToolbar.title.text = getString(R.string.time)

        binding.myToolbar.notification.setOnClickListener {
            val intent = Intent(requireContext(), NotificationActivity::class.java)
            startActivity(intent)
        }

        binding.myToolbar.menu.setOnClickListener {
            val intent = Intent(requireContext(), ProfileActivity::class.java)
            startActivity(intent)
        }
    }

    private fun completedTasks() {
        binding.completedTasks.let {
            it.icon.setImageResource(R.drawable.ic_completed)
            it.icon.setBackgroundColor(resources.getColor(R.color.primary))
            it.name.text = getString(R.string.completed)
            it.points.text = 27.toString()
            it.details.text = getString(R.string.tasks)
        }
    }

    private fun totalScore() {
        binding.totalScore.let {
            it.icon.setImageResource(R.drawable.ic_score)
            it.icon.setBackgroundColor(resources.getColor(R.color.orange))
            it.name.text = getString(R.string.total_score)
            it.points.text = 250.toString()
            it.details.text = getString(R.string.points)
        }
    }

    private fun inProgress() {
        binding.inProgress.let {
            it.icon.setImageResource(R.drawable.ic_in_progress)
            it.name.text = getString(R.string.in_progress)
            it.points.text = 4.toString()
            it.details.text = getString(R.string.tasks)
        }
    }

    private fun myFirstTasks() {
        binding.firstTask.let {
            it.icon.setImageResource(R.drawable.ic_done)
            it.title.text = getString(R.string.task_title, "1")
            it.date.text = getString(R.string.task_date, "3.00", "pm")
            it.progressText.text = getString(R.string.task_progress, "60%")
            it.progressBar.progress = 60
            it.priority.setImageResource(R.drawable.ic_flag_red)
            it.points.text = getString(R.string.task_points, "60", "100")
        }
    }

    private fun mySecondTasks() {
        binding.secondTask.let {
            it.icon.setImageResource(R.drawable.ic_still)
            it.title.text = getString(R.string.task_title, "2")
            it.date.text = getString(R.string.task_date, "3.00", "pm")
            it.progressText.text = getString(R.string.task_progress, "25%")
            it.progressBar.progress = 25
            it.priority.setImageResource(R.drawable.ic_flag_orange)
            it.points.text = getString(R.string.task_points, "25", "100")
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        _binding = null
    }
}