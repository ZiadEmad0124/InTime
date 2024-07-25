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
import com.google.android.material.snackbar.Snackbar
import com.ziad_emad_dev.in_time.R
import com.ziad_emad_dev.in_time.databinding.ActivityProfileBinding
import com.ziad_emad_dev.in_time.ui.signing.SignPage
import com.ziad_emad_dev.in_time.viewmodels.ProfileViewModel

class Profile : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding

    private val viewModel by lazy {
        ProfileViewModel(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        appToolbar()
        fitchProfile()
        clickOnDeleteAccountButton()
        setupSwipeRefreshLayout()
    }

    private fun appToolbar() {
        binding.appToolbar.title.text = getString(R.string.my_profile)
        binding.appToolbar.back.setOnClickListener {
            finish()
        }
    }

    private fun fitchProfile() {
        startLoading()
        viewModel.fetchProfileRank()
        viewModel.fetchProfileRankMessage.observe(this) { message ->
            if (message == "true") {
                setProfileImage()
                stopLoading()
            }
            else {
                errorInConnectingOrFetching(message)
            }
        }
    }

    private fun setProfileImage() {
        viewModel.profile.observe(this) { profile ->
            Glide.with(this)
                .load(profile.avatar)
                .placeholder(R.drawable.ic_profile_default)
                .error(R.drawable.ic_profile_default)
                .into(binding.profileImage)
        }
    }

    private fun clickOnDeleteAccountButton() {
        binding.deleteAccountButton.setOnClickListener {
            val alertDialog = LayoutInflater.from(this).inflate(R.layout.layout_delete_account_dialog, null)
            val alertBuilder = AlertDialog.Builder(this).setView(alertDialog)
            val alertInstance = alertBuilder.show()
            alertInstance.window?.setBackgroundDrawableResource(R.color.transparent)

            val deleteAccountButton = alertDialog.findViewById<Button>(R.id.deleteAccountButton)
            deleteAccountButton.setOnClickListener {
                alertInstance.dismiss()
                deleteAccount()
            }

            val cancelButton = alertDialog.findViewById<Button>(R.id.cancelButton)
            cancelButton.setOnClickListener {
                alertInstance.dismiss()
            }
        }
    }

    private fun deleteAccount() {
        startDeleting()
        viewModel.deleteAccount()
        viewModel.deleteAccountMessage.observe(this) { message ->
            if (message == "true") {
                val intent = Intent(this, SignPage::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
            }
            else {
                stopDeleting(message)
            }
        }
    }

    private fun setupSwipeRefreshLayout() {
        binding.swipeRefreshLayout.setColorSchemeResources(R.color.primary)
        binding.swipeRefreshLayout.setOnRefreshListener {
            refreshContent()
        }
    }

    private fun refreshContent() {
        viewModel.fetchProfile()

        viewModel.fetchProfileMessage.observe(this) {
            binding.swipeRefreshLayout.isRefreshing = false
        }
    }

    private fun startDeleting() {
        binding.blockingView.progressCircular.visibility = View.VISIBLE
        binding.blockingView.noFetch.visibility = View.GONE
        binding.blockingView.noConnection.visibility = View.GONE
        binding.blockingView.message.visibility = View.GONE
        binding.blockingView.root.visibility = View.VISIBLE
    }

    private fun stopDeleting(message: String) {
        binding.blockingView.root.visibility = View.GONE
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun startLoading() {
        binding.blockingView.progressCircular.visibility = View.VISIBLE
        binding.blockingView.noFetch.visibility = View.GONE
        binding.blockingView.noConnection.visibility = View.GONE
        binding.blockingView.message.visibility = View.GONE
        binding.blockingView.root.visibility = View.VISIBLE
    }

    private fun stopLoading() {
        binding.blockingView.root.visibility = View.GONE
    }

    private fun errorInConnectingOrFetching(message: String) {
        binding.blockingView.progressCircular.visibility = View.GONE

        if (message == "Failed Connect, Try Again") {
            binding.blockingView.noFetch.visibility = View.GONE
            binding.blockingView.noConnection.visibility = View.VISIBLE
            binding.blockingView.message.text = getString(R.string.no_connection)
        } else {
            binding.blockingView.noFetch.visibility = View.VISIBLE
            binding.blockingView.noConnection.visibility = View.GONE
            binding.blockingView.message.text = getString(R.string.something_went_wrong_try_again)
        }

        binding.blockingView.message.visibility = View.VISIBLE
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
        binding.blockingView.root.visibility = View.VISIBLE
    }
}