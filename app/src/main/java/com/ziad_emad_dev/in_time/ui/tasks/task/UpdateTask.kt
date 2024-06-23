package com.ziad_emad_dev.in_time.ui.tasks.task

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.card.MaterialCardView
import com.ziad_emad_dev.in_time.R
import com.ziad_emad_dev.in_time.databinding.ActivityUpdateTaskBinding
import com.ziad_emad_dev.in_time.network.tasks.Tag
import com.ziad_emad_dev.in_time.network.tasks.Task
import com.ziad_emad_dev.in_time.network.tasks.Values
import com.ziad_emad_dev.in_time.viewmodels.TaskViewModel
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

@Suppress("DEPRECATION")
class UpdateTask : AppCompatActivity() {

    private lateinit var binding: ActivityUpdateTaskBinding

    private var priority = 0

    private lateinit var startDate: String
    private lateinit var endDate: String

    private var taskCover: Bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)
    private var taskCoverFile: File? = null
    private var isTaskCoverEmpty = false
    private var isDeleteTaskCover = false

    private val viewModel by lazy {
        TaskViewModel(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.statusBarColor = ContextCompat.getColor(this, R.color.white_2)

        myToolbar()

        fillForm()

        clickOnColoredButtons()

        getStartDate()
        getEndDate()

        uploadTaskCoverButton()
        deleteTaskCover()

        addNewStep()

        updateTask()

        responseComing()
    }

    private fun myToolbar() {
        binding.myToolbar.root.background = ContextCompat.getDrawable(this, R.color.white_2)
        binding.myToolbar.title.text = getString(R.string.update_task)
        binding.myToolbar.back.setOnClickListener {
            finish()
        }
    }

    private fun fillForm(){
        val task: Task = intent.getParcelableExtra("task")!!

        binding.taskName.setText(task.name)
        binding.taskDescription.setText(task.disc)
        if (task.tag?.name?.isEmpty() == false) {
            binding.taskTag.setText(task.tag.name)
        }

        if (!task.image.isNullOrEmpty()) {
            binding.deleteCover.visibility = View.VISIBLE
            isTaskCoverEmpty = false
        }

        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        inputFormat.timeZone = TimeZone.getTimeZone("UTC")
        val outputFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        outputFormat.timeZone = TimeZone.getTimeZone("UTC")

        val startDateView = inputFormat.parse(task.startAt)
        val endDateView = inputFormat.parse(task.endAt)

        val startDateView1 = inputFormat.parse(task.startAt)
        val endDateView1 = inputFormat.parse(task.endAt)

        val startDateCalendar = Calendar.getInstance(TimeZone.getTimeZone("UTC")).apply {
            time = startDateView!!
//            add(Calendar.HOUR_OF_DAY, 6)
        }

        val endDateCalendar = Calendar.getInstance(TimeZone.getTimeZone("UTC")).apply {
            time = endDateView!!
//            add(Calendar.HOUR_OF_DAY, 6)
        }

        startDate = outputFormat.format(startDateCalendar.time)
        endDate = outputFormat.format(endDateCalendar.time)

        binding.startDateButton.text = outputFormat.format(startDateView1!!)
        binding.endDateButton.text = outputFormat.format(endDateView1!!)

        when (task.priority) {
            0 -> {
                primaryColorsForButtons()
                binding.redButton.background = ContextCompat.getDrawable(this, R.drawable.circle_red_selected)
                priority = 0
            }
            1 -> {
                primaryColorsForButtons()
                binding.orangeButton.background = ContextCompat.getDrawable(this, R.drawable.circle_orange_selected)
                priority = 1
            }
            2 -> {
                primaryColorsForButtons()
                binding.yellowButton.background = ContextCompat.getDrawable(this, R.drawable.circle_yellow_selected)
                priority = 2
            }
            else -> {
                primaryColorsForButtons()
                binding.grayButton.background = ContextCompat.getDrawable(this, R.drawable.circle_gray_selected)
                priority = 3
            }
        }

        val steps = task.steps
        for (step in steps) {
            val editText = EditText(this)
            editText.background = ContextCompat.getDrawable(this, R.drawable.create_edit_text_background)
            editText.text = Editable.Factory.getInstance().newEditable(step.stepDisc)
            editText.setPadding(resources.getDimensionPixelSize(R.dimen.standard_20), resources.getDimensionPixelSize(R.dimen.standard_20), resources.getDimensionPixelSize(R.dimen.standard_20), resources.getDimensionPixelSize(R.dimen.standard_20))
            editText.maxLines = 3
            editText.elevation = 5f
            editText.hint = getString(R.string.add_new_step)
            editText.setTextColor(ContextCompat.getColor(this, R.color.secondary))

            val layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            val marginHorizontal = resources.getDimensionPixelSize(R.dimen.standard_20)
            val marginVertical = resources.getDimensionPixelSize(R.dimen.standard_10)
            layoutParams.setMargins(marginHorizontal, marginVertical, resources.getDimensionPixelSize(
                R.dimen.standard_100), marginVertical)
            editText.layoutParams = layoutParams

            editText.setOnLongClickListener {
                showBottomSheet(it)
                true
            }
            binding.stepsLayout.addView(editText)
        }
    }

    private fun primaryColorsForButtons() {
        binding.redButton.background = ContextCompat.getDrawable(this, R.drawable.circle_red)
        binding.orangeButton.background = ContextCompat.getDrawable(this, R.drawable.circle_orange)
        binding.yellowButton.background = ContextCompat.getDrawable(this, R.drawable.circle_yellow)
        binding.grayButton.background = ContextCompat.getDrawable(this, R.drawable.circle_gray)
    }

    private fun clickOnColoredButtons() {
        binding.redButton.setOnClickListener {
            primaryColorsForButtons()
            binding.redButton.background = ContextCompat.getDrawable(this, R.drawable.circle_red_selected)
            priority = 0
        }
        binding.orangeButton.setOnClickListener {
            primaryColorsForButtons()
            binding.orangeButton.background = ContextCompat.getDrawable(this, R.drawable.circle_orange_selected)
            priority = 1
        }
        binding.yellowButton.setOnClickListener {
            primaryColorsForButtons()
            binding.yellowButton.background = ContextCompat.getDrawable(this, R.drawable.circle_yellow_selected)
            priority = 2
        }
        binding.grayButton.setOnClickListener {
            primaryColorsForButtons()
            binding.grayButton.background = ContextCompat.getDrawable(this, R.drawable.circle_gray_selected)
            priority = 3
        }
    }

    private fun getStartDate() {
        binding.startDateButton.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(this, R.style.InTime_PickerDialogTheme, { _, selectedYear, selectedMonth, selectedDay ->
                val timePickerDialog = TimePickerDialog(this, R.style.InTime_PickerDialogTheme, { _, selectedHour, selectedMinute ->
                    val selectedDate = Calendar.getInstance()
                    selectedDate.set(selectedYear, selectedMonth, selectedDay, selectedHour, selectedMinute)
                    if (selectedDate.before(calendar)) {
                        Toast.makeText(this, "Start date must be after current date", Toast.LENGTH_SHORT).show()
                    } else {
                        startDate = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(selectedDate.time)
                        binding.startDateButton.text = startDate
                    }
                }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true)

                timePickerDialog.setOnShowListener {
                    timePickerDialog.getButton(TimePickerDialog.BUTTON_POSITIVE).setTextColor(
                        ContextCompat.getColor(this, R.color.primary))
                    timePickerDialog.getButton(TimePickerDialog.BUTTON_NEGATIVE).setTextColor(
                        ContextCompat.getColor(this, R.color.primary))
                }
                timePickerDialog.show()
            }, year, month, day)

            datePickerDialog.setOnShowListener {
                datePickerDialog.getButton(DatePickerDialog.BUTTON_POSITIVE).setTextColor(
                    ContextCompat.getColor(this, R.color.primary))
                datePickerDialog.getButton(DatePickerDialog.BUTTON_NEGATIVE).setTextColor(
                    ContextCompat.getColor(this, R.color.primary))
            }
            datePickerDialog.show()
        }
    }

    private fun getEndDate() {
        binding.endDateButton.setOnClickListener {
            if (startDate.isEmpty()) {
                Toast.makeText(this, "Please select a start date first", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(this, R.style.InTime_PickerDialogTheme, { _, selectedYear, selectedMonth, selectedDay ->
                val timePickerDialog = TimePickerDialog(this, R.style.InTime_PickerDialogTheme, { _, selectedHour, selectedMinute ->
                    val selectedDate = Calendar.getInstance()
                    selectedDate.set(selectedYear, selectedMonth, selectedDay, selectedHour, selectedMinute)
                    val startDateCalendar = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).parse(startDate)
                    if (selectedDate.time.before(startDateCalendar)) {
                        Toast.makeText(this, "End date must be after start date", Toast.LENGTH_SHORT).show()
                    } else {
                        endDate = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(selectedDate.time)
                        binding.endDateButton.text = endDate
                    }
                }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true)

                timePickerDialog.setOnShowListener {
                    timePickerDialog.getButton(TimePickerDialog.BUTTON_POSITIVE).setTextColor(
                        ContextCompat.getColor(this, R.color.primary))
                    timePickerDialog.getButton(TimePickerDialog.BUTTON_NEGATIVE).setTextColor(
                        ContextCompat.getColor(this, R.color.primary))
                }
                timePickerDialog.show()
            }, year, month, day)

            datePickerDialog.setOnShowListener {
                datePickerDialog.getButton(DatePickerDialog.BUTTON_POSITIVE).setTextColor(
                    ContextCompat.getColor(this, R.color.primary))
                datePickerDialog.getButton(DatePickerDialog.BUTTON_NEGATIVE).setTextColor(
                    ContextCompat.getColor(this, R.color.primary))
            }
            datePickerDialog.show()
        }
    }

    private fun convertToISO8601(date: String): String {
        val originalFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        val targetFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
        targetFormat.timeZone = TimeZone.getTimeZone("UTC")
        val dateObj = originalFormat.parse(date)
        return targetFormat.format(dateObj!!)
    }

    private fun uploadTaskCoverButton() {
        binding.uploadCoverButton.setOnClickListener {
            selectImageFromCameraORGallery()
        }
    }

    private fun deleteTaskCover() {
        binding.deleteCover.setOnClickListener {
            taskCoverFile = null
            isTaskCoverEmpty = true
            isDeleteTaskCover = true
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
                taskCover = selectedImage

                taskCoverFile = saveBitmapToFile(selectedImage)
            } else if (requestCode == 1) {
                val selectedImageUri = data?.data
                selectedImageUri?.let { uri ->
                    val bitmap = getBitmapFromUri(uri)
                    taskCover = bitmap

                    taskCoverFile = saveBitmapToFile(bitmap)
                }
            }
            binding.uploadCoverButton.text = getString(R.string.uploaded)
            binding.deleteCover.visibility = View.VISIBLE
            isTaskCoverEmpty = false
            isDeleteTaskCover = false
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

    private fun addNewStep() {
        binding.addStepButton.setOnClickListener {
            val editText = EditText(this)
            editText.background = ContextCompat.getDrawable(this, R.drawable.create_edit_text_background)
            editText.setPadding(resources.getDimensionPixelSize(R.dimen.standard_20), resources.getDimensionPixelSize(R.dimen.standard_20), resources.getDimensionPixelSize(R.dimen.standard_20), resources.getDimensionPixelSize(R.dimen.standard_20))
            editText.maxLines = 3
            editText.elevation = 5f
            editText.hint = getString(R.string.add_new_step)
            editText.setTextColor(ContextCompat.getColor(this, R.color.secondary))

            val layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            val marginHorizontal = resources.getDimensionPixelSize(R.dimen.standard_20)
            val marginVertical = resources.getDimensionPixelSize(R.dimen.standard_10)
            layoutParams.setMargins(marginHorizontal, marginVertical, resources.getDimensionPixelSize(
                R.dimen.standard_100), marginVertical)
            editText.layoutParams = layoutParams

            editText.setOnLongClickListener {
                showBottomSheet(it)
                true
            }
            binding.stepsLayout.addView(editText)
        }
    }

    @SuppressLint("InflateParams")
    private fun showBottomSheet(step: View) {
        val dialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.layout_steps_bottom_sheet, null)
        dialog.setContentView(view)

        val deleteButton = view.findViewById<View>(R.id.deleteButton)
        deleteButton.setOnClickListener {
            binding.stepsLayout.removeView(step)
            dialog.dismiss()
        }
        val cancelButton = view.findViewById<View>(R.id.cancelButton)
        cancelButton.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun getStepsList(): List<String> {
        val stepsList = mutableListOf<String>()
        for (i in 0 until binding.stepsLayout.childCount) {
            val view = binding.stepsLayout.getChildAt(i)
            if (view is EditText) {
                stepsList.add(view.text.toString())
            }
        }
        return stepsList
    }

    private fun startDateEmptyError(start: String): Boolean {
        if (start.isEmpty()) {
            Toast.makeText(this, "Please select a start date", Toast.LENGTH_SHORT).show()
        }
        return start.isEmpty()
    }

    private fun endDateEmptyError(end: String): Boolean {
        if (end.isEmpty()) {
            Toast.makeText(this, "Please select a end date", Toast.LENGTH_SHORT).show()
        }
        return end.isEmpty()
    }

    private fun updateTask() {
        binding.updateButton.setOnClickListener {
            val task: Task = intent.getParcelableExtra("task")!!
            val id = task.id
            val name = if (binding.taskName.text.toString() == task.name) null else  binding.taskName.text.toString()
            val description = binding.taskDescription.text.toString()

            val steps = getStepsList()

            val tagName = binding.taskTag.text.toString()

            if (!startDateEmptyError(startDate)) {
                if (!endDateEmptyError(endDate)) {
                    startLoading()
                    viewModel.getAllTag()
                    viewModel.getTagsMessage.observe(this) { message ->
                        if (message == "true") {
                            var allTags : List<Tag>
                            viewModel.getTags.observe(this) { tags ->
                                allTags = tags
                                val tag = setTagColor(Values(tagName), allTags, colors)
                                val startAt = convertToISO8601(startDate)
                                val endAt = convertToISO8601(endDate)

                                if (isDeleteTaskCover) {
                                    viewModel.removeCover(id)
                                    viewModel.removeCoverMessage.observe(this) { message ->
                                        if (message == "true") {
                                            if (isTaskCoverEmpty) {
                                                viewModel.updateTask(id, name, description, priority.toString(), startAt, endAt, taskCoverFile, steps, tag)
                                            } else {
                                                stopLoading()
                                                Toast.makeText(this, "Remove Cover Failed", Toast.LENGTH_SHORT).show()
                                            }
                                        }
                                    }
                                } else {
                                    viewModel.updateTask(id, name, description, priority.toString(), startAt, endAt, taskCoverFile, steps, tag)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun startLoading() {
        binding.blockingView.visibility = View.VISIBLE
        binding.updateButton.setBackgroundResource(R.drawable.button_loading)
        binding.updateButton.text = null
        binding.progressCircular.visibility = View.VISIBLE
    }

    private fun stopLoading() {
        binding.progressCircular.visibility = View.GONE
        binding.updateButton.setBackgroundResource(R.drawable.button_background)
        binding.updateButton.text = getString(R.string.update)
        binding.blockingView.visibility = View.GONE
    }

    private fun responseComing() {
        viewModel.updateTaskMessage.observe(this) { message ->
            stopLoading()
            when (message) {
                "true" -> {
                    Toast.makeText(this, "Task Updated Successfully", Toast.LENGTH_SHORT).show()
                    finish()
                }

                "Failed Connect, Try Again" -> {
                    if (binding.taskTag.text.toString().isNotEmpty()) {
                        Toast.makeText(this, "Task Updated Successfully", Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        Toast.makeText(this, "Failed Connect, Try Again", Toast.LENGTH_SHORT).show()
                    }
                }

                else -> {
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setTagColor(values: Values, allTags: List<Tag>, colors: List<String>): Tag {
        if (values.tag.trim() == "") {
            return Tag("", "")
        } else {
            val tag = Tag(values.tag, "")
            val tagName = values.tag.lowercase()

            val tagIndex = allTags.indexOfFirst { it.name.lowercase() == tagName }

            if (tagIndex != -1) {
                tag.color = allTags[tagIndex].color
            } else {
                if (allTags.isNotEmpty()) {
                    val lastColor = allTags.last().color
                    val lastColorIndex = colors.indexOf(lastColor)
                    tag.color = colors[(lastColorIndex + 1) % 50]
                } else {
                    tag.color = colors[0]
                }
            }
            return tag
        }
    }

    private val colors = listOf(
        "darkseagreen",
        "crimson",
        "chocolate",
        "mediumseagreen",
        "dark-turquoise",
        "mediumorchid",
        "slateblue",
        "cyan",
        "saddlebrown",
        "firebrick",
        "lime",
        "salmon",
        "mediumvioletred",
        "teal",
        "gold",
        "orchid",
        "darkred",
        "darkgoldenrod",
        "purple",
        "darkolivegreen",
        "navy",
        "darkorange",
        "mediumblue",
        "seagreen",
        "maroon",
        "sienna",
        "magenta",
        "indianred",
        "steelblue",
        "sandybrown",
        "yellow",
        "blue",
        "violet",
        "coral",
        "mediumslateblue",
        "pink",
        "dimgray",
        "indigo",
        "royalblue",
        "red",
        "turquoise",
        "green",
        "tomato",
        "darkviolet",
        "slategray",
        "dodgerblue",
        "mediumpurple",
        "brown",
        "orange",
        "forestgreen"
    )
}