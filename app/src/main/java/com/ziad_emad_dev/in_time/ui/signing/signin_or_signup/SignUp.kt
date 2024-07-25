package com.ziad_emad_dev.in_time.ui.signing.signin_or_signup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.ziad_emad_dev.in_time.R
import com.ziad_emad_dev.in_time.databinding.FragmentSignUpBinding
import com.ziad_emad_dev.in_time.ui.signing.AsteriskPasswordTransformation
import com.ziad_emad_dev.in_time.ui.signing.ValidationListener
import com.ziad_emad_dev.in_time.ui.signing.Validator
import com.ziad_emad_dev.in_time.viewmodels.AuthViewModel

class SignUp : Fragment(), ValidationListener {

    private var _binding: FragmentSignUpBinding? = null
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
        _binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        validator.passwordToggle(binding.passwordLayout)
        validator.confirmPasswordToggle(binding.confirmPasswordLayout)

        validator.nameFocusChangeListener(binding.nameLayout)
        validator.emailFocusChangeListener(binding.emailLayout)
        validator.phoneFocusChangeListener(binding.phoneLayout)
        validator.passwordFocusChangeListener(binding.passwordLayout)
        validator.confirmPasswordFocusChangeListener(binding.confirmPasswordLayout)

        clickOnButtons()

        binding.password.transformationMethod = AsteriskPasswordTransformation()
        binding.confirmPassword.transformationMethod = AsteriskPasswordTransformation()
    }

    private fun clickOnButtons() {
        binding.logInButton.setOnClickListener {
            findNavController().navigate(R.id.action_SignUp_to_signIn)
        }

        binding.signUpButton.setOnClickListener {
            val name = binding.name.text.toString().trim()
            val email = binding.email.text.toString().trim()
            val phone = binding.phone.text.toString().substring(2).trim()
            val password = binding.password.text.toString().trim()
            val confirmPassword = binding.confirmPassword.text.toString().trim()
            validator.clearFocusEditTextLayout()
            if (!(validator.nameEmptyError(name) &&
                        validator.emailEmptyError(email) &&
                        validator.phoneEmptyError(phone) &&
                        validator.passwordEmptyError(password) &&
                        validator.confirmPasswordEmptyError(confirmPassword)))
            {
                if (!(validator.nameValidationError(name) ||
                            validator.emailValidationError(email) ||
                            validator.phoneValidationError(phone) ||
                            validator.passwordValidationError(password) ||
                            validator.confirmPasswordValidationError(confirmPassword)))
                {
                    if (!validator.passwordsMatchError(password, confirmPassword)) {
                        validator.startLoading()
                        viewModel.signUp(name, email, phone, password)
                        waitingForResponse()
                    }
                }
            }
        }
    }

    private fun waitingForResponse() {
        viewModel.message.observe(viewLifecycleOwner) { message ->
            checkAccountAndNetwork(message)
        }
    }

    private fun checkAccountAndNetwork(message: String) {
        validator.stopLoading()
        when (message) {
            "this email already exists" -> {
                binding.emailLayout.error = getString(R.string.this_email_already_exists)
            }

            "check your mail to activate your account" -> {
                val email = binding.email.text.toString().trim()
                val password = binding.password.text.toString().trim()
                val action = SignUpDirections.actionSignUpToActivationAccount(email = email, password = password)
                findNavController().navigate(action)
            }

            else -> {
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onNameEmptyError() {
        binding.nameLayout.error = getString(R.string.name_is_empty)
    }

    override fun onEmailEmptyError() {
        binding.emailLayout.error = getString(R.string.email_is_empty)
    }

    override fun onPhoneEmptyError() {
        binding.phoneLayout.error = getString(R.string.phone_is_empty)
    }

    override fun onPasswordEmptyError() {
        binding.passwordLayout.error = getString(R.string.password_is_empty)
    }

    override fun onConfirmPasswordEmptyError() {
        binding.confirmPasswordLayout.error = getString(R.string.password_is_empty)
    }

    override fun onNameValidationError() {
        binding.nameLayout.error = getString(R.string.name_error)
    }

    override fun onEmailValidationError() {
        binding.emailLayout.error = getString(R.string.email_is_invalid)
    }

    override fun onPhoneValidationError() {
        binding.phoneLayout.error = getString(R.string.phone_error)
    }

    override fun onPasswordValidationError(passwordError: String) {
        binding.passwordLayout.error = passwordError
    }

    override fun onConfirmPasswordValidationError(passwordError: String) {
        binding.confirmPasswordLayout.error = passwordError
    }

    override fun onPasswordsMatchError() {
        binding.confirmPasswordLayout.error = getString(R.string.password_not_match)
    }

    override fun onPasswordToggle(hasPasswordTransformation: Boolean) {
        if (hasPasswordTransformation) {
            binding.password.transformationMethod = null
        } else {
            binding.password.transformationMethod = AsteriskPasswordTransformation()
        }
    }

    override fun onConfirmPasswordToggle(hasPasswordTransformation: Boolean) {
        if (hasPasswordTransformation) {
            binding.confirmPassword.transformationMethod = null
        } else {
            binding.confirmPassword.transformationMethod = AsteriskPasswordTransformation()
        }
    }

    override fun onNameFocusChange(hasFocus: Boolean, message: String?) {
        if (!hasFocus) {
            binding.nameLayout.error = message
        } else {
            binding.nameLayout.error = null
        }
    }

    override fun onEmailFocusChange(hasFocus: Boolean, message: String?) {
        if (!hasFocus) {
            binding.emailLayout.error = message
        } else {
            binding.emailLayout.error = null
        }
    }

    override fun onPhoneFocusChange(hasFocus: Boolean, message: String?) {
        if (!hasFocus) {
            binding.phoneLayout.error = message
        } else {
            binding.phoneLayout.error = null
        }
    }

    override fun onPasswordFocusChange(hasFocus: Boolean, message: String?) {
        if (!hasFocus) {
            binding.passwordLayout.error = message
        } else {
            binding.passwordLayout.error = null
        }
    }

    override fun onConfirmPasswordFocusChange(hasFocus: Boolean, message: String?) {
        if (!hasFocus) {
            binding.confirmPasswordLayout.error = message
        } else {
            binding.confirmPasswordLayout.error = null
        }
    }

    override fun onClearFocusEditTextLayout() {
        binding.name.clearFocus()
        binding.email.clearFocus()
        binding.phone.clearFocus()
        binding.password.clearFocus()
        binding.confirmPassword.clearFocus()
    }

    override fun onStartLoading() {
        binding.blockingView.root.visibility = View.VISIBLE
        binding.signUpButton.text = null
        binding.progressCircular.visibility = View.VISIBLE
    }

    override fun onStopLoading() {
        binding.progressCircular.visibility = View.GONE
        binding.signUpButton.text = getString(R.string.sign_up)
        binding.blockingView.root.visibility = View.GONE
    }

    override fun onDestroy() {
        super.onDestroy()

        _binding = null
    }
}