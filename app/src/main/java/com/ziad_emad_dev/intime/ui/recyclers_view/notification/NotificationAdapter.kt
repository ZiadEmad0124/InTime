package com.ziad_emad_dev.intime.ui.recyclers_view.notification

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.ziad_emad_dev.intime.R

class NotificationAdapter(private val notifications: List<Notification>) :
    RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>() {

    class NotificationViewHolder(itemView: View) : ViewHolder(itemView) {
        val icon: ImageView = itemView.findViewById(R.id.notificationIcon)
        val content: TextView = itemView.findViewById(R.id.notificationText)
        val time: TextView = itemView.findViewById(R.id.notificationTime)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        return NotificationViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.layout_notification, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return notifications.size
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        val notification = notifications[position]
        holder.icon.setImageResource(notification.icon)
        holder.content.text = notification.content
        holder.time.text = notification.time
    }
}