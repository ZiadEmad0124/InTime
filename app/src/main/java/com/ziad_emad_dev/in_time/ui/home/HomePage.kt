package com.ziad_emad_dev.in_time.ui.home

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.ziad_emad_dev.in_time.R
import com.ziad_emad_dev.in_time.databinding.ActivityHomePageBinding
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

    private val viewModel by lazy {
        HomeViewModel(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomePageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        homeToolbar()

        customMiddleBottomNavBarIcon()
        clickOnItemInBottomNavBar()

        goToProfile()
        goToSettings()
        goToChangePassword()
        joinProject()
        signOut()

        fetchProfile()
        setupSwipeRefreshLayout()
    }

    private fun homeToolbar() {
        binding.homeToolbar.profile.setOnClickListener {
            binding.drawerLayout.openDrawer(GravityCompat.START)
        }

        binding.homeToolbar.notification.setOnClickListener {
            val intent = Intent(this, NotificationActivity::class.java)
            startActivity(intent)
        }
    }

    @SuppressLint("RestrictedApi")
    private fun customMiddleBottomNavBarIcon() {
        val bottomNavBarMenu = binding.bottomNavBar.getChildAt(0) as BottomNavigationMenuView
        val middleIcon = bottomNavBarMenu.getChildAt(2)
        val iconView = middleIcon as BottomNavigationItemView

        val myIcon =
            LayoutInflater.from(this).inflate(R.layout.layout_middle_icon, bottomNavBarMenu, false)
        iconView.addView(myIcon)

        myIcon.findViewById<ImageView>(R.id.addButton).setOnClickListener {
            showBottomSheet()
        }
    }

    private fun clickOnItemInBottomNavBar() {
        binding.bottomNavBar.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.homeFragment -> {
                    binding.homeToolbar.title.text = getString(R.string.home)
                    runSelectedFragment(HomeFragment())
                    true
                }

                R.id.tasksFragment -> {
                    binding.homeToolbar.title.text = getString(R.string.tasks)
                    runSelectedFragment(TasksFragment())
                    true
                }

                R.id.addFragment -> {
                    showBottomSheet()
                    true
                }

                R.id.projectsFragment -> {
                    binding.homeToolbar.title.text = getString(R.string.projects)
                    runSelectedFragment(ProjectsFragment())
                    true
                }

                R.id.calenderFragment -> {
                    binding.homeToolbar.title.text = getString(R.string.calender)
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

    //
    @SuppressLint("InflateParams")
    private fun joinProject() {
        binding.navigationView.joinProject.setOnClickListener {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
            val dialog = BottomSheetDialog(this)
            val view = layoutInflater.inflate(R.layout.layout_join_project, null)
            dialog.setContentView(view)

            val linkLayout = view.findViewById<TextInputLayout>(R.id.linkLayout)
            val link = view.findViewById<TextInputEditText>(R.id.link)

            link.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    linkLayout.error = null
                }
            }

            val joinProjectButton = view.findViewById<View>(R.id.joinProjectButton)
            joinProjectButton.setOnClickListener {
                link.clearFocus()
                if (link.text.toString().trim().isEmpty()) {
                    linkLayout.error = getString(R.string.empty_field)
                } else {
                    val fullLink = link.text.toString().trim()
                    val projectId = fullLink.substring(66, 90)
                    val otp = fullLink.substring(91, 95)
                    Log.d("link", "projectId: $projectId, otp: $otp")
                    viewModel.joinProject(projectId, otp)
                    viewModel.joinProjectMessage.observe(this) { message ->
                        if (message == "true") {
                            dialog.dismiss()
                            Toast.makeText(this, "Join Project Success", Toast.LENGTH_SHORT).show()
                            dialog.dismiss()
                        } else {
                            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
            dialog.show()
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
        viewModel.signOut()
        viewModel.signOutMessage.observe(this) {
            val intent = Intent(this, SignPage::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }
    }

    private fun fetchProfile() {
        startLoading()
        viewModel.refreshToken()
        viewModel.refreshTokenMessage.observe(this) { message ->
            when (message) {
                "true" -> {
                    viewModel.fetchProfile()
                }
                "session expired" -> {
                    Toast.makeText(this, "Session Expired", Toast.LENGTH_SHORT).show()
                    logOut()
                }
                else -> {
                    errorInConnectingOrFetching(message)
                }
            }
        }

        viewModel.fetchProfileMessage.observe(this) { message ->
            if (message == "true") {
                stopLoading()
                fetchProfileNameAndAvatar()
                binding.homeToolbar.title.text = getString(R.string.home)
                runSelectedFragment(HomeFragment())
            } else {
                errorInConnectingOrFetching(message)
            }
        }
    }

    private fun fetchProfileNameAndAvatar() {
        viewModel.profileAvatar.observe(this) { avatar ->
            Glide.with(this)
                .load(avatar)
                .placeholder(R.drawable.ic_profile_default)
                .error(R.drawable.ic_profile_default)
                .into(binding.homeToolbar.profile)

            Glide.with(this)
                .load(avatar)
                .placeholder(R.drawable.ic_profile_default)
                .error(R.drawable.ic_profile_default)
                .into(binding.navigationView.profileImage)
        }

        viewModel.profileName.observe(this) { name ->
            binding.navigationView.profileName.text = name
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

    @Deprecated("This function is Deprecated")
    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
}