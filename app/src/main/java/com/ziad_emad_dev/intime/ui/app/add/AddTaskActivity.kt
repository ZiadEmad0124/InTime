package com.ziad_emad_dev.intime.ui.app.add

import android.os.Bundle
import android.widget.EditText
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.ziad_emad_dev.intime.R
import com.ziad_emad_dev.intime.databinding.ActivityAddTaskBinding
import com.ziad_emad_dev.intime.ui.spinner.CirclesSpinnerAdapter
import com.ziad_emad_dev.intime.ui.spinner.SpinnerColoredCircle

class AddTaskActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddTaskBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.statusBarColor = ContextCompat.getColor(this, R.color.tenth)

        myToolbar()

        clickOnColoredButton()

        addNewStep()

        coloredCirclesSpinner()
    }

    private fun myToolbar() {
        binding.myToolbar.root.background = ContextCompat.getDrawable(this, R.color.tenth)
        binding.myToolbar.title.text = getString(R.string.create_a_task)
        binding.myToolbar.back.setOnClickListener {
            finish()
        }
    }

    private fun primaryColoredButton() {
        binding.redButton.background = ContextCompat.getDrawable(this, R.drawable.circle_red)
        binding.orangeButton.background = ContextCompat.getDrawable(this, R.drawable.circle_orange)
        binding.yellowButton.background = ContextCompat.getDrawable(this, R.drawable.circle_yellow)
        binding.grayButton.background = ContextCompat.getDrawable(this, R.drawable.circle_gray)
    }

    private fun clickOnColoredButton() {
        binding.redButton.setOnClickListener {
            primaryColoredButton()
            binding.redButton.background =
                ContextCompat.getDrawable(this, R.drawable.circle_red_selected)
        }
        binding.orangeButton.setOnClickListener {
            primaryColoredButton()
            binding.orangeButton.background =
                ContextCompat.getDrawable(this, R.drawable.circle_orange_selected)
        }
        binding.yellowButton.setOnClickListener {
            primaryColoredButton()
            binding.yellowButton.background =
                ContextCompat.getDrawable(this, R.drawable.circle_yellow_selected)
        }
        binding.grayButton.setOnClickListener {
            primaryColoredButton()
            binding.grayButton.background =
                ContextCompat.getDrawable(this, R.drawable.circle_gray_selected)
        }
    }

    private fun addNewStep() {
        binding.addStepButton.setOnClickListener {
            val editText = EditText(this)
            editText.layoutParams =
                LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.FILL_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
            binding.stepLayout.addView(editText)
        }
    }

    private fun coloredCirclesSpinner() {
        val coloredCircleList = arrayListOf(
            SpinnerColoredCircle(R.drawable.circle_red),
            SpinnerColoredCircle(R.drawable.circle_orange),
            SpinnerColoredCircle(R.drawable.circle_yellow),
            SpinnerColoredCircle(R.drawable.circle_gray),
        )

        binding.coloredCirclesSpinner.adapter = CirclesSpinnerAdapter(this, coloredCircleList)
    }

}