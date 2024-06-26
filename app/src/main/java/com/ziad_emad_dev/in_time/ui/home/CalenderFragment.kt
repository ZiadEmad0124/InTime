package com.ziad_emad_dev.in_time.ui.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.SnapHelper
import com.ziad_emad_dev.in_time.databinding.FragmentCalenderBinding
import com.ziad_emad_dev.in_time.ui.calender.CalendarAdapter
import com.ziad_emad_dev.in_time.ui.calender.CalendarDateModel
import com.ziad_emad_dev.in_time.ui.calender.OngoingTasksAdapter
import com.ziad_emad_dev.in_time.viewmodels.TaskViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class CalenderFragment : Fragment(), CalendarAdapter.OnDateClickListener {

    private var _binding: FragmentCalenderBinding? = null
    private val binding get() = _binding!!

    private val viewModel by lazy {
        TaskViewModel(requireContext())
    }

    private var calendarCurrentMonth: Calendar = Calendar.getInstance(Locale.ENGLISH)
    private var calendarNextMonth: Calendar = Calendar.getInstance(Locale.ENGLISH)
    private var calendarLastMonth: Calendar = Calendar.getInstance(Locale.ENGLISH)
    private val todayDate = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
    private lateinit var adapter: CalendarAdapter
    private val calendarList = ArrayList<CalendarDateModel>()
    private val dates = ArrayList<Date>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCalenderBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        calendarNextMonth.add(Calendar.MONTH, 1)
        calendarLastMonth.add(Calendar.MONTH, -1)

        setUpAdapter()
        setUpClickListener()
        setUpCalendar()

        binding.recyclerView.layoutManager?.scrollToPosition(todayDate - 2)

        onDateClick(Date())
    }

    private fun setUpAdapter() {
        val snapHelper: SnapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(binding.recyclerView)

        adapter = CalendarAdapter(this)
        binding.recyclerView.layoutManager?.scrollToPosition(todayDate - 2)
        adapter.setData(calendarList)
        binding.recyclerView.adapter = adapter
    }

    private fun setUpClickListener() {
        binding.nextMonth.setOnClickListener {
            calendarCurrentMonth.add(Calendar.MONTH, 1)
            calendarNextMonth.add(Calendar.MONTH, 1)
            calendarLastMonth.add(Calendar.MONTH, 1)
            setUpCalendar()
        }

        binding.lastMonth.setOnClickListener {
            calendarCurrentMonth.add(Calendar.MONTH, -1)
            calendarNextMonth.add(Calendar.MONTH, -1)
            calendarLastMonth.add(Calendar.MONTH, -1)
            setUpCalendar()
        }
    }

    private fun setUpCalendar() {
        binding.monthTextView.text = SimpleDateFormat("MMMM", Locale.getDefault()).format(calendarCurrentMonth.time)
        binding.nextMonth.text = SimpleDateFormat("MMM", Locale.getDefault()).format(calendarNextMonth.time)
        binding.lastMonth.text = SimpleDateFormat("MMM", Locale.getDefault()).format(calendarLastMonth.time)

        adapter.checkIfMonthChanged(calendarCurrentMonth.get(Calendar.MONTH))
        binding.recyclerView.layoutManager?.scrollToPosition(0)

        val calendarList = ArrayList<CalendarDateModel>()
        val monthCalendar = calendarCurrentMonth.clone() as Calendar
        val maxDaysInMonth = calendarCurrentMonth.getActualMaximum(Calendar.DAY_OF_MONTH)
        dates.clear()
        monthCalendar.set(Calendar.DAY_OF_MONTH, 1)
        while (dates.size < maxDaysInMonth) {
            dates.add(monthCalendar.time)
            calendarList.add(CalendarDateModel(monthCalendar.time))
            monthCalendar.add(Calendar.DAY_OF_MONTH, 1)
        }
        this.calendarList.clear()
        this.calendarList.addAll(calendarList)
        adapter.setData(calendarList)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onDateClick(date: Date) {

        val selectedDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val selectedDate = selectedDateFormat.format(date)

        viewModel.getTasks()

        viewModel.getTasksMessage.observe(viewLifecycleOwner) { message ->
            if (message == "true") {
                viewModel.getTasks.observe(viewLifecycleOwner) { tasks ->
                    if (tasks.isNotEmpty()) {
                        val filteredTasks = tasks.filter { task ->
                            task.endAt.substring(0, 10) == selectedDate
                        }
                        if (filteredTasks.isNotEmpty()) {
                            binding.ongoingTasks.layoutManager = LinearLayoutManager(context)
                            val ongoingTasksAdapter = OngoingTasksAdapter(filteredTasks)
                            binding.ongoingTasks.adapter = ongoingTasksAdapter
                            ongoingTasksAdapter.notifyDataSetChanged()
                        } else {
                            binding.ongoingTasks.adapter = null
                        }
                    } else {
                        Toast.makeText(context, "No tasks found", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(context, "No tasks found", Toast.LENGTH_SHORT).show()
            }
        }
    }
}