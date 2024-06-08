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
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.ziad_emad_dev.in_time.R
import com.ziad_emad_dev.in_time.databinding.ActivityHomePageBinding
import com.ziad_emad_dev.in_time.network.ProfileManager
import com.ziad_emad_dev.in_time.ui.create.CreateTask
import com.ziad_emad_dev.in_time.ui.notification.NotificationActivity
import com.ziad_emad_dev.in_time.ui.profile.EditProfile
import com.ziad_emad_dev.in_time.ui.profile.Profile
import com.ziad_emad_dev.in_time.ui.settings.Settings
import com.ziad_emad_dev.in_time.ui.signing.SignPage
import com.ziad_emad_dev.in_time.viewmodels.HomeViewModel

@Suppress("DEPRECATION")
class HomePage : AppCompatActivity() {

    private lateinit var binding: ActivityHomePageBinding

    private lateinit var profileManager: ProfileManager

    private val viewModel by lazy {
        HomeViewModel(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomePageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        profileManager = ProfileManager(this)

        myToolbar()
        customMiddleBottomNavBarIcon()
        clickOnItemInBottomNavBar()

        fitchProfile()
        goToProfile()
        goToEditProfile()
        goToSettings()
        signOut()
    }

    private fun myToolbar() {
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
            val intent = Intent(this, CreateTask::class.java)
            startActivity(intent)
            dialog.dismiss()
        }
        val createProjectButton = view.findViewById<View>(R.id.createProjectButton)
        createProjectButton.setOnClickListener {

            dialog.dismiss()
        }
        dialog.show()
    }

    private fun fitchProfile() {
        viewModel.refreshToken()
        viewModel.refreshTokenMessage.observe(this) { message ->
            if (message == "Refresh Succeed") {
                viewModel.fetchProfile()
            } else {
                failedConnect()
            }
        }
        viewModel.fetchProfileMessage.observe(this) { message ->
            if (message == "true") {
                binding.blockingView.visibility = View.GONE
                saveProfile()
                navigationViewProfile()
            } else {
                failedConnect()
            }
        }
    }

    private fun saveProfile() {
        viewModel.name.observe(this) { name ->
            viewModel.title.observe(this) { title ->
                viewModel.email.observe(this) { email ->
                    viewModel.phone.observe(this) { phone ->
                        viewModel.about.observe(this) { about ->
                            profileManager.saveProfile(name, title, email, phone, about)
                        }
                    }
                }
            }
        }
    }

    private fun navigationViewProfile() {
        binding.navigationView.profileName.text = profileManager.getProfileName()
    }

    private fun goToProfile() {
        binding.navigationView.profileContainer.setOnClickListener {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
            val intent = Intent(this, Profile::class.java)
            startActivity(intent)
        }
    }

    private fun goToEditProfile() {
        binding.navigationView.profile.setOnClickListener {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
            val intent = Intent(this, EditProfile::class.java)
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

    private fun signOut() {
        binding.navigationView.logOut.setOnClickListener {
            val alertDialog = LayoutInflater.from(this).inflate(R.layout.log_out_dialog, null)
            val alertBuilder = AlertDialog.Builder(this).setView(alertDialog)
            val alertInstance = alertBuilder.show()
            alertInstance.window?.setBackgroundDrawableResource(R.color.transparent)

            val logOutButton = alertDialog.findViewById<Button>(R.id.logOutButton)
            logOutButton.setOnClickListener {
                alertInstance.dismiss()
                binding.drawerLayout.closeDrawer(GravityCompat.START)
                logOut(alertInstance)
            }

            val cancelButton = alertDialog.findViewById<Button>(R.id.cancelButton)
            cancelButton.setOnClickListener {
                alertInstance.dismiss()
            }
        }
    }

    private fun logOut(alertDialog: AlertDialog) {
        startLogOut()

        viewModel.refreshToken()

        viewModel.refreshTokenMessage.observe(this) { message ->
            if (message == "Refresh Succeed") {
                viewModel.signOut()
            } else {
                failedConnect()
                alertDialog.dismiss()
            }
        }
        viewModel.signOutMessage.observe(this) { message ->
            if (message == "true") {
                val intent = Intent(this, SignPage::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
            } else {
                failedConnect()
                alertDialog.dismiss()
            }
        }
    }

    private fun startLogOut() {
        binding.blockingView.visibility = View.VISIBLE
        binding.progressCircular.visibility = View.VISIBLE
        binding.noConnection.visibility = View.GONE
    }

    private fun failedConnect() {
        binding.progressCircular.visibility = View.GONE
        binding.noConnection.visibility = View.VISIBLE
        binding.blockingView.setBackgroundColor(getColor(R.color.white))
        Toast.makeText(this, "Failed Connect", Toast.LENGTH_SHORT).show()
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