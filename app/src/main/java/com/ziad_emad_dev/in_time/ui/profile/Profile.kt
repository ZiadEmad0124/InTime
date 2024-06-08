package com.ziad_emad_dev.in_time.ui.profile

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.ziad_emad_dev.in_time.R
import com.ziad_emad_dev.in_time.databinding.ActivityProfileBinding
import com.ziad_emad_dev.in_time.network.ProfileManager
import com.ziad_emad_dev.in_time.ui.signing.SignPage
import com.ziad_emad_dev.in_time.viewmodels.ProfileViewModel

class Profile : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding

    private lateinit var profileManager: ProfileManager

    private val viewModel: ProfileViewModel by viewModels()

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
        binding.profileName.text = profileManager.getProfileName()
    }

    private fun clickOnDeleteAccountButton() {
        binding.deleteAccountButton.setOnClickListener {
            val alertDialog = LayoutInflater.from(this).inflate(R.layout.delete_account_dialog, null)
            val alertBuilder = AlertDialog.Builder(this).setView(alertDialog)
            val alertInstance = alertBuilder.show()
            alertInstance.window?.setBackgroundDrawableResource(R.color.transparent)

            val deleteAccountButton = alertDialog.findViewById<Button>(R.id.deleteAccountButton)
            deleteAccountButton.setOnClickListener {
                alertInstance.dismiss()
                deleteAccount(alertInstance)
            }

            val cancelButton = alertDialog.findViewById<Button>(R.id.cancelButton)
            cancelButton.setOnClickListener {
                alertInstance.dismiss()
            }
        }
    }

    private fun deleteAccount(alertDialog: AlertDialog) {
        startLoading()

        viewModel.refreshToken()

        viewModel.refreshTokenMessage.observe(this) { message ->
            if (message == "Refresh Succeed") {
                viewModel.deleteAccount()
            } else {
                failedConnect()
                alertDialog.dismiss()
            }
        }
        viewModel.deleteAccountMessage.observe(this) { message ->
            if (message == "Deleted Succeed") {
                val intent = Intent(this, SignPage::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
            } else {
                failedConnect()
                alertDialog.dismiss()
            }
        }
    }

    private fun startLoading() {
        binding.blockingView.visibility = View.VISIBLE
        binding.deleteAccountButton.setBackgroundResource(R.drawable.button_loading_background)
        binding.deleteAccountButton.text = null
        binding.progressCircular.visibility = View.VISIBLE
    }

    private fun failedConnect() {
        binding.blockingView.visibility = View.VISIBLE
        binding.blockingView.setBackgroundColor(Color.WHITE)
        binding.noConnection.visibility = View.VISIBLE
        Toast.makeText(this, "Failed Connect", Toast.LENGTH_SHORT).show()
    }
}