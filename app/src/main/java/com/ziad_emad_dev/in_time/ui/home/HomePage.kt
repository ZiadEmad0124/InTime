package com.ziad_emad_dev.in_time.ui.home

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.ziad_emad_dev.in_time.R
import com.ziad_emad_dev.in_time.databinding.ActivityHomePageBinding
import com.ziad_emad_dev.in_time.network.auth.SessionManager
import com.ziad_emad_dev.in_time.network.profile.ProfileManager
import com.ziad_emad_dev.in_time.ui.create.CreateProject
import com.ziad_emad_dev.in_time.ui.create.CreateTask
import com.ziad_emad_dev.in_time.ui.notification.NotificationActivity
import com.ziad_emad_dev.in_time.ui.profile.ChangePassword
import com.ziad_emad_dev.in_time.ui.profile.Profile
import com.ziad_emad_dev.in_time.ui.profile.Settings
import com.ziad_emad_dev.in_time.ui.signing.SignPage
import com.ziad_emad_dev.in_time.viewmodels.HomeViewModel

@Suppress("DEPRECATION")
class HomePage : AppCompatActivity() {

    private lateinit var binding: ActivityHomePageBinding

    private lateinit var sessionManager: SessionManager
    private lateinit var profileManager: ProfileManager

    private val viewModel by lazy {
        HomeViewModel(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomePageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sessionManager = SessionManager(this)
        profileManager = ProfileManager(this)

        startLoading()
        refreshProfile()

        myToolbar()

        customMiddleBottomNavBarIcon()
        clickOnItemInBottomNavBar()

        goToProfile()
        goToSettings()
        goToChangePassword()

        signOut()
    }

    private fun myToolbar() {
        myToolbarAvatar()

        binding.myToolbar.notification.setOnClickListener {
            val intent = Intent(this, NotificationActivity::class.java)
            startActivity(intent)
        }

        binding.myToolbar.profile.setOnClickListener {
            binding.drawerLayout.openDrawer(GravityCompat.START)
        }
    }

    @SuppressLint("RestrictedApi")
    private fun customMiddleBottomNavBarIcon() {
        val bottomNavBarMenu = binding.bottomNavBar.getChildAt(0) as BottomNavigationMenuView
        val middleIcon = bottomNavBarMenu.getChildAt(2)
        val iconView = middleIcon as BottomNavigationItemView

        val myIcon = LayoutInflater.from(this).inflate(R.layout.layout_middle_icon, bottomNavBarMenu, false)
        iconView.addView(myIcon)

        myIcon.findViewById<ImageView>(R.id.add_button).setOnClickListener {
            showBottomSheet()
        }
    }

    private fun clickOnItemInBottomNavBar() {
        binding.bottomNavBar.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.homeFragment -> {
                    runSelectedFragment(HomeFragment())
                    true
                }

                R.id.tasksFragment -> {
                    runSelectedFragment(TasksFragment())
                    true
                }

                R.id.addFragment -> {
                    showBottomSheet()
                    true
                }

                R.id.projectsFragment -> {
                    runSelectedFragment(ProjectsFragment())
                    true
                }

                R.id.calenderFragment -> {
                    runSelectedFragment(CalenderFragment())
                    true
                }

                else -> false
            }
        }
    }

    private fun runSelectedFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragmentContainerView, fragment)
        transaction.commit()
    }

    @SuppressLint("InflateParams")
    private fun showBottomSheet() {
        val dialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.layout_add_bottom_sheet, null)
        dialog.setContentView(view)

        val createTaskButton = view.findViewById<View>(R.id.createTaskButton)
        createTaskButton.setOnClickListener {
            dialog.dismiss()
            val intent = Intent(this, CreateTask::class.java)
            startActivity(intent)
            finish()
        }
        val createProjectButton = view.findViewById<View>(R.id.createProjectButton)
        createProjectButton.setOnClickListener {
            dialog.dismiss()
            val intent = Intent(this, CreateProject::class.java)
            startActivity(intent)
            finish()
        }
        dialog.show()
    }

    private fun goToProfile() {
        binding.navigationView.profile.setOnClickListener {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
            val intent = Intent(this, Profile::class.java)
            startActivity(intent)
        }
    }

    private fun goToSettings() {
        binding.navigationView.settings.setOnClickListener {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
            val intent = Intent(this, Settings::class.java)
            startActivity(intent)
        }
    }

    private fun goToChangePassword() {
        binding.navigationView.changePassword.setOnClickListener {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
            val intent = Intent(this, ChangePassword::class.java)
            startActivity(intent)
        }
    }

    private fun navigationViewProfile() {
        Glide.with(this)
            .load(profileManager.getProfileAvatar())
            .placeholder(R.drawable.ic_profile)
            .error(R.drawable.ic_profile)
            .into(binding.navigationView.profileImage)
        binding.navigationView.profileName.text = profileManager.getProfileName()
    }

    private fun myToolbarAvatar() {
        Glide.with(this)
            .load(profileManager.getProfileAvatar())
            .placeholder(R.drawable.ic_profile)
            .error(R.drawable.ic_profile)
            .into(binding.myToolbar.profile)
    }

    private fun refreshProfile() {
        viewModel.refreshToken()
        viewModel.refreshTokenMessage.observe(this) { message ->
            if (message == "true") {
                viewModel.fetchProfile()
            } else {
                failedConnect()
            }
        }

        viewModel.fetchProfileMessage.observe(this) { message ->
            if (message == "true") {
                viewModel.fetchProfileRank()
            } else {
                failedConnect()
            }
        }

        viewModel.fetchProfileRankMessage.observe(this) { message ->
            if (message == "Fetch Profile Rank Success") {
                stopLoading()
                navigationViewProfile()
                myToolbarAvatar()
                runSelectedFragment(HomeFragment())
            } else {
                failedConnect()
            }
        }
    }

    private fun signOut() {
        binding.navigationView.logOut.setOnClickListener {
            val alertDialog = LayoutInflater.from(this).inflate(R.layout.log_out_dialog, null)
            val alertBuilder = AlertDialog.Builder(this).setView(alertDialog)
            val alertInstance = alertBuilder.show()
            alertInstance.window?.setBackgroundDrawableResource(R.color.transparent)

            val logOutButton = alertDialog.findViewById<Button>(R.id.logOutButton)
            logOutButton.setOnClickListener {
                logOut()
                binding.drawerLayout.closeDrawer(GravityCompat.START)
                alertInstance.dismiss()
            }

            val cancelButton = alertDialog.findViewById<Button>(R.id.cancelButton)
            cancelButton.setOnClickListener {
                alertInstance.dismiss()
            }
        }
    }

    private fun logOut() {
        startLoading()
        binding.noConnection.visibility = View.GONE
        viewModel.signOut()
        viewModel.signOutMessage.observe(this) { message ->
            if (message != "true") {
                sessionManager.clearAuthToken()
                sessionManager.clearRefreshToken()
                profileManager.clearProfile()
            }
            val intent = Intent(this, SignPage::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }
    }

    private fun startLoading() {
        binding.blockingView.visibility = View.VISIBLE
    }

    private fun stopLoading() {
        binding.blockingView.visibility = View.GONE
    }

    private fun failedConnect() {
        binding.blockingView.visibility = View.GONE
        binding.blockingViewNoConnection.visibility = View.VISIBLE
        Toast.makeText(this, "Failed Connect, Try Again", Toast.LENGTH_SHORT).show()
    }

    override fun onResume() {
        super.onResume()
        refreshProfile()
    }

    @Deprecated("This function is Deprecated")
    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
}