package com.ziad_emad_dev.in_time.ui.calender

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

data class CalendarDateModel(var date: Date, var isSelected: Boolean = true) {

    val calendarDate: String
        get() {
            val calendar = Calendar.getInstance()
            calendar.time = date
            return calendar[Calendar.DAY_OF_MONTH].toString()
        }

    val calendarDay: String
        get() = SimpleDateFormat("EE", Locale.getDefault()).format(date)

    val calenderMonth: String
        get() = SimpleDateFormat("MMMM", Locale.getDefault()).format(date)

    val calendarYear: String
        get() = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()).format(date)

}