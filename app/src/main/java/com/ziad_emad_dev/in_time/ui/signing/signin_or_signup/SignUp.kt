package com.ziad_emad_dev.in_time.ui.signing.signin_or_signup

import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputLayout
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
        passwordToggle(binding.passwordLayout, binding.password)
        passwordToggle(binding.confirmPasswordLayout, binding.confirmPassword)
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
                    passwordValidationError(binding.passwordLayout, binding.password.text.toString().trim())
                }
            }
        }
        binding.confirmPassword.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                binding.confirmPasswordLayout.error = null
            } else {
                if (!confirmPasswordEmptyError(binding.confirmPassword.text.toString().trim())) {
                    passwordValidationError(binding.confirmPasswordLayout, binding.confirmPassword.text.toString().trim())
                    passwordsMatchError(binding.password.text.toString().trim(), binding.confirmPassword.text.toString().trim())
                }
            }
        }
    }

    private fun passwordToggle(passwordLayout: TextInputLayout, password: EditText) {
        passwordLayout.setEndIconOnClickListener {
            val selection = password.selectionEnd
            val hasPasswordTransformation = password.transformationMethod is PasswordTransformationMethod
            if (hasPasswordTransformation) {
                password.transformationMethod = null
            } else {
                password.transformationMethod = AsteriskPasswordTransformation()
            }
            password.setSelection(selection)
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
            val confirmPassword = binding.confirmPassword.text.toString().trim()
            clearFocusEditTextLayout()
            if (!(nameEmptyError(name) && emailEmptyError(email) && phoneEmptyError(phone) && passwordEmptyError(password))) {
                if (!(nameValidationError(name) ||
                            emailValidationError(email) ||
                            phoneValidationError(phone) ||
                            passwordValidationError(binding.passwordLayout, password) ||
                            passwordValidationError(binding.confirmPasswordLayout ,confirmPassword))) {
                    if (!passwordsMatchError(password, confirmPassword)) {
                        startLoading()
                        viewModel.signUp(name, email, phone, password)
                    }
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

    private fun confirmPasswordEmptyError(confirmPassword: String): Boolean {
        if (confirmPassword.isEmpty()) {
            binding.confirmPasswordLayout.error = getString(R.string.empty_field)
        }
        return confirmPassword.isEmpty()
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

    private fun passwordValidationError(passwordLayout: TextInputLayout, password: String): Boolean {
        if (password.length < 8) {
            passwordLayout.error = getString(R.string.password_must_be_at_least_8_characters)
            return true
        } else if (!password.matches(Regex(".*[a-z].*"))) {
            passwordLayout.error = getString(R.string.password_must_contain_at_least_one_lowercase_letter)
            return true
        } else if (!password.matches(Regex(".*[A-Z].*"))) {
            passwordLayout.error = getString(R.string.password_must_contain_at_least_one_uppercase_letter)
            return true
        } else if (!password.matches(Regex(".*[0-9].*"))) {
            passwordLayout.error = getString(R.string.password_must_contain_at_least_one_digit)
            return true
        } else if (!password.matches(Regex(".*[!@#\$_%^&*+(,)/\"\':?-].*"))) {
            passwordLayout.error = getString(R.string.password_must_contain_at_least_one_special_character)
            return true
        } else {
            return false
        }
    }

    private fun passwordsMatchError(password: String, confirmPassword: String): Boolean {
        if (password != confirmPassword) {
            binding.confirmPasswordLayout.error = getString(R.string.password_not_match)
        }
        return password != confirmPassword
    }

    private fun emailAlreadyExists() {
        binding.emailLayout.error = getString(R.string.this_email_already_exists)
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