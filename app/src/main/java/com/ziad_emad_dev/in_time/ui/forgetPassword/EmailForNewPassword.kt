package com.ziad_emad_dev.in_time.ui.forgetPassword

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.ziad_emad_dev.in_time.R
import com.ziad_emad_dev.in_time.databinding.FragmentEmailForNewPasswordBinding
import com.ziad_emad_dev.in_time.network.auth.check_email.CheckEmailAccount

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

        focusOnEditTextLayout()
        clickOnSendOTPButton()
    }

    private fun focusOnEditTextLayout() {
        binding.email.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                binding.emailLayout.error = null
            } else {
                if (!emailEmptyError(binding.email.text.toString().trim())) {
                    emailValidationError(binding.email.text.toString().trim())
                }
            }
        }
    }

    private fun clickOnSendOTPButton() {
        binding.sendOTPButton.setOnClickListener {
            val email = binding.email.text.toString().trim()
            clearFocusEditTextLayout()
            if (!emailEmptyError(email)) {
                if (!emailValidationError(email)) {
                    checkEmail(email)
                }
            }
        }
    }

    private fun emailEmptyError(email: String): Boolean {
        if (email.isEmpty()) {
            binding.emailLayout.error = getString(R.string.empty_field)
        }
        return email.isEmpty()
    }

    private fun emailValidationError(email: String): Boolean {
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.emailLayout.error = getString(R.string.invalid_email)
        }
        return !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun emailNotRegistered() {
        binding.emailLayout.error = getString(R.string.email_not_registered)
    }

    private fun clearFocusEditTextLayout() {
        binding.email.clearFocus()
    }

    private fun checkEmail(email: String) {
        CheckEmailAccount().checkEmail(email, object : CheckEmailAccount.CheckEmailCallback {
            override fun onResult(message: String) {
                checkEmailAndNetwork(message)
            }
        })
    }

    private fun checkEmailAndNetwork(message: String) {
        when (message) {
            "true" -> {
                val action = EmailForNewPasswordDirections.actionEmailForNewPasswordToResetPassword(binding.email.text.toString().trim())
                findNavController().navigate(action)
            }

            "false" -> {
                emailNotRegistered()
            }

            "Failed Connect, Try Again" -> {
                Toast.makeText(requireContext(), "Failed Connect, Try Again", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        _binding = null
    }
}