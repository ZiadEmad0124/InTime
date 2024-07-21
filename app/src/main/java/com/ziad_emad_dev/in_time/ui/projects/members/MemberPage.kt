package com.ziad_emad_dev.in_time.ui.projects.members

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.ziad_emad_dev.in_time.R
import com.ziad_emad_dev.in_time.databinding.ActivityMemberPageBinding
import com.ziad_emad_dev.in_time.network.project.project_members.MemberRecord
import com.ziad_emad_dev.in_time.ui.my_project.project_task.CreateTaskProject
import com.ziad_emad_dev.in_time.viewmodels.ProjectViewModel

@Suppress("DEPRECATION")
class MemberPage : AppCompatActivity() {

    private lateinit var binding: ActivityMemberPageBinding

    private val viewModel by lazy {
        ProjectViewModel(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMemberPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val member: MemberRecord = intent.getParcelableExtra("member")!!
        val isAdmin = intent.getBooleanExtra("isAdmin", false)
        val adminId = intent.getStringExtra("adminId")!!
        val projectId = intent.getStringExtra("projectId")!!

        myToolbar(member.name)

        if (isAdmin) {
            if (member.id != adminId) {
                binding.addTaskButtonContainer.visibility = View.VISIBLE
                binding.myToolbar.menu.visibility = View.VISIBLE
            }
        }

        memberPoints(member.points.totalPoints)
        memberAvatar(member.avatar)
        memberName(member.name)
        memberTitle(member.title)
        memberEmail(member.email)
        memberPhone(member.phone.toString())
        memberAbout(member.about)

        createTask(projectId, member.id)
    }

    private fun myToolbar(memberName: String) {
        binding.myToolbar.title.text = memberName
        binding.myToolbar.back.setOnClickListener {
            finish()
        }
        binding.myToolbar.menu.setImageResource(R.drawable.ic_options)
        binding.myToolbar.menu.setOnClickListener {
            showPopupMenu(it)
        }
        binding.myToolbar.menu.visibility = View.GONE
    }

    private fun showPopupMenu(view: View) {
        val popupMenu = PopupMenu(this, view, 0, 0, R.style.PopupMenuStyle)
        popupMenu.menuInflater.inflate(R.menu.member_menu, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.removeMember -> {
                    val member: MemberRecord = intent.getParcelableExtra("member")!!
                    val projectId = intent.getStringExtra("projectId")!!
                    removeMember(projectId, member.id)
                    true
                }
                else -> false
            }
        }
        popupMenu.show()
    }

    private fun memberAvatar(memberAvatar: String) {
        Glide.with(this)
            .load("https://intime-9hga.onrender.com/api/v1/images/${memberAvatar}")
            .placeholder(R.drawable.ic_profile_default)
            .error(R.drawable.ic_profile_default)
            .into(binding.profileImage)
    }

    private fun memberPoints(memberPoints: Int) {
        binding.level.text = if (memberPoints <= 100) {
            1.toString()
        } else {
            ((memberPoints / 100)+1).toString()
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

    private fun memberTitle(memberTitle: String?) {
        binding.profileTitle.let { title ->
            title.icon.setImageResource(R.drawable.ic_title)
            title.title.text = getString(R.string.title)
            title.details.text = if (memberTitle.isNullOrEmpty()) "No Title" else memberTitle
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

    private fun memberAbout(memberAbout: String?) {
        binding.profileAbout.let { about ->
            about.icon.setImageResource(R.drawable.ic_about)
            about.title.text = getString(R.string.about)
            about.details.text = if (memberAbout.isNullOrEmpty()) "No About" else memberAbout
        }
    }

    private fun removeMember(projectId: String, memberId: String) {
        startLoading()
        viewModel.removeMember(projectId, memberId)
        viewModel.removeMembersMessage.observe(this) { message ->
            if (message == "true") {
                finish()
            } else {
                failedConnect(message)
            }
        }
    }

    private fun createTask(projectId: String, memberId: String) {
        binding.addTaskButton.setOnClickListener {
            val intent = Intent(this, CreateTaskProject::class.java)
            intent.putExtra("projectId", projectId)
            intent.putExtra("memberId", memberId)
            startActivity(intent)
        }
    }

    private fun startLoading() {
        binding.blockingView.visibility = View.VISIBLE
        binding.progressCircular.visibility = View.VISIBLE
    }

    private fun failedConnect(message: String) {
        binding.blockingView.visibility = View.GONE
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}