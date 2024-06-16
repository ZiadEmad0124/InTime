package com.ziad_emad_dev.in_time.ui.create

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.ziad_emad_dev.in_time.R
import com.ziad_emad_dev.in_time.databinding.ActivityCreateProjectBinding

class CreateProject : AppCompatActivity() {

    private lateinit var binding: ActivityCreateProjectBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateProjectBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.statusBarColor = ContextCompat.getColor(this, R.color.white_2)

        myToolbar()
    }

    private fun myToolbar() {
        binding.myToolbar.root.background = ContextCompat.getDrawable(this, R.color.white_2)
        binding.myToolbar.title.text = getString(R.string.create_project)
        binding.myToolbar.back.setOnClickListener {
            finish()
        }
    }
}