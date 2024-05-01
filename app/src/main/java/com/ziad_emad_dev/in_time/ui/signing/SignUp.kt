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
import com.ziad_emad_dev.in_time.databinding.FragmentSignUpBinding
import com.ziad_emad_dev.in_time.network.sign_up.SignUpUser
import com.ziad_emad_dev.in_time.ui.AsteriskPasswordTransformation
import com.ziad_emad_dev.in_time.ui.activate_account.ActivateAccount

class SignUp : Fragment() {

    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        signUpWith()
        focusOnEditTextLayout()
        clickOnButtons()

        binding.password.transformationMethod = AsteriskPasswordTransformation()
    }

    private fun signUpWith() {
        binding.signWithSocialMedia.signTextView.text = getString(R.string.signup_with)
    }

    private fun focusOnEditTextLayout() {
        binding.name.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                binding.nameLayout.error = null
            }
        }
        binding.email.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                binding.emailLayout.error = null
            }
        }
        binding.phone.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                binding.phoneLayout.error = null
            }
        }
        binding.password.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                binding.passwordLayout.error = null
            }
        }
    }

    private fun clickOnButtons() {
        binding.logInButton.setOnClickListener {
            focusOnEditTextLayout()
            findNavController().navigate(R.id.action_SignUp_to_signIn)
        }

        binding.signUpButton.setOnClickListener {
            focusOnEditTextLayout()
            if (!areFieldsEmpty()) {
                if (!(nameValidationError() || emailValidationError() || phoneValidationError() || passwordValidationError())) {
                    signAccount(binding.name.text.toString(), binding.email.text.toString(), binding.phone.text.toString(), binding.password.text.toString())
                }
            }
        }
    }

    private fun areFieldsEmpty(): Boolean {
        var isErrorFound = false
        if (binding.name.text?.trim().isNullOrEmpty()) {
            binding.nameLayout.error = getString(R.string.empty_field)
            isErrorFound = true
        }
        if (binding.email.text?.trim().isNullOrEmpty()) {
            binding.emailLayout.error = getString(R.string.empty_field)
            isErrorFound = true
        }
        if (binding.phone.text?.trim().isNullOrEmpty()) {
            binding.phoneLayout.error = getString(R.string.empty_field)
            isErrorFound = true
        }
        if (binding.password.text?.trim().isNullOrEmpty()) {
            binding.passwordLayout.error = getString(R.string.empty_field)
            isErrorFound = true
        }
        return isErrorFound
    }

    private fun nameValidationError(): Boolean {
        val name = binding.name.text.toString()
        var isNameError = false
        if (name.length < 3) {
            binding.nameLayout.error = getString(R.string.name_error)
            isNameError = true
        }
        return isNameError
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

    private fun phoneValidationError(): Boolean {
        val phone = binding.phone.text.toString()
        var isPhoneError = false
        if (phone.length < 11) {
            binding.phoneLayout.error = getString(R.string.phone_error)
            isPhoneError = true
        }
        return isPhoneError
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

    private fun emailAlreadyExists() {
        binding.emailLayout.error = getString(R.string.this_email_already_exists)
    }

    private fun signAccount(name: String, email: String, phone: String, password: String) {
        SignUpUser().signUp(name, email, phone, password, object : SignUpUser.SignUpCallback {
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

            "this email already exists" -> {
                emailAlreadyExists()
            }

            "check your mail to activate your account" -> {
                val intent = Intent(requireContext(), ActivateAccount::class.java)
                intent.putExtra("email", binding.email.text.toString())
                startActivity(intent)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        _binding = null
    }
}