package com.ziad_emad_dev.in_time.ui.signing.signin_or_signup

import android.content.Intent
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.ziad_emad_dev.in_time.R
import com.ziad_emad_dev.in_time.databinding.FragmentSignInBinding
import com.ziad_emad_dev.in_time.ui.home.HomePage
import com.ziad_emad_dev.in_time.ui.signing.AsteriskPasswordTransformation
import com.ziad_emad_dev.in_time.viewmodels.AuthViewModel

class SignIn : Fragment() {

    private var _binding: FragmentSignInBinding? = null
    private val binding get() = _binding!!

    private val viewModel by lazy {
        AuthViewModel(requireContext())
    }

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
        passwordToggle()
        clickOnButtons()
        responseComing()

        binding.password.transformationMethod = AsteriskPasswordTransformation()
    }

    private fun signInWith() {
        binding.signWithSocialMedia.signTextView.text = getString(R.string.signin_with)
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
        binding.forgetPasswordButton.setOnClickListener {
            findNavController().navigate(R.id.action_signIn_to_emailToResetPassword)
        }

        binding.signUpButton.setOnClickListener {
            findNavController().navigate(R.id.action_signIn_to_SignUp)
        }

        binding.logInButton.setOnClickListener {
            val email = binding.email.text.toString().trim()
            val password = binding.password.text.toString().trim()
            clearFocusEditTextLayout()
            if (!(emailEmptyError(email) && passwordEmptyError(password))) {
                if (!(emailValidationError(email) || passwordValidationError(password))) {
                    startLoading()
                    viewModel.signIn(email, password)
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

    private fun passwordEmptyError(password: String): Boolean {
        if (password.isEmpty()) {
            binding.passwordLayout.error = getString(R.string.empty_field)
        }
        return password.isEmpty()
    }

    private fun emailValidationError(email: String): Boolean {
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.emailLayout.error = getString(R.string.invalid_email)
        }
        return !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
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

    private fun emailNotRegistered() {
        binding.emailLayout.error = getString(R.string.email_not_registered)
    }

    private fun passwordWrong() {
        binding.passwordLayout.error = getString(R.string.wrong_password)
    }

    private fun startLoading() {
        binding.blockingView.visibility = View.VISIBLE
        binding.logInButton.setBackgroundResource(R.drawable.button_loading)
        binding.logInButton.text = null
        binding.progressCircular.visibility = View.VISIBLE
    }

    private fun stopLoading() {
        binding.progressCircular.visibility = View.GONE
        binding.logInButton.setBackgroundResource(R.drawable.button_background)
        binding.logInButton.text = getString(R.string.login)
        binding.blockingView.visibility = View.GONE
    }

    private fun clearFocusEditTextLayout() {
        binding.email.clearFocus()
        binding.password.clearFocus()
    }

    private fun responseComing() {
        viewModel.message.observe(viewLifecycleOwner) { message ->
            checkAccountAndNetwork(message)
        }
    }

    private fun checkAccountAndNetwork(message: String) {
        stopLoading()
        when (message) {
            "user not registered" -> {
                emailNotRegistered()
            }

            "wrong password" -> {
                passwordWrong()
            }

            "you have to activate your account first" -> {
                val email = binding.email.text.toString().trim()
                val action = SignInDirections.actionSignInToActivationAccount(email = email)
                viewModel.resendActivationCode(email)
                findNavController().navigate(action)
            }

            "true" -> {
                val intent = Intent(activity, HomePage::class.java)
                startActivity(intent)
                requireActivity().finish()
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