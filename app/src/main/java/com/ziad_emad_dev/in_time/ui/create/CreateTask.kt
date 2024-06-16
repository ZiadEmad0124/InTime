package com.ziad_emad_dev.in_time.ui.create

import android.os.Bundle
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.ziad_emad_dev.in_time.R
import com.ziad_emad_dev.in_time.databinding.ActivityCreateTaskBinding
import com.ziad_emad_dev.in_time.viewmodels.TaskViewModel

class CreateTask : AppCompatActivity() {

    private lateinit var binding: ActivityCreateTaskBinding

    private var priority = 0

    private val viewModel by lazy {
        TaskViewModel(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.statusBarColor = ContextCompat.getColor(this, R.color.white_2)

        myToolbar()

        addNewStep()

        clickOnColoredButtons()

//        createTask()

        viewModel.createTaskMessage.observe(this) {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        }
    }

    private fun myToolbar() {
        binding.myToolbar.root.background = ContextCompat.getDrawable(this, R.color.white_2)
        binding.myToolbar.title.text = getString(R.string.create_a_task)
        binding.myToolbar.back.setOnClickListener {
            finish()
        }
    }

    private fun addNewStep() {
        binding.addStepButton.setOnClickListener {
            val editText = EditText(this)
            editText.background = ContextCompat.getDrawable(this, R.drawable.create_edit_text_background)
            editText.setPadding(resources.getDimensionPixelSize(R.dimen.standard_20), resources.getDimensionPixelSize(R.dimen.standard_20), resources.getDimensionPixelSize(R.dimen.standard_20), resources.getDimensionPixelSize(R.dimen.standard_20))
            editText.maxLines = 1
            editText.elevation = 5f
            editText.hint = getString(R.string.add_new_step)

            val layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            val marginHorizontal = resources.getDimensionPixelSize(R.dimen.standard_20)
            val marginVertical = resources.getDimensionPixelSize(R.dimen.standard_10)
            layoutParams.setMargins(marginHorizontal, marginVertical, resources.getDimensionPixelSize(R.dimen.standard_100), marginVertical)
            editText.layoutParams = layoutParams
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

//    private fun createTask() {
//        binding.createButton.setOnClickListener {
//            viewModel.createTask(
//                binding.taskName.text.toString(),
//                binding.taskDetails.text.toString(),
//                binding.taskTag.text.toString(),
//                priority,
////                binding.stepsLayout.children.map { it as EditText }.map { it.text.toString() },
//                listOf(
//                    "FFFF","FFFF","FFFF"
//                ),
//                null
//            )
//        }
//    }
}