package com.ziad_emad_dev.in_time.ui.my_project

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayoutMediator
import com.ziad_emad_dev.in_time.R
import com.ziad_emad_dev.in_time.databinding.ActivityMyProjectBinding
import com.ziad_emad_dev.in_time.network.project.Project
import com.ziad_emad_dev.in_time.ui.MyViewPagerAdapter

@Suppress("DEPRECATION")
class MyProject : AppCompatActivity() {

    private lateinit var binding: ActivityMyProjectBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyProjectBinding.inflate(layoutInflater)
        setContentView(binding.root)

        myToolbar()

        myTabsBar()
    }

    private fun myToolbar() {
        binding.myToolbar.title.text = intent.getStringExtra("projectName")
        binding.myToolbar.back.setOnClickListener {
            finish()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun myTabsBar() {

        binding.viewPager.isSaveEnabled = false

        val adapter = MyViewPagerAdapter(supportFragmentManager, lifecycle)

        val admin = intent.getBooleanExtra("admin", false)
        val adminId = intent.getStringExtra("adminId")
        val projectId = intent.getStringExtra("projectId")
        val membersName = intent.getStringArrayListExtra("membersNames")!!
        val membersImage = intent.getStringArrayListExtra("membersAvatars")!!
        val membersIds = intent.getStringArrayListExtra("membersIds")!!

        val bundle = Bundle().apply {
            val project: Project = intent.getParcelableExtra("project")!!
            putString("projectId", projectId)
            putBoolean("admin", admin)
            putString("adminId", adminId)
            putParcelable("project", project)
            putStringArrayList("membersNames", membersName)
            putStringArrayList("membersAvatars", membersImage)
            putStringArrayList("membersIds", membersIds)
        }

        val projectAllTasksFragment = ProjectAllTasks().apply {
            arguments = bundle
        }

        val projectFinishedTasks = ProjectFinishedTasks().apply {
            arguments = bundle
        }

        val projectInProgressTasks = ProjectInProgressTasks().apply {
            arguments = bundle
        }

        val projectWaitToStartTasks = ProjectWaitToStartTasks().apply {
            arguments = bundle
        }

        adapter.let {
            it.addFragment(projectAllTasksFragment, getString(R.string.all))
            it.addFragment(projectFinishedTasks, getString(R.string.finished))
            it.addFragment(projectInProgressTasks, getString(R.string.in_progress))
            it.addFragment(projectWaitToStartTasks, getString(R.string.wait_to_start))
            it.notifyDataSetChanged()
        }

        binding.viewPager.adapter = adapter

        TabLayoutMediator(binding.tabsBar.root, binding.viewPager) { tab, position ->
            tab.text = adapter.getPageTitle(position)
            binding.viewPager.setCurrentItem(tab.position, true)
        }.attach()
    }
}