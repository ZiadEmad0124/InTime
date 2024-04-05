package com.ziad_emad_dev.in_time.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.SnapHelper
import com.ziad_emad_dev.in_time.R
import com.ziad_emad_dev.in_time.databinding.FragmentCalenderBinding
import com.ziad_emad_dev.in_time.ui.calender.CalendarAdapter
import com.ziad_emad_dev.in_time.ui.calender.CalendarDateModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class CalenderFragment : Fragment() {

    private var _binding: FragmentCalenderBinding? = null
    private val binding get() = _binding!!

    private var calendarDate: Calendar = Calendar.getInstance(Locale.ENGLISH)
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

        myToolbar()

        setUpAdapter()
        setUpClickListener()
        setUpCalendar()
    }

    private fun myToolbar() {
        binding.myToolbar.title.text = getString(R.string.agenda)
    }

    private fun setUpAdapter() {

        val snapHelper: SnapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(binding.recyclerView)

        adapter = CalendarAdapter()

        val isMonthChanged = adapter.checkIfMonthChanged(calendarDate.get(Calendar.MONTH) + 1)

        if (isMonthChanged) {
            binding.recyclerView.layoutManager?.scrollToPosition(0)
        } else {
            binding.recyclerView.layoutManager?.scrollToPosition(todayDate - 2)
        }

        adapter.setData(calendarList)

        binding.recyclerView.adapter = adapter
    }

    private fun setUpClickListener() {
        binding.nextMonth.setOnClickListener {
            calendarDate.add(Calendar.MONTH, 1)
            calendarNextMonth.add(Calendar.MONTH, 1)
            calendarLastMonth.add(Calendar.MONTH, 1)
            setUpCalendar()
        }

        binding.lastMonth.setOnClickListener {
            calendarDate.add(Calendar.MONTH, -1)
            calendarNextMonth.add(Calendar.MONTH, -1)
            calendarLastMonth.add(Calendar.MONTH, -1)
            setUpCalendar()
        }
    }

    private fun setUpCalendar() {

        binding.monthTextView.text =
            SimpleDateFormat("MMMM", Locale.getDefault()).format(calendarDate.time)
        binding.nextMonth.text =
            SimpleDateFormat("MMM", Locale.getDefault()).format(calendarNextMonth.time)
        binding.lastMonth.text =
            SimpleDateFormat("MMM", Locale.getDefault()).format(calendarLastMonth.time)

        val calendarList = ArrayList<CalendarDateModel>()
        val monthCalendar = calendarDate.clone() as Calendar
        val maxDaysInMonth = calendarDate.getActualMaximum(Calendar.DAY_OF_MONTH)

        dates.clear()

        monthCalendar.set(Calendar.DAY_OF_MONTH, 1)

        while (dates.size < maxDaysInMonth) {
            dates.add(monthCalendar.time)
            calendarList.add(CalendarDateModel(monthCalendar.time))
            monthCalendar.add(Calendar.DAY_OF_MONTH, 1)
        }

        this.calendarList.clear()
        this.calendarList.addAll(calendarList)

        val isMonthChanged = adapter.checkIfMonthChanged(calendarDate.get(Calendar.MONTH) + 1)

        if (isMonthChanged) {
            binding.recyclerView.layoutManager?.scrollToPosition(0)
        } else {
            binding.recyclerView.layoutManager?.scrollToPosition(todayDate - 2)
        }

        adapter.setData(calendarList)
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }
}