package com.ziad_emad_dev.in_time.ui.profile

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.ziad_emad_dev.in_time.R
import com.ziad_emad_dev.in_time.databinding.ActivityProfileBinding
import com.ziad_emad_dev.in_time.viewmodels.UserViewModel

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding

    private val userViewModel by lazy {
        UserViewModel(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        myToolbar()

        profileUsername()
        profileName()
        profileEmail()
        profilePhone()
        profilePassword()

        userViewModel.fetchUser()

        userViewModel.message.observe(this) {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        }
    }

    private fun myToolbar() {
        binding.myToolbar.title.text = getString(R.string.my_profile)
        binding.myToolbar.back.setOnClickListener {
            finish()
        }
    }

    private fun profileUsername() {
        userViewModel.name.observe(this) {
            binding.profileUsername.text = it
        }
    }

    private fun profileName() {
        binding.profileName.let { name ->
            name.icon.setImageResource(R.drawable.ic_username)
            name.title.text = getString(R.string.name)
            userViewModel.name.observe(this) {
                name.details.text = it
            }
        }
    }

    private fun profileEmail() {
        binding.profileEmail.let { email ->
            email.icon.setImageResource(R.drawable.ic_email)
            email.title.text = getString(R.string.email)
            userViewModel.email.observe(this) {
                email.details.text = it
            }
        }
    }

    private fun profilePhone() {
        binding.profilePhone.let { phone ->
            phone.icon.setImageResource(R.drawable.ic_edit)
            phone.title.text = getString(R.string.username)
            userViewModel.phone.observe(this) {
                phone.details.text = it
            }
        }
    }

    private fun profilePassword() {
        binding.profilePassword.let { password ->
            password.icon.setImageResource(R.drawable.ic_password_lock)
            password.title.text = getString(R.string.password)
            password.details.text = getString(R.string.password_pattern)
        }
    }
}