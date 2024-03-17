package com.ziad_emad_dev.intime.ui.signing

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ziad_emad_dev.intime.databinding.ActivitySignPageBinding

class SignPage : AppCompatActivity() {

    private lateinit var binding: ActivitySignPageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignPageBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}