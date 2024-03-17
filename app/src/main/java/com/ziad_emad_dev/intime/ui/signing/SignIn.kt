package com.ziad_emad_dev.intime.ui.signing

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.ziad_emad_dev.intime.databinding.FragmentSignInBinding
import com.ziad_emad_dev.intime.ui.AsteriskPasswordTransformationMethod
import com.ziad_emad_dev.intime.ui.app.HomePage
import com.ziad_emad_dev.intime.ui.forgetPassword.ForgetPassword


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

        clickOnButton()

        binding.passwordEditText.transformationMethod = AsteriskPasswordTransformationMethod()
    }

    private fun clickOnButton() {

        binding.forgetPasswordButton.setOnClickListener {
            val intent = Intent(activity, ForgetPassword::class.java)
            startActivity(intent)
        }

        binding.signUpButton.setOnClickListener {
            findNavController().navigate(com.ziad_emad_dev.intime.R.id.action_signIn_to_signUp)
        }

        binding.logInButton.setOnClickListener {
            val intent = Intent(activity, HomePage::class.java)
            startActivity(intent)
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        _binding = null
    }
}