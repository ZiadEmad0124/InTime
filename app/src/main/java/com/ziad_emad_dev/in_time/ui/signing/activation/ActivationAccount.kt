package com.ziad_emad_dev.in_time.ui.signing.activation

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.ziad_emad_dev.in_time.R
import com.ziad_emad_dev.in_time.databinding.FragmentActivationAccountBinding
import com.ziad_emad_dev.in_time.ui.home.HomePage
import com.ziad_emad_dev.in_time.ui.signing.ValidationListener
import com.ziad_emad_dev.in_time.ui.signing.Validator
import com.ziad_emad_dev.in_time.viewmodels.AuthViewModel

class ActivationAccount : Fragment(), ValidationListener {

    private var _binding: FragmentActivationAccountBinding? = null
    private val binding get() = _binding!!

    private val viewModel by lazy {
        AuthViewModel(requireContext())
    }

    private lateinit var validator: Validator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        validator = Validator(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentActivationAccountBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        validator.startCountDownTimer()
        resendOTP()
        clickOnNextButton()
    }

    private fun clickOnNextButton() {
        binding.nextButton.setOnClickListener {
            val otpCode = binding.otpCode.text.toString()
            val email = requireArguments().getString("email").toString()
            validator.clearFocusEditTextLayout()
            if (!validator.otpCodeEmptyError(otpCode)) {
                validator.startLoading()
                viewModel.activateAccount(otpCode, email)
                waitingForResponse()
            }
        }
    }

    private fun waitingForResponse() {
        viewModel.message.observe(viewLifecycleOwner) { message ->
            checkCodeAndNetwork(message)
        }
    }

    private fun checkCodeAndNetwork(message: String) {
        when (message) {
            "true" -> {
                binding.otpCode.setLineColor(Color.GREEN)
                Toast.makeText(requireContext(), "This account is active now", Toast.LENGTH_LONG).show()
                validator.stopLoading()

                val email = requireArguments().getString("email").toString()
                val password = requireArguments().getString("password").toString()
                viewModel.signIn(email, password)
                viewModel.signInMessage.observe(viewLifecycleOwner) { signInMessage ->
                    if (signInMessage == "true") {
                        val intent = Intent(requireContext(), HomePage::class.java)
                        startActivity(intent)
                        requireActivity().finish()
                    }
                }
            }

            "Invalid OTP" -> {
                binding.otpCode.setLineColor(Color.RED)
                Toast.makeText(requireContext(), "Code is wrong, Try again", Toast.LENGTH_SHORT).show()
                validator.stopLoading()
            }

            else -> {
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                validator.stopLoading()
            }
        }
    }

    override fun onOTPTimer(timer: String) {
        _binding?.otpTimer?.text = timer
    }

    private fun resendOTP() {
        binding.otpTimer.setOnClickListener {
            if (binding.otpTimer.text == "Now") {
                validator.resetOTPTimer()
            }
        }
    }

    override fun onResetOTPTimer() {
        val email = requireArguments().getString("email").toString()
        viewModel.resendActivationCode(email)
        viewModel.resendActivationCodeMessage.observe(viewLifecycleOwner) { resendActivationCodeMessage ->
            Toast.makeText(requireContext(), resendActivationCodeMessage, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onOTPCodeEmptyError() {
        binding.otpCode.setLineColor(Color.RED)
        Toast.makeText(requireContext(), "Please Enter The Code Contain 4 Digits", Toast.LENGTH_SHORT).show()
    }

    override fun onClearFocusEditTextLayout() {
        binding.otpCode.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                binding.otpCode.setLineColor(R.drawable.pin_line)
            }
        }
    }

    override fun onStartLoading() {
        binding.blockingView.root.visibility = View.VISIBLE
        binding.nextButton.text = null
        binding.progressCircular.visibility = View.VISIBLE
    }

    override fun onStopLoading() {
        binding.progressCircular.visibility = View.GONE
        binding.nextButton.text = getString(R.string.next)
        binding.blockingView.root.visibility = View.GONE
    }

    override fun onDestroy() {
        super.onDestroy()

        _binding = null
    }
}