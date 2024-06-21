package com.ziad_emad_dev.in_time.ui.tasks.task

import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.text.Editable
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.ziad_emad_dev.in_time.R
import com.ziad_emad_dev.in_time.databinding.ActivityTaskPageBinding
import com.ziad_emad_dev.in_time.network.tasks.Step
import com.ziad_emad_dev.in_time.network.tasks.Task
import com.ziad_emad_dev.in_time.viewmodels.TaskViewModel
import java.text.SimpleDateFormat
import java.util.Locale

@Suppress("DEPRECATION")
class TaskPage : AppCompatActivity() {

    private lateinit var binding: ActivityTaskPageBinding

    private val viewModel by lazy {
        TaskViewModel(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTaskPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.statusBarColor = ContextCompat.getColor(this, R.color.white_2)

        val task: Task = intent.getParcelableExtra("task")!!

        myToolbar(task.name)

        getTask(task.id)
        deleteTask(task.id)
        editTask()
        completeTask(task.id)
    }

    private fun myToolbar(taskName: String) {
        binding.myToolbar.root.background = ContextCompat.getDrawable(this, R.color.white_2)
        binding.myToolbar.title.text = taskName
        binding.myToolbar.back.setOnClickListener {
            finish()
        }
    }

    private fun getTask(id: String) {
        viewModel.getTask(id)
        viewModel.getTaskMessage.observe(this) { message ->
            if (message == "true") {
                viewModel.getTask.observe(this) { task ->
                    setTask(task)
                }
            } else {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            }
            stopLoading(message)
        }
    }

    private fun setTask(task: Task) {
        Glide.with(this)
            .load("https://intime-9hga.onrender.com/api/v1/images/${task.image}")
            .error(R.drawable.project_image)
            .into(binding.taskCover)

        binding.taskDescription.text = task.disc.ifEmpty { "No Description" }

        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        val outputFormat = SimpleDateFormat("HH:mm dd MMM yyyy", Locale.getDefault())
        val startDate = inputFormat.parse(task.startAt)
        val endDate = inputFormat.parse(task.endAt)

        binding.taskStartDate.text = getString(R.string.from_date, outputFormat.format(startDate!!))
        binding.taskEndDate.text = getString(R.string.to_date, outputFormat.format(endDate!!))

        val priority = when (task.priority) {
            0 -> R.drawable.circle_red
            1 -> R.drawable.circle_orange
            2 -> R.drawable.circle_yellow
            else -> R.drawable.circle_gray
        }
        binding.taskPriority.setImageResource(priority)

        if (task.steps.isEmpty()) {
            binding.noSteps.visibility = View.VISIBLE
        } else {
            binding.noSteps.visibility = View.GONE
            showSteps(task.steps)
        }
    }

    private fun showSteps(steps: List<Step>) {
        binding.stepsLayout.removeAllViews()
        for (step in steps) {
            val textView = TextView(this)
            textView.text = Editable.Factory.getInstance().newEditable(step.stepDisc)
            textView.setPadding(resources.getDimensionPixelSize(R.dimen.standard_20), resources.getDimensionPixelSize(R.dimen.standard_20), resources.getDimensionPixelSize(R.dimen.standard_20), resources.getDimensionPixelSize(R.dimen.standard_20))
            textView.maxLines = 3
            textView.elevation = 5f
            textView.textSize = 16f

            var icon = R.drawable.ic_still
            var color = R.color.black_2
            var container = R.drawable.steps_text_view

            if (step.completed) {
                icon = R.drawable.ic_done
                color = R.color.green
                container = R.drawable.steps_complete_text_view
                textView.paintFlags = textView.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            }

            textView.background = ContextCompat.getDrawable(this, container)

            textView.setTextColor(ContextCompat.getColor(this, color))

            val drawable = ContextCompat.getDrawable(this, icon)
            drawable?.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
            textView.setCompoundDrawables(null, null, drawable, null)

            val layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            val marginHorizontal = resources.getDimensionPixelSize(R.dimen.standard_10)
            val marginVertical = resources.getDimensionPixelSize(R.dimen.standard_10)
            layoutParams.setMargins(marginHorizontal, marginVertical, marginHorizontal, marginVertical)
            textView.layoutParams = layoutParams

            textView.setOnLongClickListener {
                textView.setOnClickListener {
                    icon = R.drawable.ic_done
                    color = R.color.green
                    container = R.drawable.steps_complete_text_view
                    textView.paintFlags = textView.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                    textView.background = ContextCompat.getDrawable(this, container)
                    textView.setTextColor(ContextCompat.getColor(this, color))
                    val drawableClicked = ContextCompat.getDrawable(this, icon)
                    drawableClicked?.setBounds(0, 0, drawableClicked.intrinsicWidth, drawableClicked.intrinsicHeight)
                    textView.setCompoundDrawables(null, null, drawableClicked, null)
                }
                true
            }
            binding.stepsLayout.addView(textView)
        }
    }

    private fun deleteTask(id: String) {
        binding.deleteTaskButton.setOnClickListener {
            startLoadingToDelete()
            viewModel.deleteTask(id)
            viewModel.deleteTaskMessage.observe(this) { message ->
                stopLoadingDeleting()
                if (message == "true") {
                    Toast.makeText(this, "Task Deleted Successfully", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun completeTask(id: String) {
        binding.completeTaskButton.setOnClickListener {
            startLoadingToComplete()
            viewModel.completeTask(id)
            viewModel.completeTaskMessage.observe(this) { message ->
                stopLoadingCompleting()
                if (message == "true") {
                    Toast.makeText(this, "Task Completed Successfully", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun stopLoading(message: String) {
        if (message == "true") {
            binding.blockingView.visibility = View.GONE
        } else {
            binding.blockingView.visibility = View.GONE
            binding.progressCircular.visibility = View.GONE
            binding.noConnection.visibility = View.VISIBLE

            binding.noConnection.setOnClickListener {
                binding.noConnection.visibility = View.GONE
                binding.progressCircular.visibility = View.VISIBLE
                val task: Task = intent.getParcelableExtra("task")!!
                getTask(task.id)
            }
        }
    }

    private fun startLoadingToDelete() {
        binding.blockingView.visibility = View.VISIBLE
        binding.blockingView.setBackgroundColor(getColor(R.color.transparent))
        binding.noConnection.visibility = View.GONE
        binding.progressCircular.visibility = View.GONE
        binding.deleteTaskButton.text = null
        binding.deleteProgressCircular.visibility = View.VISIBLE
    }

    private fun stopLoadingDeleting() {
        binding.deleteProgressCircular.visibility = View.GONE
        binding.blockingView.setBackgroundColor(getColor(R.color.white))
        binding.deleteTaskButton.text = getString(R.string.delete)
        binding.blockingView.visibility = View.GONE
    }

    private fun startLoadingToComplete() {
        binding.blockingView.visibility = View.VISIBLE
        binding.blockingView.setBackgroundColor(getColor(R.color.transparent))
        binding.noConnection.visibility = View.GONE
        binding.progressCircular.visibility = View.GONE
        binding.completeTaskButton.text = null
        binding.completeProgressCircular.visibility = View.VISIBLE
    }

    private fun stopLoadingCompleting() {
        binding.completeProgressCircular.visibility = View.GONE
        binding.blockingView.setBackgroundColor(getColor(R.color.white))
        binding.completeTaskButton.text = getString(R.string.complete)
        binding.blockingView.visibility = View.GONE
    }

    private fun editTask() {
        binding.editTaskButton.setOnClickListener {
            val task: Task = intent.getParcelableExtra("task")!!
            val intent = Intent(this, UpdateTask::class.java)
            intent.putExtra("task", task)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        val task: Task = intent.getParcelableExtra("task")!!
        myToolbar(task.name)
        getTask(task.id)
    }

    @Deprecated("This method has been deprecated in favor of using the\n" +
            "{@link OnBackPressedDispatcher} via {@link #getOnBackPressedDispatcher()}.\n" +
            "The OnBackPressedDispatcher controls how back button events are dispatched\n" +
            "to one or more {@link OnBackPressedCallback} objects.")
    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}