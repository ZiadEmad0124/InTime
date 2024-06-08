package com.ziad_emad_dev.in_time.ui.profile

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.card.MaterialCardView
import com.ziad_emad_dev.in_time.R
import com.ziad_emad_dev.in_time.databinding.ActivityEditProfileBinding
import com.ziad_emad_dev.in_time.network.ProfileManager

@Suppress("DEPRECATION")
class EditProfile : AppCompatActivity() {

    private lateinit var binding: ActivityEditProfileBinding

    private lateinit var profileManager: ProfileManager

    private var profileImage: Bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        profileManager = ProfileManager(this)

        myToolbar()

        changeProfilePhoto()

        profileName()
        profileTitle()
        profileEmail()
        profilePhone()
        profileAbout()

        clickEditButton()
    }

    private fun myToolbar() {
        binding.myToolbar.title.text = getString(R.string.my_profile)
        binding.myToolbar.back.setOnClickListener {
            finish()
        }
    }

    private fun profileName() {
        binding.profileName.let { name ->
            name.icon.setImageResource(R.drawable.ic_name)
            name.title.text = getString(R.string.name)
            name.details.text = profileManager.getProfileName()
            binding.profileName.textInputEditText.setText(profileManager.getProfileName())
        }
    }

    private fun profileTitle() {
        binding.profileTitle.let { title ->
            title.icon.setImageResource(R.drawable.ic_title)
            title.title.text = getString(R.string.title)
            title.details.text = profileManager.getProfileTitle()
            binding.profileTitle.textInputEditText.setText(profileManager.getProfileTitle())
        }
    }

    private fun profileEmail() {
        binding.profileEmail.let { email ->
            email.icon.setImageResource(R.drawable.ic_email)
            email.title.text = getString(R.string.email)
            email.details.text = profileManager.getProfileEmail()
        }
    }

    private fun profilePhone() {
        binding.profilePhone.let { phone ->
            phone.icon.setImageResource(R.drawable.ic_phone)
            phone.title.text = getString(R.string.phone)
            phone.details.text = profileManager.getProfilePhone()
            phone.textInputEditText.inputType = phone.textInputEditText.inputType
            binding.profilePhone.textInputEditText.setText(profileManager.getProfilePhone())
        }
    }

    private fun profileAbout() {
        binding.profileAbout.let { about ->
            about.icon.setImageResource(R.drawable.ic_about)
            about.title.text = getString(R.string.about)
            about.details.text = profileManager.getProfileName()
            binding.profileAbout.textInputEditText.setText(profileManager.getProfileAbout())
        }
    }

    private fun clickEditButton() {
        binding.editButton.setOnClickListener {
            binding.profileName.details.visibility = View.GONE
            binding.profileTitle.details.visibility = View.GONE
            binding.profilePhone.details.visibility = View.GONE
            binding.profileAbout.details.visibility = View.GONE
            binding.profileName.textInputLayout.visibility = View.VISIBLE
            binding.profileTitle.textInputLayout.visibility = View.VISIBLE
            binding.profilePhone.textInputLayout.visibility = View.VISIBLE
            binding.profileAbout.textInputLayout.visibility = View.VISIBLE
            binding.editButton.text = getString(R.string.save)
            saveEditedProfile()
        }
    }

    private fun saveEditedProfile() {
        binding.editButton.setOnClickListener {
//            val name = binding.profileName.textInputEditText.text.toString()
//            val title = binding.profileTitle.textInputEditText.text.toString()
//            val phone = binding.profilePhone.textInputEditText.text.toString()
//            val about = binding.profileAbout.textInputEditText.text.toString()

        }
    }

    private fun changeProfilePhoto() {
        binding.uploadButton.setOnClickListener {
            selectImageFromCameraORGallery()
        }
    }

    private fun selectImageFromCameraORGallery() {
        val alertDialog = LayoutInflater.from(this).inflate(R.layout.profile_image_dialog, null)
        val alertBuilder = AlertDialog.Builder(this).setView(alertDialog)
        val alertInstance = alertBuilder.show()
        alertInstance.window?.setBackgroundDrawableResource(R.color.transparent)

        val takePhotoButton = alertDialog.findViewById<MaterialCardView>(R.id.takePhoto)
        takePhotoButton.setOnClickListener {
            takePhoto()
            alertInstance.dismiss()
        }
        val choosefromGalleryButton = alertDialog.findViewById<MaterialCardView>(R.id.chooseFromGallery)
        choosefromGalleryButton.setOnClickListener {
            pickImage()
            alertInstance.dismiss()
        }
    }

    private fun takePhoto() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            val takePicture = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(takePicture, 0)
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), 0)
            val takePicture = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(takePicture, 0)
        }
    }

    private fun pickImage() {
        val pickPhoto = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
        }
        startActivityForResult(pickPhoto, 1)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (requestCode == 0) {
                val takePicture = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(takePicture, 0)
            } else if (requestCode == 1) {
                val pickPhoto = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(pickPhoto, 1)
            }
        } else {
            Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    @Deprecated("This method has been deprecated in favor of using the Activity Result API\n" +
            "which brings increased type safety via an {@link ActivityResultContract} and the prebuilt\n" +
            "contracts for common intents available in\n" +
            "{@link androidx.activity.result.contract.ActivityResultContracts}, provides hooks for\n" +
            "testing, and allow receiving results in separate, testable classes independent from your\n" +
            "activity. Use\n" +
            "{@link #registerForActivityResult(ActivityResultContract, ActivityResultCallback)}\n" +
            "with the appropriate {@link ActivityResultContract} and handling the result in the\n" +
            "{@link ActivityResultCallback#onActivityResult(Object) callback}.")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 0) {
                val selectedImage = data?.extras?.get("data") as Bitmap
                profileImage = selectedImage
                binding.profileImage.setImageBitmap(selectedImage)
            } else if (requestCode == 1) {
                val selectedImage = data?.data
                profileImage = MediaStore.Images.Media.getBitmap(this.contentResolver, selectedImage)
                binding.profileImage.setImageURI(selectedImage)
            }
        }
    }
}