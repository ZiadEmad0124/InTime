package com.ziad_emad_dev.in_time.ui.signing.signin_or_signup

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
import com.ziad_emad_dev.in_time.ui.home.HomePage
import com.ziad_emad_dev.in_time.ui.signing.AsteriskPasswordTransformation
import com.ziad_emad_dev.in_time.ui.signing.ValidationListener
import com.ziad_emad_dev.in_time.ui.signing.Validator
import com.ziad_emad_dev.in_time.viewmodels.AuthViewModel

class SignIn : Fragment(), ValidationListener {

    private var _binding: FragmentSignInBinding? = null
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
        _binding = FragmentSignInBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        validator.passwordToggle(binding.passwordLayout)

        validator.emailFocusChangeListener(binding.emailLayout)
        validator.passwordFocusChangeListener(binding.passwordLayout)

        clickOnButtons()

        binding.password.transformationMethod = AsteriskPasswordTransformation()
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
            validator.clearFocusEditTextLayout()
            if (!(validator.emailEmptyError(email) && validator.passwordEmptyError(password))) {
                if (!(validator.emailValidationError(email) || validator.passwordValidationError(password))) {
                    validator.startLoading()
                    viewModel.signIn(email, password)
                    waitingForResponse()
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
            "user not registered" -> {
                binding.emailLayout.error = getString(R.string.email_not_registered)
            }

            "wrong password" -> {
                binding.passwordLayout.error = getString(R.string.wrong_password)
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

    override fun onEmailEmptyError() {
        binding.emailLayout.error = getString(R.string.email_is_empty)
    }

    override fun onPasswordEmptyError() {
        binding.passwordLayout.error = getString(R.string.password_is_empty)
    }

    override fun onEmailValidationError() {
        binding.emailLayout.error = getString(R.string.email_is_invalid)
    }

    override fun onPasswordValidationError(passwordError: String) {
        binding.passwordLayout.error = passwordError
    }

    override fun onPasswordToggle(hasPasswordTransformation: Boolean) {
        if (hasPasswordTransformation) {
            binding.password.transformationMethod = null
        } else {
            binding.password.transformationMethod = AsteriskPasswordTransformation()
        }
    }

    override fun onEmailFocusChange(hasFocus: Boolean, message: String?) {
        if (!hasFocus) {
            binding.emailLayout.error = message
        } else {
            binding.emailLayout.error = null
        }
    }

    override fun onPasswordFocusChange(hasFocus: Boolean, message: String?) {
        if (!hasFocus) {
            binding.passwordLayout.error = message
        } else {
            binding.passwordLayout.error = null
        }
    }

    override fun onClearFocusEditTextLayout() {
        binding.email.clearFocus()
        binding.password.clearFocus()
    }

    override fun onStartLoading() {
        binding.blockingView.visibility = View.VISIBLE
        binding.logInButton.text = null
        binding.progressCircular.visibility = View.VISIBLE
    }

    override fun onStopLoading() {
        binding.progressCircular.visibility = View.GONE
        binding.logInButton.text = getString(R.string.login)
        binding.blockingView.visibility = View.GONE
    }

    override fun onDestroy() {
        super.onDestroy()

        _binding = null
    }
}