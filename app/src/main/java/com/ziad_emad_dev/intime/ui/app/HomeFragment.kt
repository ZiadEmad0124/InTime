package com.ziad_emad_dev.intime.ui.app

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ziad_emad_dev.intime.R
import com.ziad_emad_dev.intime.databinding.FragmentHomeBinding
import com.ziad_emad_dev.intime.ui.recyclers_view.notification.NotificationActivity
import com.ziad_emad_dev.intime.ui.app.profile.ProfileActivity

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        myToolbar()

        return binding.root
    }

    private fun myToolbar() {
        binding.myToolbar.title.text = getString(R.string.time)

        binding.myToolbar.notification.setOnClickListener {
            val intent = Intent(requireContext(), NotificationActivity::class.java)
            startActivity(intent)
        }

        binding.myToolbar.menu.setOnClickListener {
            val intent = Intent(requireContext(), ProfileActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        _binding = null
    }

}