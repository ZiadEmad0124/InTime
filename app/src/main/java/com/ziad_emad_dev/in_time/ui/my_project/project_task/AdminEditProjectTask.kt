package com.ziad_emad_dev.in_time.ui.my_project.project_task

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.ziad_emad_dev.in_time.R
import com.ziad_emad_dev.in_time.databinding.ActivityAdminEditProjectTaskBinding
import com.ziad_emad_dev.in_time.viewmodels.ProjectViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

class AdminEditProjectTask : AppCompatActivity() {

    private lateinit var binding: ActivityAdminEditProjectTaskBinding

    private lateinit var startDate: String
    private lateinit var endDate: String

    private val viewModel by lazy {
        ProjectViewModel(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminEditProjectTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.statusBarColor = ContextCompat.getColor(this, R.color.white_2)

        val projectId = intent.getStringExtra("projectId")!!
        val taskId = intent.getStringExtra("taskId")!!


        myToolbar()

        fillForm()

        getStartDate()
        getEndDate()

        editTask(projectId, taskId)

        responseComing()
    }

    private fun myToolbar() {
        binding.myToolbar.root.background = ContextCompat.getDrawable(this, R.color.white_2)
        binding.myToolbar.title.text = intent.getStringExtra("taskName")!!
        binding.myToolbar.back.setOnClickListener {
            finish()
        }
        binding.myToolbar.menu.setImageResource(R.drawable.ic_options)
        binding.myToolbar.menu.setOnClickListener {
            showPopupMenu(it)
        }
    }

    private fun showPopupMenu(view: View) {
        val popupMenu = PopupMenu(this, view, 0, 0, R.style.PopupMenuStyle)
        popupMenu.menuInflater.inflate(R.menu.project_task_admin_menu, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.deleteTask -> {
                    val projectId = intent.getStringExtra("projectId")!!
                    val taskId = intent.getStringExtra("taskId")!!
                    deleteTask(projectId, taskId)
                    true
                }
                else -> false
            }
        }
        popupMenu.show()
    }

    private fun fillForm(){

        val taskName = intent.getStringExtra("taskName")!!
        binding.taskName.setText(taskName)

        val taskDescription = intent.getStringExtra("taskDescription")!!
        binding.taskDescription.setText(taskDescription)

        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        val outputFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())

        val taskStartAt = intent.getStringExtra("taskStartAt")!!
        val taskEndAt = intent.getStringExtra("taskEndAt")!!

        val startDateView = inputFormat.parse(taskStartAt)
        val endDateView = inputFormat.parse(taskEndAt)

        val startDateCalendar = Calendar.getInstance().apply {
            time = startDateView!!
            add(Calendar.HOUR_OF_DAY, 3)
        }

        val endDateCalendar = Calendar.getInstance().apply {
            time = endDateView!!
            add(Calendar.HOUR_OF_DAY, 3)
        }

        startDate = outputFormat.format(startDateCalendar.time)
        endDate = outputFormat.format(endDateCalendar.time)

