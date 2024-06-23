package com.ziad_emad_dev.in_time.ui.projects.project

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.PopupMenu
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
import com.ziad_emad_dev.in_time.ui.chat.ChatPage
import com.ziad_emad_dev.in_time.ui.my_project.MyProject
import com.ziad_emad_dev.in_time.ui.projects.members.MembersAdapter
import com.ziad_emad_dev.in_time.viewmodels.ProjectViewModel

@Suppress("DEPRECATION")
class ProjectPage : AppCompatActivity() {

    private lateinit var binding: ActivityProjectPageBinding

    private lateinit var profileManager: ProfileManager

    private var membersNames = ArrayList<String>()
    private var membersAvatars = ArrayList<String>()
    private var membersIds = ArrayList<String>()

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

        for (member in project.members) {
            if (member.role == "admin") {
                if (member.memberId == profileManager.getProfileId()) {
                    admin = true
                }
                adminId = member.memberId
                binding.myToolbar.menu.visibility = View.VISIBLE
            }
        }

        setMembers(project.id)
        viewTasks()
        goToChat()
    }

    private fun goToChat() {
        binding.chatButton.setOnClickListener {
            val project: Project = intent.getParcelableExtra("project")!!
            val intent = Intent(this, ChatPage::class.java)
            intent.putExtra("project", project)
            intent.putExtra("membersNames", membersNames)
            intent.putExtra("membersAvatars", membersAvatars)
            intent.putExtra("membersIds", membersIds)
            startActivity(intent)
        }
    }

    private fun myToolbar(projectName: String) {
        binding.myToolbar.root.background = ContextCompat.getDrawable(this, R.color.white_2)
        binding.myToolbar.title.text = projectName
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
        popupMenu.menuInflater.inflate(R.menu.project_menu, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.editProject -> {
                    editProject()
                    true
                }
                R.id.deleteProject -> {
                    val task: Task = intent.getParcelableExtra("task")!!
                    deleteProject(task.id)
                    true
                }
                R.id.shareProject -> {
                    shareLink()
                    true
                }
                else -> false
            }
        }
        popupMenu.show()
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

    private fun setMembers(id: String) {
        viewModel.getMembers(id)
        viewModel.getMembersMessage.observe(this) { message ->
            if (message == "true") {
                viewModel.getMembers.observe(this) { members ->
                    binding.memberRecyclerView.adapter = MembersAdapter(this, members as ArrayList, id, admin, adminId)
                    binding.memberRecyclerView.setHasFixedSize(true)
                    getProject(id)
                    for (member in members) {
                        membersNames.add(member.name)
                        membersAvatars.add(member.avatar)
                        membersIds.add(member.id)
                    }
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
        binding.progressCircular.visibility = View.VISIBLE
    }

    private fun stopLoadingDeleting() {
        binding.blockingView.setBackgroundColor(getColor(R.color.white))
        binding.blockingView.visibility = View.GONE
        binding.progressCircular.visibility = View.GONE
    }

    private fun editProject() {
        val project: Project = intent.getParcelableExtra("project")!!
        val intent = Intent(this, UpdateProject::class.java)
        intent.putExtra("project", project)
        startActivity(intent)
    }

    private fun shareLink() {
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
                binding.myToolbar.menu.visibility = View.VISIBLE
            }
        }
    }

    private fun viewTasks() {
        binding.viewTasks.setOnClickListener {
            val project: Project = intent.getParcelableExtra("project")!!
            val intent = Intent(this, MyProject::class.java)
            intent.putExtra("projectId", project.id)
            intent.putExtra("admin", admin)
            intent.putExtra("adminId", adminId)
            intent.putExtra("project", project)
            intent.putExtra("membersNames", membersNames)
            intent.putExtra("membersAvatars", membersAvatars)
            intent.putExtra("membersIds", membersIds)
            startActivity(intent)
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