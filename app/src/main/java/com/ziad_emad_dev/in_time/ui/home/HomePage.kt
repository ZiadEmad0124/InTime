package com.ziad_emad_dev.in_time.ui.home

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.ziad_emad_dev.in_time.R
import com.ziad_emad_dev.in_time.databinding.ActivityHomePageBinding
import com.ziad_emad_dev.in_time.ui.create.CreateTask

class HomePage : AppCompatActivity() {

    private lateinit var binding: ActivityHomePageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomePageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        customMiddleBottomNavBarIcon()

        clickOnItemInBottomNavBar()
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

        val createTaskButton = view.findViewById<View>(R.id.create_task_Button)
        createTaskButton.setOnClickListener {
            val intent = Intent(this, CreateTask::class.java)
            startActivity(intent)
            dialog.dismiss()
        }
        val createProjectButton = view.findViewById<View>(R.id.create_project_Button)
        createProjectButton.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }
}