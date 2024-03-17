package com.ziad_emad_dev.intime.ui.onboarding

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.ziad_emad_dev.intime.R
import com.ziad_emad_dev.intime.databinding.ActivityOnBoardingBinding
import com.ziad_emad_dev.intime.ui.signing.SignPage

class OnBoarding : AppCompatActivity() {

    private lateinit var binding: ActivityOnBoardingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnBoardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.statusBarColor = getColor(R.color.white2)

        makeViewPager()

        clickOnNextButton()

        setNextButtonText()

        binding.dotsIndicator.attachTo(binding.onBoardingViewPager)
    }

    private fun makeViewPager() {

        val fragmentList = arrayListOf(
            PageOne(), PageTwo(), PageThree()
        )

        binding.onBoardingViewPager.adapter =
            OnBoardingAdapter(fragmentList, this.supportFragmentManager, lifecycle)
    }

    private fun clickOnNextButton() {
        binding.next.setOnClickListener {
            if (binding.onBoardingViewPager.currentItem == 2) {
                val intent = Intent(this, SignPage::class.java)
                startActivity(intent)
            } else {
                binding.onBoardingViewPager.currentItem += 1
            }
        }
    }

    private fun setNextButtonText() {
        binding.onBoardingViewPager.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                if (position == 2) {
                    binding.next.text = getText(R.string.get_started)
                } else {
                    binding.next.text = getText(R.string.next)
                }
            }
        })
    }

}