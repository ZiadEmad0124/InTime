package com.ziad_emad_dev.in_time.ui.projects.members

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.ziad_emad_dev.in_time.R
import com.ziad_emad_dev.in_time.databinding.ActivityMemberPageBinding
import com.ziad_emad_dev.in_time.network.profile.ProfileManager
import com.ziad_emad_dev.in_time.network.project.project_members.MemberRecord

@Suppress("DEPRECATION")
class MemberPage : AppCompatActivity() {

    private lateinit var binding: ActivityMemberPageBinding

    private lateinit var profileManager: ProfileManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMemberPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        profileManager = ProfileManager(this)

        val member: MemberRecord? = intent.getParcelableExtra("member")
        val isAdmin = intent.getBooleanExtra("isAdmin", false)

        if (member != null) {
            if (isAdmin) {
                if (member.id != profileManager.getProfileId()) {
                    binding.removeButton.visibility = android.view.View.VISIBLE
                }
            }
            myToolbar(member.name)
            memberPoints(member.points.totalPoints)
            memberAvatar(member.avatar)
            memberName(member.name)
            memberTitle(member.title!!)
            memberEmail(member.email)
            memberPhone(member.phone.toString())
            memberAbout(member.about!!)
        } else {
            // Handle the case where member is null
        }
    }

    private fun myToolbar(memberName: String) {
        binding.myToolbar.title.text = memberName
        binding.myToolbar.back.setOnClickListener {
            finish()
        }
    }

    private fun memberAvatar(memberAvatar: String) {
        Glide.with(this)
            .load("https://intime-9hga.onrender.com/api/v1/images/${memberAvatar}")
            .placeholder(R.drawable.ic_profile)
            .error(R.drawable.ic_profile)
            .into(binding.profileImage)
    }

    private fun memberPoints(memberPoints: Int) {
        binding.level.text = if (memberPoints <= 100) {
            1.toString()
        } else {
            (memberPoints / 100).toString()
        }
        binding.totalPoints.text = memberPoints.toString()
    }

    private fun memberName(memberName: String) {
        binding.profileName.let { name ->
            name.icon.setImageResource(R.drawable.ic_name)
            name.title.text = getString(R.string.name)
            name.details.text = memberName
        }
    }

    private fun memberTitle(memberTitle: String) {
        binding.profileTitle.let { title ->
            title.icon.setImageResource(R.drawable.ic_title)
            title.title.text = getString(R.string.title)
            title.details.text = memberTitle
        }
    }

    private fun memberEmail(memberEmail: String) {
        binding.profileEmail.let { email ->
            email.icon.setImageResource(R.drawable.ic_email)
            email.title.text = getString(R.string.email)
            email.details.text = memberEmail
        }
    }

    private fun memberPhone(memberPhone: String) {
        binding.profilePhone.let { phone ->
            phone.icon.setImageResource(R.drawable.ic_phone)
            phone.title.text = getString(R.string.phone)
            phone.details.text = getString(R.string._0, memberPhone)
        }
    }

    private fun memberAbout(memberAbout: String) {
        binding.profileAbout.let { about ->
            about.icon.setImageResource(R.drawable.ic_about)
            about.title.text = getString(R.string.about)
            about.details.text = memberAbout
        }
    }
}