package com.ziad_emad_dev.in_time.ui.signing

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.ziad_emad_dev.in_time.R
import com.ziad_emad_dev.in_time.databinding.FragmentSignInBinding
import com.ziad_emad_dev.in_time.network.SessionManager
import com.ziad_emad_dev.in_time.network.sign_in.SignInUser
import com.ziad_emad_dev.in_time.ui.AsteriskPasswordTransformation
import com.ziad_emad_dev.in_time.ui.forgetPassword.ForgetPassword
import com.ziad_emad_dev.in_time.ui.home.HomePage

class SignIn : Fragment() {

    private var _binding: FragmentSignInBinding? = null
    private val binding get() = _binding!!

    private lateinit var sessionManager: SessionManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignInBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        signInWith()
        focusOnEditTextLayout()
        clickOnButtons()

        binding.password.transformationMethod = AsteriskPasswordTransformation()
    }

    private fun signInWith() {
        binding.signWithSocialMedia.signTextView.text = getString(R.string.signin_with)
    }

    private fun focusOnEditTextLayout() {
        binding.email.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                binding.emailLayout.error = null
            }
        }
        binding.password.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                binding.passwordLayout.error = null
            }
        }
    }

    private fun clickOnButtons() {
        binding.forgetPasswordButton.setOnClickListener {
            focusOnEditTextLayout()
            val intent = Intent(activity, ForgetPassword::class.java)
            startActivity(intent)
        }

        binding.signUpButton.setOnClickListener {
            focusOnEditTextLayout()
            findNavController().navigate(R.id.action_signIn_to_SignUp)
        }

        binding.logInButton.setOnClickListener {
            focusOnEditTextLayout()
            if (!areEmailOrPasswordEmpty()) {
                if (!(emailValidationError() || passwordValidationError())) {
                    isAccountRegistered(binding.email.text.toString(), binding.password.text.toString())
                }
            }
        }
    }

    private fun areEmailOrPasswordEmpty(): Boolean {
        var isErrorFound = false
        if (binding.email.text?.trim().isNullOrEmpty()) {
            binding.emailLayout.error = getString(R.string.empty_field)
            isErrorFound = true
        }
        if (binding.password.text?.trim().isNullOrEmpty()) {
            binding.passwordLayout.error = getString(R.string.empty_field)
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

    private fun passwordValidationError(): Boolean {
        val password = binding.password.text.toString()
        var isPasswordError = false
        if (password.length < 8) {
            binding.passwordLayout.error = getString(R.string.password_must_be_at_least_8_characters)
            isPasswordError = true
        } else if (!password.matches(Regex(".*[a-z].*"))) {
            binding.passwordLayout.error = getString(R.string.password_must_contain_at_least_one_lowercase_letter)
            isPasswordError = true
        } else if (!password.matches(Regex(".*[A-Z].*"))) {
            binding.passwordLayout.error = getString(R.string.password_must_contain_at_least_one_uppercase_letter)
            isPasswordError = true
        } else if (!password.matches(Regex(".*[0-9].*"))) {
            binding.passwordLayout.error = getString(R.string.password_must_contain_at_least_one_digit)
            isPasswordError = true
        } else if (!password.matches(Regex(".*[!@#\$%^&*].*"))) {
            binding.passwordLayout.error = getString(R.string.password_must_contain_at_least_one_special_character)
            isPasswordError = true
        } else {
            binding.passwordLayout.error = null
        }
        return isPasswordError
    }

    private fun emailNotRegistered() {
        binding.emailLayout.error = getString(R.string.email_not_found)
    }

    private fun passwordWrong() {
        binding.passwordLayout.error = getString(R.string.wrong_password)
    }

    private fun isAccountRegistered(email: String, password: String) {
        SignInUser().signIn(email, password, object : SignInUser.SignInCallback {
            override fun onResult(message: String) {
                checkAccountAndNetwork(message)
            }
        })
    }

    private fun checkAccountAndNetwork(message: String) {
        when (message) {
            "Failed Connect, Try Again" -> {
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
            }

            "user not registered" -> {
                emailNotRegistered()
            }

            "wrong password" -> {
                passwordWrong()
            }

            else -> {
                sessionManager = SessionManager(requireContext())
                sessionManager.saveAuthToken(message)
                val intent = Intent(activity, HomePage::class.java)
                startActivity(intent)
                requireActivity().finish()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        _binding = null
    }
}