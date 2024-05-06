package com.ziad_emad_dev.in_time.ui.signing.signin_or_signup

import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.ziad_emad_dev.in_time.R
import com.ziad_emad_dev.in_time.databinding.FragmentSignUpBinding
import com.ziad_emad_dev.in_time.ui.signing.AsteriskPasswordTransformation
import com.ziad_emad_dev.in_time.viewmodels.AuthViewModel

class SignUp : Fragment() {

    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AuthViewModel by viewModels()

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
        passwordToggle()
        clickOnButtons()
        responseComing()

        binding.password.transformationMethod = AsteriskPasswordTransformation()
    }

    private fun signUpWith() {
        binding.signWithSocialMedia.signTextView.text = getString(R.string.signup_with)
    }

    private fun focusOnEditTextLayout() {
        binding.name.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                binding.nameLayout.error = null
            } else {
                if (!nameEmptyError(binding.name.text.toString().trim())) {
                    nameValidationError(binding.name.text.toString().trim())
                }
            }
        }
        binding.email.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                binding.emailLayout.error = null
            } else {
                if (!emailEmptyError(binding.email.text.toString().trim())) {
                    emailValidationError(binding.email.text.toString().trim())
                }
            }
        }
        binding.phone.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                binding.phoneLayout.error = null
            } else {
                if (!phoneEmptyError(binding.phone.text.toString().trim())) {
                    phoneValidationError(binding.phone.text.toString().trim())
                }
            }
        }
        binding.password.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                binding.passwordLayout.error = null
            } else {
                if (!passwordEmptyError(binding.password.text.toString().trim())) {
                    passwordValidationError(binding.password.text.toString().trim())
                }
            }
        }
    }

    private fun passwordToggle() {
        binding.passwordLayout.setEndIconOnClickListener {
            val selection = binding.password.selectionEnd
            val hasPasswordTransformation = binding.password.transformationMethod is PasswordTransformationMethod
            if (hasPasswordTransformation) {
                binding.password.transformationMethod = null
            } else {
                binding.password.transformationMethod = AsteriskPasswordTransformation()
            }
            binding.password.setSelection(selection)
        }
    }

    private fun clickOnButtons() {
        binding.logInButton.setOnClickListener {
            findNavController().navigate(R.id.action_SignUp_to_signIn)
        }

        binding.signUpButton.setOnClickListener {
            val name = binding.name.text.toString().trim()
            val email = binding.email.text.toString().trim()
            val phone = binding.phone.text.toString().trim()
            val password = binding.password.text.toString().trim()
            clearFocusEditTextLayout()
            if (!(nameEmptyError(name) && emailEmptyError(email) && phoneEmptyError(phone) && passwordEmptyError(password))) {
                if (!(nameValidationError(name) || emailValidationError(email) || phoneValidationError(phone) || passwordValidationError(password))) {
                    startLoading()
                    viewModel.signUp(name, email, phone, password)
                }
            }
        }
    }

    private fun nameEmptyError(name: String): Boolean {
        if (name.isEmpty()) {
            binding.nameLayout.error = getString(R.string.empty_field)
        }
        return name.isEmpty()
    }

    private fun emailEmptyError(email: String): Boolean {
        if (email.isEmpty()) {
            binding.emailLayout.error = getString(R.string.empty_field)
        }
        return email.isEmpty()
    }

    private fun phoneEmptyError(phone: String): Boolean {
        if (phone.isEmpty()) {
            binding.phoneLayout.error = getString(R.string.empty_field)
        }
        return phone.isEmpty()
    }

    private fun passwordEmptyError(password: String): Boolean {
        if (password.isEmpty()) {
            binding.passwordLayout.error = getString(R.string.empty_field)
        }
        return password.isEmpty()
    }

    private fun nameValidationError(name: String): Boolean {
        if (name.length < 3) {
            binding.nameLayout.error = getString(R.string.name_error)
        }
        return name.length < 3
    }

    private fun emailValidationError(email: String): Boolean {
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.emailLayout.error = getString(R.string.invalid_email)
        }
        return !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun phoneValidationError(phone: String): Boolean {
        if (phone.length < 11) {
            binding.phoneLayout.error = getString(R.string.phone_error)
        }
        return phone.length < 11
    }

    private fun passwordValidationError(password: String): Boolean {
        if (password.length < 8) {
            binding.passwordLayout.error = getString(R.string.password_must_be_at_least_8_characters)
            return true
        } else if (!password.matches(Regex(".*[a-z].*"))) {
            binding.passwordLayout.error = getString(R.string.password_must_contain_at_least_one_lowercase_letter)
            return true
        } else if (!password.matches(Regex(".*[A-Z].*"))) {
            binding.passwordLayout.error = getString(R.string.password_must_contain_at_least_one_uppercase_letter)
            return true
        } else if (!password.matches(Regex(".*[0-9].*"))) {
            binding.passwordLayout.error = getString(R.string.password_must_contain_at_least_one_digit)
            return true
        } else if (!password.matches(Regex(".*[!@#\$_%^&*+(,)/\"\':?-].*"))) {
            binding.passwordLayout.error = getString(R.string.password_must_contain_at_least_one_special_character)
            return true
        } else {
            return false
        }
    }

    private fun emailAlreadyExists() {
        binding.emailLayout.error = getString(R.string.this_email_already_exists)
    }

    private fun startLoading() {
        binding.name.isEnabled = false
        binding.email.isEnabled = false
        binding.phone.isEnabled = false
        binding.password.isEnabled = false
        binding.logInButton.isEnabled = false
        binding.signUpButton.isEnabled = false
        binding.signUpButton.setBackgroundResource(R.drawable.button_loading_background)
        binding.signUpButton.setTextColor(resources.getColor(R.color.grey_5, null))
        binding.signUpButton.text = getString(R.string.loading)
    }

    private fun stopLoading() {
        binding.name.isEnabled = true
        binding.email.isEnabled = true
        binding.phone.isEnabled = true
        binding.password.isEnabled = true
        binding.logInButton.isEnabled = true
        binding.signUpButton.isEnabled = true
        binding.signUpButton.setBackgroundResource(R.drawable.button_background)
        binding.signUpButton.setTextColor(resources.getColor(R.color.white, null))
        binding.signUpButton.text = getString(R.string.sign_up)
    }

    private fun clearFocusEditTextLayout() {
        binding.name.clearFocus()
        binding.email.clearFocus()
        binding.phone.clearFocus()
        binding.password.clearFocus()
    }

    private fun responseComing() {
        viewModel.message.observe(viewLifecycleOwner) {
            checkAccountAndNetwork(it)
        }
    }

    private fun checkAccountAndNetwork(message: String) {
        stopLoading()
        when (message) {
            "this email already exists" -> {
                emailAlreadyExists()
            }

            "check your mail to activate your account" -> {
                val action = SignUpDirections.actionSignUpToActivationAccount(binding.email.text.toString().trim(), binding.password.text.toString().trim())
                findNavController().navigate(action)
            }

            else -> {
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        _binding = null
    }
}