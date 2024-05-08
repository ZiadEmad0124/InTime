package com.ziad_emad_dev.in_time.ui.signing.activation

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.ziad_emad_dev.in_time.R
import com.ziad_emad_dev.in_time.databinding.FragmentActivationAccountBinding
import com.ziad_emad_dev.in_time.viewmodels.AuthViewModel
import java.util.concurrent.TimeUnit

class ActivationAccount : Fragment() {

    private var _binding: FragmentActivationAccountBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AuthViewModel by viewModels()

    val millisInFuture = 5 * 60 * 1000 // 5 minutes in milliseconds
    val countDownInterval = 1000 // 1 second

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
        binding.otpView.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                binding.otpView.setLineColor(R.drawable.pin_line)
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
            val otpCode = binding.otpView.text.toString()
            val email = requireArguments().getString("email").toString()
            if (!otpCodeEmptyError(otpCode)) {
                startLoading()
                viewModel.activateAccount(otpCode, email)
            }
        }
    }

    private fun otpCodeEmptyError(otpCode: String): Boolean {
        if (otpCode.isEmpty() || otpCode.length < 4) {
            binding.otpView.setLineColor(Color.RED)
            Toast.makeText(requireContext(), "Please Enter The Code Contain 4 Digits", Toast.LENGTH_SHORT).show()
        }
        return otpCode.isEmpty() || otpCode.length < 4
    }

    private fun startLoading() {
        binding.blockingView.visibility = View.VISIBLE
        binding.loadingLayout.visibility = View.VISIBLE
        val rotateAnimation = AnimationUtils.loadAnimation(requireContext(), R.anim.animation_rotate)
        binding.loadingIcon.startAnimation(rotateAnimation)
    }

    private fun stopLoading() {
        binding.blockingView.visibility = View.INVISIBLE
        binding.loadingLayout.visibility = View.INVISIBLE
        binding.loadingIcon.clearAnimation()
    }

    private fun responseComing() {
        viewModel.message.observe(viewLifecycleOwner) {
            checkCodeAndNetwork(it)
        }
    }

    private fun checkCodeAndNetwork(message: String) {
        stopLoading()
        when (message) {
            "this account is now active" -> {
                binding.otpView.setLineColor(Color.GREEN)
                findNavController().navigate(R.id.action_activation_Account_to_signIn)
                Toast.makeText(requireContext(), "This account is active now, SignIn", Toast.LENGTH_SHORT).show()
            }

            "Failed Connect, Try Again" -> {
                binding.otpView.setLineColor(Color.RED)
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
            }

            else -> {
                binding.otpView.setLineColor(Color.RED)
                Toast.makeText(requireContext(), "Code is Wrong, Try Again", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun resendOTP() {
        binding.otpTimer.setOnClickListener {
            if (binding.otpTimer.text == getString(R.string.now)) {
                val email = requireArguments().getString("email").toString()
                val password = requireArguments().getString("password").toString()
                viewModel.resendActivationCode(email, password)
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