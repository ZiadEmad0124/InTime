package com.ziad_emad_dev.intime.ui.signing

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ziad_emad_dev.intime.databinding.FragmentContinueSignUpBinding
import com.ziad_emad_dev.intime.ui.AsteriskPasswordTransformationMethod
import com.ziad_emad_dev.intime.ui.app.HomePage

class ContinueSignUp : Fragment() {

    private var _binding: FragmentContinueSignUpBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentContinueSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        clickOnButton()

        binding.passwordEditText.transformationMethod = AsteriskPasswordTransformationMethod()
        binding.confirmPasswordEditText.transformationMethod = AsteriskPasswordTransformationMethod()
    }

    private fun clickOnButton() {
        binding.signUpButton.setOnClickListener {
            val intent = Intent(activity, HomePage::class.java)
            startActivity(intent)
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        _binding = null
    }
}