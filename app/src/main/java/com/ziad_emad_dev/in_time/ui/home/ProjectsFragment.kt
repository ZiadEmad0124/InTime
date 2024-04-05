package com.ziad_emad_dev.in_time.ui.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.ziad_emad_dev.in_time.R
import com.ziad_emad_dev.in_time.databinding.FragmentProjectsBinding
import com.ziad_emad_dev.in_time.ui.MyViewPagerAdapter
import com.ziad_emad_dev.in_time.ui.home.projects.AllProjectsFragment
import com.ziad_emad_dev.in_time.ui.home.projects.AsAdminFragment
import com.ziad_emad_dev.in_time.ui.home.projects.AsMemberFragment

class ProjectsFragment : Fragment() {

    private var _binding: FragmentProjectsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProjectsBinding.inflate(inflater, container, false)

        myToolbar()

        myTabsBar()

        return binding.root
    }

    private fun myToolbar() {
        binding.myToolbar.title.text = getString(R.string.projects)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun myTabsBar() {

        binding.viewPager.isSaveEnabled = false

        val adapter = MyViewPagerAdapter(childFragmentManager, lifecycle)

        adapter.let {
            it.addFragment(AllProjectsFragment(), getString(R.string.all_projects))
            it.addFragment(AsAdminFragment(), getString(R.string.as_admin))
            it.addFragment(AsMemberFragment(), getString(R.string.as_member))
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