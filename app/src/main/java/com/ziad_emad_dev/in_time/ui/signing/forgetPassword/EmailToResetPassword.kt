package com.ziad_emad_dev.in_time.ui.signing.forgetPassword

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.ziad_emad_dev.in_time.R
import com.ziad_emad_dev.in_time.databinding.FragmentEmailToResetPasswordBinding
import com.ziad_emad_dev.in_time.viewmodels.AuthViewModel

class EmailToResetPassword : Fragment() {

    private var _binding: FragmentEmailToResetPasswordBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AuthViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEmailToResetPasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        focusOnEditTextLayout()
        clickOnSendOTPButton()
        responseComing()
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
                    startLoading()
                    viewModel.checkEmail(email)
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

    private fun startLoading() {
        binding.blockingView.visibility = View.VISIBLE
        binding.sendOTPButton.setBackgroundResource(R.drawable.button_loading_background)
        binding.sendOTPButton.text = null
        binding.progressCircular.visibility = View.VISIBLE
    }

    private fun stopLoading() {
        binding.progressCircular.visibility = View.GONE
        binding.sendOTPButton.setBackgroundResource(R.drawable.button_background)
        binding.sendOTPButton.text = getString(R.string.send_otp)
        binding.blockingView.visibility = View.GONE
    }

    private fun clearFocusEditTextLayout() {
        binding.email.clearFocus()
    }

    private fun responseComing() {
        viewModel.message.observe(viewLifecycleOwner) { message ->
            checkEmailAndNetwork(message)
        }
    }

    private fun checkEmailAndNetwork(message: String) {
        stopLoading()
        when (message) {
            "true" -> {
                val action = EmailToResetPasswordDirections.actionEmailToResetPasswordToResetPassword(binding.email.text.toString().trim())
                findNavController().navigate(action)
            }

            "false" -> {
                emailNotRegistered()
            }

            "Failed Connect, Try Again" -> {
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        _binding = null
    }
}