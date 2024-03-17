package com.ziad_emad_dev.intime.ui.app

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
import com.ziad_emad_dev.intime.R
import com.ziad_emad_dev.intime.databinding.ActivityHomePageBinding
import com.ziad_emad_dev.intime.ui.app.add.AddTaskActivity

class HomePage : AppCompatActivity() {

    private lateinit var binding: ActivityHomePageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomePageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        clickOnItemInBottomNavBar()

        customMiddleTabMenu()
    }

    @SuppressLint("RestrictedApi")
    private fun customMiddleTabMenu() {
        val bottomMenuView = binding.bottomNavigationView.getChildAt(0) as BottomNavigationMenuView
        val view = bottomMenuView.getChildAt(2)
        val itemView = view as BottomNavigationItemView

        val viewCustom = LayoutInflater.from(this).inflate(R.layout.layout_button_custom, bottomMenuView, false)
        itemView.addView(viewCustom)

        viewCustom.findViewById<ImageView>(R.id.add_button).setOnClickListener {
            showCreateBottomSheet()
        }
    }

    private fun clickOnItemInBottomNavBar() {

        binding.bottomNavigationView.setOnItemSelectedListener { item ->
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
                    showCreateBottomSheet()
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
    private fun showCreateBottomSheet() {
        val dialog = BottomSheetDialog(this)

        val view = layoutInflater.inflate(R.layout.layout_create_task_bottom_sheet, null)

        val createTaskButton = view.findViewById<View>(R.id.create_task_Button)
        createTaskButton.setOnClickListener {
            val intent = Intent(this, AddTaskActivity::class.java)
            startActivity(intent)
            dialog.dismiss()
        }
        val createProjectButton = view.findViewById<View>(R.id.create_project_Button)
        createProjectButton.setOnClickListener {

        }



        dialog.setContentView(view)
        dialog.show()
    }

}