        binding.startDateButton.text = outputFormat.format(startDateCalendar.time)
        binding.endDateButton.text = outputFormat.format(endDateCalendar.time)
     }

    private fun getStartDate() {
        binding.startDateButton.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(this, R.style.InTime_PickerDialogTheme, { _, selectedYear, selectedMonth, selectedDay ->
                val timePickerDialog = TimePickerDialog(this, R.style.InTime_PickerDialogTheme, { _, selectedHour, selectedMinute ->
                    val selectedDate = Calendar.getInstance()
                    selectedDate.set(selectedYear, selectedMonth, selectedDay, selectedHour, selectedMinute)
                    if (selectedDate.before(calendar)) {
                        Toast.makeText(this, "Start date must be after current date", Toast.LENGTH_SHORT).show()
                    } else {
                        startDate = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(selectedDate.time)
                        binding.startDateButton.text = startDate
                    }
                    startDate = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(selectedDate.time)
                }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true)

                timePickerDialog.setOnShowListener {
                    timePickerDialog.getButton(TimePickerDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(this, R.color.primary))
                    timePickerDialog.getButton(TimePickerDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(this, R.color.primary))
                }
                timePickerDialog.show()
            }, year, month, day)

            datePickerDialog.setOnShowListener {
                datePickerDialog.getButton(DatePickerDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(this, R.color.primary))
                datePickerDialog.getButton(DatePickerDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(this, R.color.primary))
            }
            datePickerDialog.show()
        }
    }

    private fun getEndDate() {
        binding.endDateButton.setOnClickListener {
            if (startDate.isEmpty()) {
                Toast.makeText(this, "Please select a start date first", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(this, R.style.InTime_PickerDialogTheme, { _, selectedYear, selectedMonth, selectedDay ->
                val timePickerDialog = TimePickerDialog(this, R.style.InTime_PickerDialogTheme, { _, selectedHour, selectedMinute ->
                    val selectedDate = Calendar.getInstance()
                    selectedDate.set(selectedYear, selectedMonth, selectedDay, selectedHour, selectedMinute)
                    val startDateCalendar = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).parse(startDate)
                    if (selectedDate.time.before(startDateCalendar)) {
                        Toast.makeText(this, "End date must be after start date", Toast.LENGTH_SHORT).show()
                    } else {
                        endDate = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(selectedDate.time)
                        binding.endDateButton.text = endDate
                    }
                    endDate = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(selectedDate.time)
                }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true)

                timePickerDialog.setOnShowListener {
                    timePickerDialog.getButton(TimePickerDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(this, R.color.primary))
                    timePickerDialog.getButton(TimePickerDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(this, R.color.primary))
                }
                timePickerDialog.show()
            }, year, month, day)

            datePickerDialog.setOnShowListener {
                datePickerDialog.getButton(DatePickerDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(this, R.color.primary))
                datePickerDialog.getButton(DatePickerDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(this, R.color.primary))
            }
            datePickerDialog.show()
        }
    }

    private fun convertToISO8601(date: String): String {
        val originalFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        val targetFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", Locale.getDefault())
        targetFormat.timeZone = TimeZone.getTimeZone("UTC")
        val dateObj = originalFormat.parse(date)
        return targetFormat.format(dateObj!!)
    }

    private fun startDateEmptyError(start: String): Boolean {
        if (start.isEmpty()) {
            Toast.makeText(this, "Please select a start date", Toast.LENGTH_SHORT).show()
        }
        return start.isEmpty()
    }

    private fun endDateEmptyError(end: String): Boolean {
        if (end.isEmpty()) {
            Toast.makeText(this, "Please select a end date", Toast.LENGTH_SHORT).show()
        }
        return end.isEmpty()
    }

    private fun editTask(projectId: String, memberId: String) {
        binding.editButton.setOnClickListener {
            val taskName = intent.getStringExtra("taskName")!!
            val name = if (binding.taskName.text.toString() == taskName) null else  binding.taskName.text.toString()
            val description = binding.taskDescription.text.toString()

            if (!startDateEmptyError(startDate)) {
                if (!endDateEmptyError(endDate)) {
                    startLoading()
                    val startAt = convertToISO8601(startDate)
                    val endAt = convertToISO8601(endDate)
                    viewModel.editProjectTaskAdmin(projectId, memberId, name, description, startAt, endAt)
                }
            }
        }
    }

    private fun deleteTask(projectId: String, taskId: String) {
        viewModel.deleteProjectTask(projectId, taskId)
        viewModel.deleteProjectTaskMessage.observe(this) { message ->
            when (message) {
                "true" -> {
                    Toast.makeText(this, "Task Deleted Successfully", Toast.LENGTH_SHORT).show()
                    finish()
                }

                "Failed Connect, Try Again" -> {
                    Toast.makeText(this, "Failed Connect, Try Again", Toast.LENGTH_SHORT).show()
                }

                else -> {
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun startLoading() {
        binding.blockingView.visibility = View.VISIBLE
        binding.editButton.setBackgroundResource(R.drawable.button_loading)
        binding.editButton.text = null
        binding.progressCircular.visibility = View.VISIBLE
    }

    private fun stopLoading() {
        binding.progressCircular.visibility = View.GONE
        binding.editButton.setBackgroundResource(R.drawable.button_background)
        binding.editButton.text = getString(R.string.create)
        binding.blockingView.visibility = View.GONE
    }

    private fun responseComing() {
        viewModel.editProjectTaskAdminMessage.observe(this) { message ->
            stopLoading()
            when (message) {
                "true" -> {
                    Toast.makeText(this, "Task Updated Successfully", Toast.LENGTH_SHORT).show()
                    finish()
                }

                "Failed Connect, Try Again" -> {
                    Toast.makeText(this, "Failed Connect, Try Again", Toast.LENGTH_SHORT).show()
                }

                else -> {
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}