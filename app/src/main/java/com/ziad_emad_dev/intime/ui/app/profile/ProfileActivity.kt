package com.ziad_emad_dev.intime.ui.app.profile

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ziad_emad_dev.intime.R
import com.ziad_emad_dev.intime.databinding.ActivityProfileBinding

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        myToolbar()
    }

    private fun myToolbar() {
        binding.myToolbar.title.text = getString(R.string.my_profile)

        binding.myToolbar.back.setOnClickListener {
            finish()
        }
    }

}