package com.ziad_emad_dev.in_time.ui.profile

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ziad_emad_dev.in_time.R
import com.ziad_emad_dev.in_time.databinding.ActivityProfileBinding

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        myToolbar()

        profileName()
        profileEmail()
        profileUsername()
        profilePassword()
    }

    private fun myToolbar() {
        binding.myToolbar.title.text = getString(R.string.my_profile)
        binding.myToolbar.back.setOnClickListener {
            finish()
        }
    }

    private fun profileName() {
        binding.profileName.let {
            it.icon.setImageResource(R.drawable.ic_username)
            it.title.text = getString(R.string.name)
            it.details.text = getText(R.string.name)
        }
    }

    private fun profileEmail() {
        binding.profileEmail.let {
            it.icon.setImageResource(R.drawable.ic_email)
            it.title.text = getString(R.string.email)
            it.details.text = getText(R.string.email)
        }
    }

    private fun profileUsername() {
        binding.profileUsername.let {
            it.icon.setImageResource(R.drawable.ic_edit)
            it.title.text = getString(R.string.username)
            it.details.text = getText(R.string.username)
        }
    }

    private fun profilePassword() {
        binding.profilePassword.let {
            it.icon.setImageResource(R.drawable.ic_password_lock)
            it.title.text = getString(R.string.password)
            it.details.text = getText(R.string.password)
        }
    }
}