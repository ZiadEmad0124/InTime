package com.ziad_emad_dev.in_time.ui.forgetPassword

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.ziad_emad_dev.in_time.R
import com.ziad_emad_dev.in_time.databinding.FragmentEmailForNewPasswordBinding

class EmailForNewPassword : Fragment() {

    private var _binding: FragmentEmailForNewPasswordBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEmailForNewPasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        clickOnButtons()
    }

    private fun clickOnButtons() {
        binding.sentOTPButton.setOnClickListener {
            findNavController().navigate(R.id.action_emailForNewPassword_to_enterOTP)
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        _binding = null
    }
}