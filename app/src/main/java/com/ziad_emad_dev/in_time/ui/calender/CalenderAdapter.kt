package com.ziad_emad_dev.in_time.ui.calender

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.ziad_emad_dev.in_time.R
import java.util.Calendar

class CalendarAdapter : RecyclerView.Adapter<CalendarAdapter.CalendarViewHolder>() {

    class CalendarViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val calenderItemCard: MaterialCardView = view.findViewById(R.id.calenderItemCard)
        val calenderItem: LinearLayout = view.findViewById(R.id.calenderItem)
        val calendarDate: TextView = view.findViewById(R.id.calenderDate)
        val calendarDay: TextView = view.findViewById(R.id.calenderDay)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarViewHolder {
        return CalendarViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.layout_date, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return calenderDateList.size
    }

    private var calenderDateList = ArrayList<CalendarDateModel>()

    @SuppressLint("NotifyDataSetChanged")
    fun setData(calendarList: ArrayList<CalendarDateModel>) {
        calenderDateList.clear()
        calenderDateList.addAll(calendarList)
        notifyDataSetChanged()
    }

    private val todayDate = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
    private var adapterPosition = todayDate - 1
    private val currentMonth = Calendar.getInstance().get(Calendar.MONTH)
    private var isThisPastMonth = false

    fun checkIfMonthChanged(selectedMonth: Int) {
        isThisPastMonth = selectedMonth < currentMonth
        adapterPosition = if (selectedMonth == currentMonth) {
            todayDate - 1
        } else {
            -1
        }
    }

    override fun onBindViewHolder(holder: CalendarViewHolder, position: Int) {

        val day = calenderDateList[position]
        holder.calendarDate.text = day.calendarDate
        holder.calendarDay.text = day.calendarDay

        if (position == adapterPosition) {
            holder.calendarDay.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.white))
            holder.calendarDate.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.white))
            holder.calenderItemCard.strokeColor = ContextCompat.getColor(holder.itemView.context, R.color.primary)
            holder.calenderItem.setBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color.primary))
        } else {
            if (position < adapterPosition || isThisPastMonth) {
                holder.calendarDay.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.grey_1))
                holder.calendarDate.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.grey_1))
                holder.calenderItemCard.strokeColor = ContextCompat.getColor(holder.itemView.context, R.color.white)
                holder.calenderItem.setBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color.white))
            } else {
                holder.calendarDay.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.secondary))
                holder.calendarDate.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.secondary))
                holder.calenderItemCard.strokeColor = ContextCompat.getColor(holder.itemView.context, R.color.white)
                holder.calenderItem.setBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color.white))
            }
        }
    }
}