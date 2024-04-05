package com.ziad_emad_dev.in_time.ui.signing

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.ziad_emad_dev.in_time.R
import com.ziad_emad_dev.in_time.databinding.FragmentSignUpBinding
import com.ziad_emad_dev.in_time.ui.AsteriskPasswordTransformation
import com.ziad_emad_dev.in_time.ui.home.HomePage

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

        clickOnButtons()
        signUpWith()

        binding.password.transformationMethod = AsteriskPasswordTransformation()
        binding.confirmPassword.transformationMethod = AsteriskPasswordTransformation()
    }

    private fun clickOnButtons() {
        binding.signUpButton.setOnClickListener {
            val intent = Intent(activity, HomePage::class.java)
            startActivity(intent)
        }

        binding.logInButton.setOnClickListener {
            findNavController().navigate(R.id.action_SignUp_to_signIn)
        }
    }

    private fun signUpWith() {
        binding.signWithSocialMedia.signTextView.text = getString(R.string.signup_with)
    }

    override fun onDestroy() {
        super.onDestroy()

        _binding = null
    }
}