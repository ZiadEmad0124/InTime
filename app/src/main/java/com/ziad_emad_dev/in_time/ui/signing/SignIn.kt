package com.ziad_emad_dev.in_time.ui.signing

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.ziad_emad_dev.in_time.R
import com.ziad_emad_dev.in_time.databinding.FragmentSignInBinding
import com.ziad_emad_dev.in_time.ui.AsteriskPasswordTransformation
import com.ziad_emad_dev.in_time.ui.home.HomePage
import com.ziad_emad_dev.in_time.ui.forgetPassword.ForgetPassword


class SignIn : Fragment() {

    private var _binding: FragmentSignInBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignInBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        clickOnButtons()
        signInWith()

        binding.password.transformationMethod = AsteriskPasswordTransformation()
    }

    private fun clickOnButtons() {

        binding.forgetPasswordButton.setOnClickListener {
            val intent = Intent(activity, ForgetPassword::class.java)
            startActivity(intent)
        }

        binding.signUpButton.setOnClickListener {
            findNavController().navigate(R.id.action_signIn_to_SignUp)
        }

        binding.logInButton.setOnClickListener {
            val intent = Intent(activity, HomePage::class.java)
            startActivity(intent)
        }
    }

    private fun signInWith() {
        binding.signWithSocialMedia.signTextView.text = getString(R.string.signin_with)
    }

    override fun onDestroy() {
        super.onDestroy()

        _binding = null
    }
}