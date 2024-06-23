package com.ziad_emad_dev.in_time.ui.notification

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.ziad_emad_dev.in_time.R
import com.ziad_emad_dev.in_time.databinding.ActivityNotificationBinding
import com.ziad_emad_dev.in_time.viewmodels.HomeViewModel

class NotificationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNotificationBinding

    private val viewModel by lazy {
        HomeViewModel(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotificationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recyclerView.setHasFixedSize(true)

        getNotifications()

        myToolbar()
    }

    private fun getNotifications() {
        viewModel.getNotification()
        viewModel.getNotificationsMessage.observe(this) { message ->
            if (message == "true") {
                viewModel.getNotifications.observe(this) { record ->
                    if (record.notifications.isEmpty()) {
                        binding.noNotificationLayout.visibility = View.VISIBLE
                    } else {
                        binding.recyclerView.adapter = NotificationAdapter(record.notifications)
                        binding.recyclerView.setHasFixedSize(true)
                    }
                }
            } else {
                binding.noNotificationLayout.visibility = View.VISIBLE
            }
        }
    }

    private fun myToolbar() {
        binding.myToolbar.title.text = getString(R.string.notifications)
        binding.myToolbar.back.setOnClickListener {
            finish()
        }
    }
}