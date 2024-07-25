package com.ziad_emad_dev.in_time.ui.on_boarding

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.ziad_emad_dev.in_time.R
import com.ziad_emad_dev.in_time.databinding.ActivityOnBoardingBinding
import com.ziad_emad_dev.in_time.ui.signing.SignPage

class OnBoarding : AppCompatActivity() {

    private lateinit var binding: ActivityOnBoardingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnBoardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.statusBarColor = getColor(R.color.white_2)

        runViewPager()

        clickOnNextButton()

        setContent()

        binding.dotsIndicator.attachTo(binding.onBoardingViewPager)
    }

    private fun runViewPager() {

        val fragmentList = arrayListOf(PageOne(), PageTwo(), PageThree())
        binding.onBoardingViewPager.adapter = OnBoardingAdapter(fragmentList, this.supportFragmentManager, lifecycle)
    }

    private fun clickOnNextButton() {
        binding.nextButton.setOnClickListener {
            if (binding.onBoardingViewPager.currentItem == 2) {
                val intent = Intent(this, SignPage::class.java)
                startActivity(intent)
                finish()
            } else {
                binding.onBoardingViewPager.currentItem += 1
            }
        }
    }

    private fun setContent() {
        binding.onBoardingViewPager.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                when (position) {
                    0 -> {
                        binding.nextButton.text = getText(R.string.next)
                        binding.describe.text = getText(R.string.onboarding_describe_1)
                    }

                    1 -> {
                        binding.nextButton.text = getText(R.string.next)
                        binding.describe.text = getText(R.string.onboarding_describe_2)
                    }

                    else -> {
                        binding.nextButton.text = getText(R.string.get_started)
                        binding.describe.text = getText(R.string.onboarding_describe_3)
                    }
                }
            }
        })
    }

}