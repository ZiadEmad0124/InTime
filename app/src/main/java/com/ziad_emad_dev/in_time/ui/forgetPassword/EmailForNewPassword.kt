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
import com.ziad_emad_dev.in_time.network.check_email.CheckEmailAccount

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
        clickOnButtons()
    }

    private fun focusOnEditTextLayout() {
        binding.email.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                binding.emailLayout.error = null
            }
        }
    }

    private fun clickOnButtons() {
        binding.sendOTPButton.setOnClickListener {
            focusOnEditTextLayout()
            if (!isEmailEmpty()){
                if (!emailValidationError()){
                    checkEmail()
                }
            }
        }
    }

    private fun isEmailEmpty(): Boolean {
        var isErrorFound = false
        if (binding.email.text?.trim().isNullOrEmpty()) {
            binding.emailLayout.error = getString(R.string.empty_field)
            isErrorFound = true
        }
        return isErrorFound
    }

    private fun emailValidationError(): Boolean {
        val email = binding.email.text.toString()
        var isEmailError = false
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.emailLayout.error = getString(R.string.invalid_email)
            isEmailError = true
        }
        return isEmailError
    }

    private fun emailNotRegistered() {
        binding.emailLayout.error = getString(R.string.email_not_found)
    }

    private fun checkEmail() {
        val email = binding.email.text?.trim().toString()
        CheckEmailAccount().checkEmail(email, object : CheckEmailAccount.CheckEmailCallback {
            override fun onResult(message: String) {
                checkEmailAndNetwork(message)
            }
        })
    }

    private fun checkEmailAndNetwork(message: String) {
        when (message) {
            "Failed Connect, Try Again" -> {
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
            }

            "true" -> {
                val action = EmailForNewPasswordDirections.actionEmailForNewPasswordToResetPassword(binding.email.text.toString())
                findNavController().navigate(action)
            }

            "false" -> {
                emailNotRegistered()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        _binding = null
    }
}