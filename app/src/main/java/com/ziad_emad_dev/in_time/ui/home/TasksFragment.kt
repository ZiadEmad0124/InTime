package com.ziad_emad_dev.in_time.ui.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.ziad_emad_dev.in_time.R
import com.ziad_emad_dev.in_time.databinding.FragmentTasksBinding
import com.ziad_emad_dev.in_time.ui.MyViewPagerAdapter
import com.ziad_emad_dev.in_time.ui.tasks.BacklogFragment
import com.ziad_emad_dev.in_time.ui.tasks.CompletedFragment
import com.ziad_emad_dev.in_time.ui.tasks.InProgressFragment

class TasksFragment : Fragment() {

    private var _binding: FragmentTasksBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTasksBinding.inflate(inflater, container, false)

        myTabsBar()

        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun myTabsBar() {

        binding.viewPager.isSaveEnabled = false

        val adapter = MyViewPagerAdapter(childFragmentManager, lifecycle)

        adapter.let {
            it.addFragment(InProgressFragment(), getString(R.string.in_progress))
            it.addFragment(CompletedFragment(), getString(R.string.completed))
            it.addFragment(BacklogFragment(), getString(R.string.backlog))
            it.notifyDataSetChanged()
        }

        binding.viewPager.adapter = adapter

        TabLayoutMediator(binding.tabsBar.root, binding.viewPager) { tab, position ->
            tab.text = adapter.getPageTitle(position)
            binding.viewPager.setCurrentItem(tab.position, true)
        }.attach()
    }

    override fun onDestroy() {
        super.onDestroy()

        _binding = null
    }
}