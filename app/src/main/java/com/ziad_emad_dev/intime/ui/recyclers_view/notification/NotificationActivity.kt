package com.ziad_emad_dev.intime.ui.recyclers_view.notification

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ziad_emad_dev.intime.R
import com.ziad_emad_dev.intime.databinding.ActivityNotificationBinding

class NotificationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNotificationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotificationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recyclerView.adapter = NotificationAdapter(Notifications.getNotifications())
        binding.recyclerView.setHasFixedSize(true)

        myToolbar()
    }

    private fun myToolbar() {
        binding.myToolbar.title.text = getString(R.string.notifications)

        binding.myToolbar.back.setOnClickListener {
            finish()
        }
    }
}