package com.ziad_emad_dev.in_time.ui.profile

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.ziad_emad_dev.in_time.R
import com.ziad_emad_dev.in_time.databinding.ActivityProfileBinding
import com.ziad_emad_dev.in_time.network.profile.ProfileManager
import com.ziad_emad_dev.in_time.ui.signing.SignPage
import com.ziad_emad_dev.in_time.viewmodels.ProfileViewModel

class Profile : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding

    private lateinit var profileManager: ProfileManager

    private val viewModel by lazy {
        ProfileViewModel(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        profileManager = ProfileManager(this)

        myToolbar()
        fitchProfile()
        clickOnDeleteAccountButton()
    }

    private fun myToolbar() {
        binding.myToolbar.title.text = getString(R.string.my_profile)
        binding.myToolbar.back.setOnClickListener {
            finish()
        }
    }

    private fun fitchProfile() {
        Glide.with(this)
            .load(profileManager.getProfileAvatar())
            .placeholder(R.drawable.ic_profile)
            .error(R.drawable.ic_profile)
            .into(binding.profileImage)
        binding.profileName.text = profileManager.getProfileName()
        binding.profileTitle.text = profileManager.getProfileTitle()
        binding.profileAbout.text = profileManager.getProfileAbout()
        binding.rank.text = profileManager.getRank().toString()
        binding.level.text = if (profileManager.getTotalPoints() <= 100) {
            1.toString()
        } else {
            ((profileManager.getTotalPoints() / 100)+1).toString()
        }
        binding.totalPoints.text = profileManager.getTotalPoints().toString()
    }

    private fun clickOnDeleteAccountButton() {
        binding.deleteAccountButton.setOnClickListener {
            val alertDialog = LayoutInflater.from(this).inflate(R.layout.layout_delete_account_dialog, null)
            val alertBuilder = AlertDialog.Builder(this).setView(alertDialog)
            val alertInstance = alertBuilder.show()
            alertInstance.window?.setBackgroundDrawableResource(R.color.transparent)

            val deleteAccountButton = alertDialog.findViewById<Button>(R.id.deleteAccountButton)
            deleteAccountButton.setOnClickListener {
                deleteAccount()
                alertInstance.dismiss()
            }

            val cancelButton = alertDialog.findViewById<Button>(R.id.cancelButton)
            cancelButton.setOnClickListener {
                alertInstance.dismiss()
            }
        }
    }

    private fun deleteAccount() {
        startLoading()
        viewModel.deleteAccount()
        viewModel.deleteAccountMessage.observe(this) { message ->
            if (message == "true") {
                profileManager.clearProfile()
                val intent = Intent(this, SignPage::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
            } else {
                failedConnect(message)
            }
        }
    }

    private fun startLoading() {
        binding.blockingView.visibility = View.VISIBLE
        binding.deleteAccountButton.setBackgroundResource(R.drawable.button_loading)
        binding.deleteAccountButton.text = null
        binding.progressCircular.visibility = View.VISIBLE
    }

    private fun failedConnect(message: String) {
        binding.blockingView.visibility = View.GONE
        binding.deleteAccountButton.setBackgroundResource(R.drawable.button_delete_account)
        binding.deleteAccountButton.text = getString(R.string.delete_account)
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}