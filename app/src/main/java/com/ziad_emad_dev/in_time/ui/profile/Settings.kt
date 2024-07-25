package com.ziad_emad_dev.in_time.ui.profile

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.google.android.material.card.MaterialCardView
import com.google.android.material.snackbar.Snackbar
import com.ziad_emad_dev.in_time.R
import com.ziad_emad_dev.in_time.databinding.ActivitySettingsBinding
import com.ziad_emad_dev.in_time.viewmodels.ProfileViewModel
import java.io.File
import java.io.IOException

@Suppress("DEPRECATION")
class Settings : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding

    //
    private var profileImage: Bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)
    private var profileImageFile: File? = null
    private var isProfileImageRemoved = false

    private val viewModel by lazy {
        ProfileViewModel(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        appToolbar()

        fetchProfile()

        removeProfilePhotoButton()
        uploadProfilePhotoButton()

        focusOnEditTextLayout()

        clickEditButton()
    }

    private fun appToolbar() {
        binding.appToolbar.title.text = getString(R.string.settings)
        binding.appToolbar.back.setOnClickListener {
            finish()
        }
    }

    private fun fetchProfile() {
        profileImage()
        profileName()
        profileTitle()
        profileEmail()
        profilePhone()
        profileAbout()
    }

    private fun profileImage() {
        viewModel.profile.observe(this) { profile ->
            Glide.with(this)
                .load(profile.avatar)
                .placeholder(R.drawable.ic_profile_default)
                .error(R.drawable.ic_profile_default)
                .into(binding.profileImage)
        }
    }

    private fun profileName() {
        binding.profileName.let { name ->
            name.icon.setImageResource(R.drawable.ic_name)
            name.title.text = getString(R.string.name)
            viewModel.profile.observe(this) { profile ->
                name.details.text = profile.name
                binding.profileName.textInputEditText.setText(profile.name)
            }
        }
    }

    private fun profileTitle() {
        binding.profileTitle.let { title ->
            title.icon.setImageResource(R.drawable.ic_title)
            title.title.text = getString(R.string.title)
            viewModel.profile.observe(this) { profile ->
                title.details.text = profile.title
                binding.profileTitle.textInputEditText.setText(profile.title)
            }
        }
    }

    private fun profileEmail() {
        binding.profileEmail.let { email ->
            email.icon.setImageResource(R.drawable.ic_email)
            email.title.text = getString(R.string.email)
            viewModel.profile.observe(this) { profile ->
                email.details.text = profile.email
            }
        }
    }

    private fun profilePhone() {
        binding.profilePhone.let { phone ->
            phone.icon.setImageResource(R.drawable.ic_phone)
            phone.title.text = getString(R.string.phone)
            viewModel.profile.observe(this) { profile ->
                phone.details.text = profile.phone
                binding.profilePhone.textInputEditText.inputType = InputType.TYPE_CLASS_PHONE
            }
        }
    }

    private fun profileAbout() {
        binding.profileAbout.let { about ->
            about.icon.setImageResource(R.drawable.ic_about)
            about.title.text = getString(R.string.about)
            viewModel.profile.observe(this) { profile ->
                about.details.text = profile.about
                binding.profileAbout.textInputEditText.maxLines = 5
                binding.profileAbout.textInputEditText.setText(profile.about)
            }
        }
    }

    private fun clickEditButton() {
        binding.editButton.setOnClickListener {
            if (binding.editButton.text == getString(R.string.edit)) {
                startEditing()
                binding.editButton.text = getString(R.string.save)
            } else {
                editProfile()
            }
        }
    }

    private fun startEditing() {
        binding.profileName.details.visibility = View.GONE
        binding.profileTitle.details.visibility = View.GONE
        binding.profilePhone.details.visibility = View.GONE
        binding.profileAbout.details.visibility = View.GONE

        binding.imageButtonsContainer.visibility = View.VISIBLE

        binding.profileName.textInputLayout.visibility = View.VISIBLE
        binding.profileTitle.textInputLayout.visibility = View.VISIBLE
        binding.profilePhone.textInputLayout.visibility = View.VISIBLE
        binding.profileAbout.textInputLayout.visibility = View.VISIBLE
    }

    private fun endEditing() {
        binding.profileName.details.visibility = View.VISIBLE
        binding.profileTitle.details.visibility = View.VISIBLE
        binding.profilePhone.details.visibility = View.VISIBLE
        binding.profileAbout.details.visibility = View.VISIBLE

        binding.imageButtonsContainer.visibility = View.GONE

        binding.profileName.textInputLayout.visibility = View.GONE
        binding.profileTitle.textInputLayout.visibility = View.GONE
        binding.profilePhone.textInputLayout.visibility = View.GONE
        binding.profileAbout.textInputLayout.visibility = View.GONE
    }

    private fun startLoading() {
        binding.blockingView.root.visibility = View.VISIBLE
        binding.editButton.text = null
        binding.progressCircular.visibility = View.VISIBLE
    }

    private fun endLoading() {
        binding.blockingView.root.visibility = View.GONE
        binding.editButton.text = getString(R.string.save)
        binding.progressCircular.visibility = View.GONE
    }

    private fun editProfile() {
        clearFocusEditTextLayout()
        val name = binding.profileName.textInputEditText.text.toString()
        val title = binding.profileTitle.textInputEditText.text.toString()
        val phone = binding.profilePhone.textInputEditText.text.toString().substring(2).trim()
        val about = binding.profileAbout.textInputEditText.text.toString()
        if (!(nameEmptyError(name) && titleEmptyError(title) && phoneEmptyError(phone) && aboutEmptyError(about))) {
            if (!(nameValidationError(name) || phoneValidationError(phone))) {
                startLoading()
                viewModel.editProfile(name, title, phone, about, profileImageFile)
                if (isProfileImageRemoved) {
                    viewModel.removeAvatar()
                }
                waitingForResponse()
            }
        }
    }

    private fun waitingForResponse() {
        viewModel.editProfileMessage.observe(this) { message ->
            if (message == "true") {
                fetchUpdatedProfile()
            } else {
                finishEditing(message)
            }
        }
    }

    private fun fetchUpdatedProfile() {
        viewModel.fetchProfile()
        viewModel.fetchProfileMessage.observe(this) { message ->
            finishEditing(message)
        }
    }

    private fun finishEditing(message: String) {
        if (message == "true") {
            Snackbar.make(binding.root, "Profile Updated Successfully", Snackbar.LENGTH_SHORT)
                .show()
        } else {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
        endEditing()
        endLoading()
    }

    private fun removeProfilePhotoButton() {
        binding.removeButton.setOnClickListener {
            isProfileImageRemoved = true
            binding.profileImage.setImageResource(R.drawable.ic_profile_default)
        }
    }

    private fun uploadProfilePhotoButton() {
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
            alertInstance.dismiss()
            takePhoto()
        }

        val choosefromGalleryButton =
            alertDialog.findViewById<MaterialCardView>(R.id.chooseFromGallery)
        choosefromGalleryButton.setOnClickListener {
            alertInstance.dismiss()
            pickImage()
        }
    }

    private fun takePhoto() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            val takePicture = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(takePicture, 0)
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), 0)
        }
    }

    private fun pickImage() {
        val pickPhoto = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
        }
        startActivityForResult(pickPhoto, 1)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (requestCode == 0) {
                val takePicture = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(takePicture, 0)
            } else if (requestCode == 1) {
                val pickPhoto =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(pickPhoto, 1)
            }
        } else {
            Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    @Deprecated(
        "This method has been deprecated in favor of using the Activity Result API\n" +
                "which brings increased type safety via an {@link ActivityResultContract} and the prebuilt\n" +
                "contracts for common intents available in\n" +
                "{@link androidx.activity.result.contract.ActivityResultContracts}, provides hooks for\n" +
                "testing, and allow receiving results in separate, testable classes independent from your\n" +
                "activity. Use\n" +
                "{@link #registerForActivityResult(ActivityResultContract, ActivityResultCallback)}\n" +
                "with the appropriate {@link ActivityResultContract} and handling the result in the\n" +
                "{@link ActivityResultCallback#onActivityResult(Object) callback}."
    )
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 0) {
                val selectedImage = data?.extras?.get("data") as Bitmap
                profileImage = selectedImage
                binding.profileImage.setImageBitmap(selectedImage)
                profileImageFile = saveBitmapToFile(selectedImage)
            } else if (requestCode == 1) {
                val selectedImageUri = data?.data
                selectedImageUri?.let { uri ->
                    val bitmap = getBitmapFromUri(uri)
                    profileImage = bitmap
                    binding.profileImage.setImageBitmap(bitmap)
                    profileImageFile = saveBitmapToFile(bitmap)
                }
            }
        }
    }

    private fun getBitmapFromUri(uri: Uri): Bitmap {
        return contentResolver.openInputStream(uri)?.use { inputStream ->
            BitmapFactory.decodeStream(inputStream)
        } ?: throw IOException("Unable to open image stream")
    }

    private fun saveBitmapToFile(bitmap: Bitmap): File {
        val file = File(cacheDir, "profile_image.jpg")
        file.outputStream().use {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
        }
        return file
    }

    private fun focusOnEditTextLayout() {
        binding.profileName.textInputEditText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                binding.profileName.textInputLayout.error = null
            } else {
                if (!nameEmptyError(binding.profileName.textInputEditText.text.toString().trim())) {
                    nameValidationError(
                        binding.profileName.textInputEditText.text.toString().trim()
                    )
                }
            }
        }
        binding.profileTitle.textInputEditText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                binding.profileTitle.textInputLayout.error = null
            } else {
                titleEmptyError(binding.profileTitle.textInputEditText.text.toString().trim())
            }
        }
        binding.profilePhone.textInputEditText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                binding.profilePhone.textInputLayout.error = null
            } else {
                if (!phoneEmptyError(
                        binding.profilePhone.textInputEditText.text.toString().trim()
                    )
                ) {
                    phoneValidationError(
                        binding.profilePhone.textInputEditText.text.toString().trim()
                    )
                }
            }
        }
        binding.profileAbout.textInputEditText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                binding.profileAbout.textInputLayout.error = null
            } else {
                aboutEmptyError(binding.profileAbout.textInputEditText.text.toString().trim())
            }
        }
    }

    private fun nameEmptyError(name: String): Boolean {
        if (name.isEmpty()) {
            binding.profileName.textInputLayout.error = getString(R.string.empty_field)
        }
        return name.isEmpty()
    }

    private fun titleEmptyError(title: String): Boolean {
        if (title.isEmpty()) {
            binding.profileTitle.textInputLayout.error = getString(R.string.empty_field)
        }
        return title.isEmpty()
    }

    private fun phoneEmptyError(phone: String): Boolean {
        if (phone.isEmpty()) {
            binding.profilePhone.textInputLayout.error = getString(R.string.empty_field)
        }
        return phone.isEmpty()
    }

    private fun aboutEmptyError(about: String): Boolean {
        if (about.isEmpty()) {
            binding.profileAbout.textInputLayout.error = getString(R.string.empty_field)
        }
        return about.isEmpty()
    }

    private fun nameValidationError(name: String): Boolean {
        if (name.length < 3) {
            binding.profileName.textInputLayout.error = getString(R.string.name_error)
        }
        return name.length < 3
    }

    private fun phoneValidationError(phone: String): Boolean {
        if (phone.length < 11) {
            binding.profilePhone.textInputLayout.error = getString(R.string.phone_error)
        }
        return phone.length < 11
    }

    private fun clearFocusEditTextLayout() {
        binding.profileName.textInputEditText.clearFocus()
        binding.profileTitle.textInputEditText.clearFocus()
        binding.profilePhone.textInputEditText.clearFocus()
        binding.profileAbout.textInputEditText.clearFocus()
    }
}