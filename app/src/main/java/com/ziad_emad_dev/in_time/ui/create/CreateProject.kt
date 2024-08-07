package com.ziad_emad_dev.in_time.ui.create

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
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
import com.ziad_emad_dev.in_time.databinding.ActivityCreateProjectBinding
import com.ziad_emad_dev.in_time.ui.home.HomePage
import com.ziad_emad_dev.in_time.viewmodels.ProjectViewModel
import java.io.File
import java.io.IOException

@Suppress("DEPRECATION")
class CreateProject : AppCompatActivity() {

    private lateinit var binding: ActivityCreateProjectBinding

    private var projectCover: Bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)
    private var projectCoverFile: File? = null
    private var isProjectCoverEmpty = false

    private val viewModel by lazy {
        ProjectViewModel(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateProjectBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.statusBarColor = ContextCompat.getColor(this, R.color.white_2)

        myToolbar()

        uploadProjectCoverButton()

        deleteProjectCover()

        createProject()

        responseComing()
    }

    private fun myToolbar() {
        binding.myToolbar.root.background = ContextCompat.getDrawable(this, R.color.white_2)
        binding.myToolbar.title.text = getString(R.string.project)
        binding.myToolbar.back.setOnClickListener {
            val intent = Intent(this, HomePage::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun uploadProjectCoverButton() {
        binding.uploadCoverButton.setOnClickListener {
            selectImageFromCameraORGallery()
        }
    }

    private fun deleteProjectCover() {
        binding.deleteCover.setOnClickListener {
            projectCoverFile = null
            isProjectCoverEmpty = true
            binding.uploadCoverButton.text = getString(R.string.upload_cover)
            binding.deleteCover.visibility = View.GONE
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
                projectCover = selectedImage

                projectCoverFile = saveBitmapToFile(selectedImage)
            } else if (requestCode == 1) {
                val selectedImageUri = data?.data
                selectedImageUri?.let { uri ->
                    val bitmap = getBitmapFromUri(uri)
                    projectCover = bitmap

                    projectCoverFile = saveBitmapToFile(bitmap)
                }
            }
            binding.uploadCoverButton.text = getString(R.string.uploaded)
            binding.deleteCover.visibility = View.VISIBLE
            isProjectCoverEmpty = false
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

    private fun nameEmptyError(name: String): Boolean {
        if (name.isEmpty()) {
            binding.projectName.error = getString(R.string.empty_field)
        }
        return name.isEmpty()
    }

    private fun createProject() {
        binding.createButton.setOnClickListener {
            val name = binding.projectName.text.toString()

            if (!nameEmptyError(name)) {
                startLoading()
                viewModel.createProject(name, projectCoverFile)
            }
        }
    }

    private fun startLoading() {
        binding.blockingView.visibility = View.VISIBLE
        binding.createButton.setBackgroundResource(R.drawable.button_loading)
        binding.createButton.text = null
        binding.progressCircular.visibility = View.VISIBLE
    }

    private fun stopLoading() {
        binding.progressCircular.visibility = View.GONE
        binding.createButton.setBackgroundResource(R.drawable.button_background)
        binding.createButton.text = getString(R.string.create)
        binding.blockingView.visibility = View.GONE
    }

    private fun responseComing() {
        viewModel.createProjectMessage.observe(this) { message ->
            stopLoading()
            when (message) {
                "true" -> {
                    Toast.makeText(this, "Project Created Successfully", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, HomePage::class.java)
                    startActivity(intent)
                    finish()
                }

                "Failed Connect, Try Again" -> {
                    Toast.makeText(this, "Failed Connect, Try Again", Toast.LENGTH_SHORT).show()
                }

                else -> {
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}