package com.ziad_emad_dev.in_time.ui.profile

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.ziad_emad_dev.in_time.R
import com.ziad_emad_dev.in_time.databinding.FragmentProfileBinding
import com.ziad_emad_dev.in_time.ui.signing.SignPage
import com.ziad_emad_dev.in_time.viewmodels.ProfileViewModel

class Profile : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val viewModel by lazy {
        ProfileViewModel(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        myToolbar()

        fitchProfile()

        profileUsername()
        profileName()
        profileEmail()
        profilePhone()
        profilePassword()

        clickOnEditButton()

        signOut()
    }

    private fun myToolbar() {
        binding.myToolbar.title.text = getString(R.string.my_profile)
        binding.myToolbar.back.setOnClickListener {
            activity?.finish()
        }
    }

    private fun profileUsername() {
        viewModel.name.observe(viewLifecycleOwner) {
            binding.profileUsername.text = it
        }
    }

    private fun profileName() {
        binding.profileName.let { name ->
            name.icon.setImageResource(R.drawable.ic_name)
            name.title.text = getString(R.string.name)
            viewModel.name.observe(viewLifecycleOwner) {
                name.details.text = it
            }
        }
    }

    private fun profileEmail() {
        binding.profileEmail.let { email ->
            email.icon.setImageResource(R.drawable.ic_email)
            email.title.text = getString(R.string.email)
            viewModel.email.observe(viewLifecycleOwner) {
                email.details.text = it
            }
        }
    }

    private fun profilePhone() {
        binding.profilePhone.let { phone ->
            phone.icon.setImageResource(R.drawable.ic_phone)
            phone.title.text = getString(R.string.phone)
            viewModel.phone.observe(viewLifecycleOwner) {
                phone.details.text = it
            }
        }
    }

    private fun profilePassword() {
        binding.profilePassword.let { password ->
            password.icon.setImageResource(R.drawable.ic_password_lock)
            password.title.text = getString(R.string.password)
            password.details.text = getString(R.string.password_pattern)
        }
    }

    private fun clickOnEditButton() {
        binding.editButton.setOnClickListener {
            val action = ProfileDirections.actionProfileToEditProfile(
                binding.profileName.details.text.toString(),
                binding.profileEmail.details.text.toString(),
                binding.profilePhone.details.text.toString()
            )
            findNavController().navigate(action)
        }
    }

    private fun signOut() {
        binding.logOut.setOnClickListener {

            val alertDialog = LayoutInflater.from(requireContext()).inflate(R.layout.log_out_dialog, null)
            val alertBuilder = AlertDialog.Builder(requireContext()).setView(alertDialog)
            val alertInstance = alertBuilder.show()
            alertInstance.window?.setBackgroundDrawableResource(R.color.transparent)

            val logOutButton = alertDialog.findViewById<Button>(R.id.logOutButton)
            logOutButton.setOnClickListener {
                logOut()
            }

            val cancelButton = alertDialog.findViewById<Button>(R.id.cancelButton)
            cancelButton.setOnClickListener {
                alertInstance.dismiss()
            }
        }
    }

    private fun logOut() {
        binding.loadingIcon.visibility = View.VISIBLE
        val rotateAnimation = AnimationUtils.loadAnimation(requireContext(), R.anim.animation_rotate)
        binding.loadingIcon.startAnimation(rotateAnimation)

        viewModel.refreshToken()

        viewModel.refreshTokenMessage.observe(viewLifecycleOwner) { message ->
            if (message == "Refresh Succeed") {
                viewModel.signOut()
            } else {
                binding.loadingIcon.clearAnimation()
                binding.loadingIcon.visibility = View.INVISIBLE
                binding.page.visibility = View.INVISIBLE
                binding.loadingIcon.setImageResource(R.drawable.ic_no_connection)
                Toast.makeText(requireContext(), "Failed Connect", Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.signOutMessage.observe(viewLifecycleOwner) { message ->
            if (message == "true") {
                val intent = Intent(requireContext(), SignPage::class.java)
                startActivity(intent)
                activity?.finish()
            } else {
                binding.loadingIcon.clearAnimation()
                binding.loadingIcon.visibility = View.INVISIBLE
                binding.page.visibility = View.INVISIBLE
                binding.loadingIcon.setImageResource(R.drawable.ic_no_connection)
                Toast.makeText(requireContext(), "Failed Connect", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun fitchProfile() {
        val rotateAnimation = AnimationUtils.loadAnimation(requireContext(), R.anim.animation_rotate)
        binding.loadingIcon.startAnimation(rotateAnimation)

        viewModel.refreshToken()

        viewModel.refreshTokenMessage.observe(viewLifecycleOwner) { message ->
            if (message == "Refresh Succeed") {
                viewModel.fetchProfile()
            } else {
                binding.loadingIcon.clearAnimation()
                binding.loadingIcon.setImageResource(R.drawable.ic_no_connection)
                Toast.makeText(requireContext(), "Failed Connect", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.fetchProfileMessage.observe(viewLifecycleOwner) { message ->
            if (message == "true") {
                binding.loadingIcon.clearAnimation()
                binding.page.visibility = View.VISIBLE
                binding.loadingIcon.visibility = View.INVISIBLE
            } else {
                binding.loadingIcon.clearAnimation()
                binding.loadingIcon.setImageResource(R.drawable.ic_no_connection)
                Toast.makeText(requireContext(), "Failed Connect", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        _binding = null
    }
}