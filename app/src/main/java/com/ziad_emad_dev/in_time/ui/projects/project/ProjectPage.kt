package com.ziad_emad_dev.in_time.ui.projects.project

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.ziad_emad_dev.in_time.R
import com.ziad_emad_dev.in_time.databinding.ActivityProjectPageBinding
import com.ziad_emad_dev.in_time.network.profile.ProfileManager
import com.ziad_emad_dev.in_time.network.project.Project
import com.ziad_emad_dev.in_time.network.tasks.Task
import com.ziad_emad_dev.in_time.ui.projects.members.MembersAdapter
import com.ziad_emad_dev.in_time.viewmodels.ProjectViewModel

@Suppress("DEPRECATION")
class ProjectPage : AppCompatActivity() {

    private lateinit var binding: ActivityProjectPageBinding

    private lateinit var profileManager: ProfileManager

    private var admin = false
    private var adminId = ""

    private val viewModel by lazy {
        ProjectViewModel(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProjectPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.statusBarColor = ContextCompat.getColor(this, R.color.white_2)

        profileManager = ProfileManager(this)

        val project: Project = intent.getParcelableExtra("project")!!

        myToolbar(project.name)

        project.members.forEach {  member ->
            if (member.memberId == profileManager.getProfileId() && member.role == "admin") {
                admin = true
                adminId = member.memberId
                binding.shareProjectButton.visibility = View.VISIBLE
                binding.editProjectButton.visibility = View.VISIBLE
                binding.addTaskButtonContainer.visibility = View.VISIBLE
                binding.deleteProjectButtonContainer.visibility = View.VISIBLE
            }
        }

        setMembers(project.id)

        deleteProject(project.id)
        editProject()
        shareLink()
//        addTasks()
    }

    private fun myToolbar(projectName: String) {
        binding.myToolbar.root.background = ContextCompat.getDrawable(this, R.color.white_2)
        binding.myToolbar.title.text = projectName
        binding.myToolbar.back.setOnClickListener {
            finish()
        }
    }

    private fun getProject(id: String) {
        viewModel.getProject(id)
        viewModel.getProjectMessage.observe(this) { message ->
            if (message == "true") {
                viewModel.getProject.observe(this) { project ->
                    setProject(project)
                }
            } else {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            }
            stopLoading(message)
        }
    }

    private fun setProject(project: Project) {
        Glide.with(this)
            .load("https://intime-9hga.onrender.com/api/v1/images/${project.photo}")
            .error(R.drawable.project_image)
            .into(binding.projectCover)
    }

    private fun deleteProject(id: String) {
        binding.deleteProjectButton.setOnClickListener {
            startLoadingToDelete()
            viewModel.deleteProject(id)
            viewModel.deleteProjectMessage.observe(this) { message ->
                stopLoadingDeleting()
                if (message == "true") {
                    Toast.makeText(this, "Project Deleted Successfully", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setMembers(id: String) {
        viewModel.getMembers(id)
        viewModel.getMembersMessage.observe(this) { message ->
            if (message == "true") {
                viewModel.getMembers.observe(this) { members ->
                    binding.memberRecyclerView.adapter = MembersAdapter(this, members as ArrayList, admin, adminId)
                    binding.memberRecyclerView.setHasFixedSize(true)
                    getProject(id)
                }
            } else {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun stopLoading(message: String) {
        if (message == "true") {
            binding.blockingView.visibility = View.GONE
        } else {
            binding.blockingView.visibility = View.GONE
            binding.progressCircular.visibility = View.GONE
            binding.noConnection.visibility = View.VISIBLE

            binding.noConnection.setOnClickListener {
                binding.noConnection.visibility = View.GONE
                binding.progressCircular.visibility = View.VISIBLE
                val project: Task = intent.getParcelableExtra("project")!!
                setMembers(project.id)
            }
        }
    }

    private fun startLoadingToDelete() {
        binding.blockingView.visibility = View.VISIBLE
        binding.blockingView.setBackgroundColor(getColor(R.color.transparent))
        binding.noConnection.visibility = View.GONE
        binding.progressCircular.visibility = View.GONE
        binding.deleteProjectButton.text = null
        binding.deleteProgressCircular.visibility = View.VISIBLE
    }

    private fun stopLoadingDeleting() {
        binding.deleteProgressCircular.visibility = View.GONE
        binding.blockingView.setBackgroundColor(getColor(R.color.white))
        binding.deleteProjectButton.text = getString(R.string.delete)
        binding.blockingView.visibility = View.GONE
    }


    private fun editProject() {
        binding.editProjectButton.setOnClickListener {
            val project: Project = intent.getParcelableExtra("project")!!
            val intent = Intent(this, UpdateProject::class.java)
            intent.putExtra("project", project)
            startActivity(intent)
        }
    }

    private fun shareLink() {
        binding.shareProjectButton.setOnClickListener {
            val project: Project = intent.getParcelableExtra("project")!!
            viewModel.shareProject(project.id)
            viewModel.shareProjectMessage.observe(this) { message ->
                if (message == "true") {
                    viewModel.shareProjectLink.observe(this) { link ->
                        showBottomSheet(link)
                    }
                } else {
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    @SuppressLint("InflateParams")
    private fun showBottomSheet(link: String) {
        val dialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.layout_share_project_link, null)
        dialog.setContentView(view)

        val projectLink = view.findViewById<TextView>(R.id.projectLink)
        projectLink.text = link
        projectLink.setOnClickListener {
            val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("Link", projectLink.text)
            clipboard.setPrimaryClip(clip)

            Toast.makeText(this, "Link copied to clipboard", Toast.LENGTH_SHORT).show()
        }

        dialog.show()
    }

    override fun onResume() {
        super.onResume()
        val project: Project = intent.getParcelableExtra("project")!!
        myToolbar(project.name)
        setMembers(project.id)

        project.members.forEach {  member ->
            if (member.memberId == profileManager.getProfileId() && member.role == "admin") {
                admin = true
                binding.shareProjectButton.visibility = View.VISIBLE
                binding.editProjectButton.visibility = View.VISIBLE
                binding.addTaskButtonContainer.visibility = View.VISIBLE
                binding.deleteProjectButtonContainer.visibility = View.VISIBLE
            }
        }
    }

    @Deprecated("This method has been deprecated in favor of using the\n" +
            "{@link OnBackPressedDispatcher} via {@link #getOnBackPressedDispatcher()}.\n" +
            "The OnBackPressedDispatcher controls how back button events are dispatched\n" +
            "to one or more {@link OnBackPressedCallback} objects.")
    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}