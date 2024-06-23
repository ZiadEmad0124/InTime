package com.ziad_emad_dev.in_time.ui.my_project

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.ziad_emad_dev.in_time.databinding.FragmentProjectWaitToStartTasksBinding
import com.ziad_emad_dev.in_time.network.project.Project
import com.ziad_emad_dev.in_time.viewmodels.ProjectViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Suppress("DEPRECATION")
class ProjectWaitToStartTasks : Fragment() {

    private var _binding: FragmentProjectWaitToStartTasksBinding? = null
    private val binding get() = _binding!!

    private val viewModel by lazy {
        ProjectViewModel(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProjectWaitToStartTasksBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val projectId: String? = arguments?.getString("projectId")
        val admin: Boolean? = arguments?.getBoolean("admin")
        val adminId: String? = arguments?.getString("adminId")
        val project: Project? = arguments?.getParcelable("project")
        val membersNames = arguments?.getStringArrayList("membersNames")
        val membersAvatars = arguments?.getStringArrayList("membersAvatars")
        val membersIds = arguments?.getStringArrayList("membersIds")

        getTasks(projectId!!, project!!, admin!!, adminId!!, membersNames!!, membersAvatars!!, membersIds!!)
    }

    private fun getCurrentDateTime(): Date {
        val calendar = Calendar.getInstance()
        return calendar.time
    }

    private fun convertDateFormat(dateString: String): Date {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", Locale.getDefault())
        return inputFormat.parse(dateString)!!
    }

    private fun isCurrentDateTimeBefore(dateString: String): Boolean {
        val currentDate = getCurrentDateTime()
        val dateToCompare = convertDateFormat(dateString)
        return currentDate.before(dateToCompare)
    }

    private fun getTasks(id: String, project: Project, admin: Boolean, adminId: String,
                         membersNames: ArrayList<String>, membersAvatars: ArrayList<String>, membersIds: ArrayList<String>) {
        viewModel.getProjectTasks(id)
        viewModel.getProjectTasksMessage.observe(viewLifecycleOwner) { message ->
            if (message == "true") {
                viewModel.getProjectTasks.observe(viewLifecycleOwner) { tasks ->
                    val waitingTasks = tasks.filter {
                        isCurrentDateTimeBefore(it.startAt) && !it.completed
                    }
                    stopLoading()
                    if (waitingTasks.isEmpty()) {
                        binding.noProjects.visibility = View.VISIBLE
                    } else {
                        binding.recyclerView.adapter = ProjectsTasksAdapter(requireContext(), waitingTasks as ArrayList, id,
                            project, admin, adminId, membersNames, membersAvatars, membersIds)
                        binding.recyclerView.setHasFixedSize(true)
                    }
                }
            } else {
                noConnection()
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun stopLoading() {
        binding.blockingView.visibility = View.GONE
        binding.blockingViewNoConnection.visibility = View.GONE
    }

    private fun noConnection() {
        binding.blockingView.visibility = View.GONE
        binding.blockingViewNoConnection.visibility = View.VISIBLE
        binding.noConnection.setOnClickListener {
            val projectId: String? = arguments?.getString("projectId")
            viewModel.getProjectTasks(projectId!!)
            binding.blockingViewNoConnection.visibility = View.GONE
            binding.blockingView.visibility = View.VISIBLE
        }
    }

    override fun onResume() {
        super.onResume()
        binding.blockingView.visibility = View.VISIBLE
        val projectId: String? = arguments?.getString("projectId")
        val admin: Boolean? = arguments?.getBoolean("admin")
        val adminId: String? = arguments?.getString("adminId")
        val project: Project? = arguments?.getParcelable("project")
        val membersNames = arguments?.getStringArrayList("membersNames")
        val membersAvatars = arguments?.getStringArrayList("membersAvatars")
        val membersIds = arguments?.getStringArrayList("membersIds")

        getTasks(projectId!!, project!!, admin!!, adminId!!, membersNames!!, membersAvatars!!, membersIds!!)
    }

    override fun onDestroy() {
        super.onDestroy()

        _binding = null
    }
}