package com.ziad_emad_dev.in_time.ui.settings

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ziad_emad_dev.in_time.R
import com.ziad_emad_dev.in_time.databinding.ActivitySettingsBinding

class Settings : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        myToolbar()
    }

    private fun myToolbar() {
        binding.myToolbar.title.text = getString(R.string.settings)
        binding.myToolbar.back.setOnClickListener {
            finish()
        }
    }
}