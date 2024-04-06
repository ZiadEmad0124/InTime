package com.ziad_emad_dev.in_time.ui.home.projects.my_project

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayoutMediator
import com.ziad_emad_dev.in_time.R
import com.ziad_emad_dev.in_time.databinding.ActivityMyProjectBinding
import com.ziad_emad_dev.in_time.ui.MyViewPagerAdapter

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

        adapter.let {
            it.addFragment(ProjectAllTasks(), getString(R.string.all))
            it.addFragment(ProjectFinishedTasks(), getString(R.string.finished))
            it.addFragment(ProjectInProgressTasks(), getString(R.string.in_progress))
            it.addFragment(ProjectWaitToStartTasks(), getString(R.string.wait_to_start))
            it.notifyDataSetChanged()
        }

        binding.viewPager.adapter = adapter

        TabLayoutMediator(binding.tabsBar.root, binding.viewPager) { tab, position ->
            tab.text = adapter.getPageTitle(position)
            binding.viewPager.setCurrentItem(tab.position, true)
        }.attach()
    }
}