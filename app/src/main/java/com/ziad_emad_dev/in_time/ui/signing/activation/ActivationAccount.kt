package com.ziad_emad_dev.in_time.ui.signing.activation

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.ziad_emad_dev.in_time.R
import com.ziad_emad_dev.in_time.databinding.FragmentActivationAccountBinding
import com.ziad_emad_dev.in_time.viewmodels.AuthViewModel
import java.util.concurrent.TimeUnit

class ActivationAccount : Fragment() {

    private var _binding: FragmentActivationAccountBinding? = null
    private val binding get() = _binding!!

    private val viewModel by lazy {
        AuthViewModel(requireContext())
    }

    val millisInFuture = 5 * 60 * 1000 // 5 minutes in milliseconds
    val countDownInterval = 1000 // 1 second in milliseconds

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentActivationAccountBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        focusOnEditTextLayout()
        countDownTimer.start()
        clickOnNextButton()
        resendOTP()
        responseComing()
    }

    private fun focusOnEditTextLayout() {
        binding.otpCode.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {

                binding.otpCode.setLineColor(R.drawable.pin_line)
            }
        }
    }

    private val countDownTimer = object : CountDownTimer(millisInFuture.toLong(), countDownInterval.toLong()) {
            @SuppressLint("DefaultLocale")
            override fun onTick(millisUntilFinished: Long) {
                val minutes = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)
                val seconds = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(minutes)
                binding.otpTimer.text = String.format("in %02d:%02d", minutes, seconds)
            }

            override fun onFinish() {
                binding.otpTimer.text = getString(R.string.now)
            }
        }

    private fun clickOnNextButton() {
        binding.nextButton.setOnClickListener {
            val otpCode = binding.otpCode.text.toString()
            val email = requireArguments().getString("email").toString()
            if (!otpCodeEmptyError(otpCode)) {
                startLoading()
                viewModel.activateAccount(otpCode, email)
            }
        }
    }

    private fun otpCodeEmptyError(otpCode: String): Boolean {
        if (otpCode.isEmpty() || otpCode.length < 4) {

            binding.otpCode.setLineColor(Color.RED)
            Toast.makeText(requireContext(), "Please Enter The Code Contain 4 Digits", Toast.LENGTH_SHORT).show()
        }
        return otpCode.isEmpty() || otpCode.length < 4
    }

    private fun startLoading() {
        binding.blockingView.visibility = View.VISIBLE

        binding.otpCode.setLineColor(R.drawable.pin_line)

        binding.nextButton.setBackgroundResource(R.drawable.button_loading)
        binding.nextButton.text = null
        binding.progressCircular.visibility = View.VISIBLE
    }

    private fun stopLoading() {
        binding.progressCircular.visibility = View.GONE
        binding.nextButton.setBackgroundResource(R.drawable.button_background)
        binding.nextButton.text = getString(R.string.next)
        binding.blockingView.visibility = View.GONE
    }

    private fun responseComing() {
        viewModel.message.observe(viewLifecycleOwner) { message ->
            checkCodeAndNetwork(message)
        }
    }

    private fun checkCodeAndNetwork(message: String) {
        stopLoading()
        when (message) {
            "true" -> {
                binding.otpCode.setLineColor(Color.GREEN)
                Toast.makeText(requireContext(), "This account is active now, SignIn", Toast.LENGTH_LONG).show()
                findNavController().navigate(R.id.action_activation_Account_to_signIn)
            }

            "Invalid OTP" -> {

                binding.otpCode.setLineColor(Color.RED)
                Toast.makeText(requireContext(), "Code is wrong, Try again", Toast.LENGTH_SHORT).show()
            }

            "Failed Connect, Try Again" -> {
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
            }

            else -> {

                binding.otpCode.setLineColor(Color.RED)
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun resendOTP() {
        binding.otpTimer.setOnClickListener {
            if (binding.otpTimer.text == getString(R.string.now)) {
                val email = requireArguments().getString("email").toString()
                viewModel.resendActivationCode(email)
                Toast.makeText(requireContext(), "Resend OTP, Check Your Email", Toast.LENGTH_SHORT).show()
                countDownTimer.start()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        _binding = null
    }
}