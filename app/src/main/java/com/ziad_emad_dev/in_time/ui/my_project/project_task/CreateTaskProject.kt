package com.ziad_emad_dev.in_time.ui.my_project.project_task

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.ziad_emad_dev.in_time.R
import com.ziad_emad_dev.in_time.databinding.ActivityCreateTaskProjectBinding
import com.ziad_emad_dev.in_time.ui.home.HomePage
import com.ziad_emad_dev.in_time.viewmodels.ProjectViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

class CreateTaskProject : AppCompatActivity() {

    private lateinit var binding: ActivityCreateTaskProjectBinding

    private var startDate: String = ""
    private var endDate: String = ""

    private val viewModel by lazy {
        ProjectViewModel(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateTaskProjectBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.statusBarColor = ContextCompat.getColor(this, R.color.white_2)

        myToolbar()

        val projectId = intent.getStringExtra("projectId")!!
        val memberId = intent.getStringExtra("memberId")!!

        getStartDate()
        getEndDate()

        createTask(projectId, memberId)

        responseComing()
    }

    private fun myToolbar() {
        binding.myToolbar.root.background = ContextCompat.getDrawable(this, R.color.white_2)
        binding.myToolbar.title.text = getString(R.string.create_a_task)
        binding.myToolbar.back.setOnClickListener {
            val intent = Intent(this, HomePage::class.java)
            startActivity(intent)
            finish()
        }
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

    private fun nameEmptyError(name: String): Boolean {
        if (name.isEmpty()) {
            binding.taskName.error = getString(R.string.empty_field)
        }
        return name.isEmpty()
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

    private fun createTask(projectId: String, memberId: String) {
        binding.createButton.setOnClickListener {
            val name = binding.taskName.text.toString()
            val disc = binding.taskDescription.text.toString()

            if (!nameEmptyError(name)) {
                if (!startDateEmptyError(startDate)) {
                    if (!endDateEmptyError(endDate)) {
                        startLoading()
                        val startAt = convertToISO8601(startDate)
                        val endAt = convertToISO8601(endDate)
                        viewModel.createProjectTask(projectId, memberId, name, disc, startAt, endAt)
                    }
                }
            }
        }
    }


    private fun startLoading() {
        binding.blockingView.visibility = View.VISIBLE
        binding.createButton.setBackgroundResource(R.drawable.button_loading)
        binding.createButton.text = null
        binding.progressCircular.visibility = View.VISIBLE
    }

    private fun stopLoading() {
        binding.progressCircular.visibility = View.GONE
        binding.createButton.setBackgroundResource(R.drawable.button_background)
        binding.createButton.text = getString(R.string.create)
        binding.blockingView.visibility = View.GONE
    }

    private fun responseComing() {
        viewModel.createProjectTaskMessage.observe(this) { message ->
            stopLoading()
            when (message) {
                "true" -> {
                    Toast.makeText(this, "Task Created Successfully", Toast.LENGTH_SHORT).show()
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