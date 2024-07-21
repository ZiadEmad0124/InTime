package com.ziad_emad_dev.in_time.ui.signing.forgetPassword

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.ziad_emad_dev.in_time.R
import com.ziad_emad_dev.in_time.databinding.FragmentEmailToResetPasswordBinding
import com.ziad_emad_dev.in_time.ui.signing.ValidationListener
import com.ziad_emad_dev.in_time.ui.signing.Validator
import com.ziad_emad_dev.in_time.viewmodels.AuthViewModel

class EmailToResetPassword : Fragment(), ValidationListener {

    private var _binding: FragmentEmailToResetPasswordBinding? = null
    private val binding get() = _binding!!

    private lateinit var validator: Validator

    private val viewModel by lazy {
        AuthViewModel(requireContext())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        validator = Validator(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEmailToResetPasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        validator.emailFocusChangeListener(binding.emailLayout)
        clickOnSendOTPButton()
    }

    private fun clickOnSendOTPButton() {
        binding.sendOTPButton.setOnClickListener {
            val email = binding.email.text.toString().trim()
            validator.clearFocusEditTextLayout()
            if (!validator.emailEmptyError(email)) {
                if (!validator.emailValidationError(email)) {
                    validator.startLoading()
                    viewModel.checkEmail(email)
                    waitingForResponse()
                }
            }
        }
    }

    private fun waitingForResponse() {
        viewModel.message.observe(viewLifecycleOwner) { message ->
            checkEmailAndNetwork(message)
        }
    }

    private fun checkEmailAndNetwork(message: String) {
        validator.stopLoading()
        when (message) {
            "true" -> {
                val action = EmailToResetPasswordDirections.actionEmailToResetPasswordToResetPassword(binding.email.text.toString().trim())
                findNavController().navigate(action)
            }

            "false" -> {
                binding.emailLayout.error = getString(R.string.email_not_registered)
            }

            else -> {
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onEmailEmptyError() {
        binding.emailLayout.error = getString(R.string.email_is_empty)
    }

    override fun onEmailValidationError() {
        binding.emailLayout.error = getString(R.string.email_is_invalid)
    }

    override fun onEmailFocusChange(hasFocus: Boolean, message: String?) {
        if (!hasFocus) {
            binding.emailLayout.error = message
        } else {
            binding.emailLayout.error = null
        }
    }

    override fun onClearFocusEditTextLayout() {
        binding.email.clearFocus()
    }

    override fun onStartLoading() {
        binding.blockingView.root.visibility = View.VISIBLE
        binding.sendOTPButton.text = null
        binding.progressCircular.visibility = View.VISIBLE
    }

    override fun onStopLoading() {
        binding.progressCircular.visibility = View.GONE
        binding.sendOTPButton.text = getString(R.string.send_otp)
        binding.blockingView.root.visibility = View.GONE
    }

    override fun onDestroy() {
        super.onDestroy()

        _binding = null
    }
}