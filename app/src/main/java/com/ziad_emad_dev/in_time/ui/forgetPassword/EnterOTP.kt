package com.ziad_emad_dev.in_time.ui.forgetPassword

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.ziad_emad_dev.in_time.R
import com.ziad_emad_dev.in_time.databinding.FragmentEnterOTPBinding

class EnterOTP : Fragment() {

    private var _binding: FragmentEnterOTPBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEnterOTPBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        clickOnButtons()
    }

    private fun clickOnButtons() {
        binding.nextButton.setOnClickListener {
            findNavController().navigate(R.id.action_enterOTP_to_resetNewPassword)
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        _binding = null
    }
}