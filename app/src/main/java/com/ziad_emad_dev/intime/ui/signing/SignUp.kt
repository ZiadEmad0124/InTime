package com.ziad_emad_dev.intime.ui.signing

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.ziad_emad_dev.intime.R
import com.ziad_emad_dev.intime.databinding.FragmentSignUpBinding

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

        clickOnButton()
    }

    private fun clickOnButton() {

        binding.logInButton.setOnClickListener {
            findNavController().navigate(R.id.action_signUp_to_signIn)
        }

        binding.continueButton.setOnClickListener {
            findNavController().navigate(R.id.action_signUp_to_continueSignUp)
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        _binding = null
    }
}