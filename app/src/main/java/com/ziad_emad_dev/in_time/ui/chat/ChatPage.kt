package com.ziad_emad_dev.in_time.ui.chat

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ziad_emad_dev.in_time.databinding.ActivityChatPageBinding
import com.ziad_emad_dev.in_time.network.auth.SessionManager
import com.ziad_emad_dev.in_time.network.profile.ProfileManager
import com.ziad_emad_dev.in_time.network.project.Project
import com.ziad_emad_dev.in_time.network.socket.ChatMessage
import com.ziad_emad_dev.in_time.network.socket.SocketManager

@Suppress("DEPRECATION")
class ChatPage : AppCompatActivity() {

    private lateinit var binding: ActivityChatPageBinding
    private lateinit var socketManager: SocketManager
    private lateinit var sessionManager: SessionManager
    private lateinit var profileManager: ProfileManager
    private lateinit var adapter: ChatAdapter

    private val messages = mutableListOf<ChatMessage>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        profileManager = ProfileManager(this)
        sessionManager = SessionManager(this)

        val project: Project = intent.getParcelableExtra("project")!!
        val membersName = intent.getStringArrayListExtra("membersNames")!!
        val membersImage = intent.getStringArrayListExtra("membersAvatars")!!
        val membersIds = intent.getStringArrayListExtra("membersIds")!!

        adapter = ChatAdapter(this, profileManager.getProfileName())
        binding.chatListView.adapter = adapter

        socketManager = SocketManager(sessionManager.fetchAccessToken()!!, project.id, profileManager.getProfileName(),
            profileManager.getProfileAvatar(), adapter, membersName, membersImage, membersIds)
        socketManager.connect()

        binding.sendButton.setOnClickListener {
            val message = binding.messageEditText.text.toString().trim()
            if (message.isNotEmpty()) {
                sendMessage(project.id, message)
                binding.messageEditText.text.clear()
            }
        }

        myToolbar(project.name)
    }

    private fun myToolbar(projectName: String) {
        binding.myToolbar.title.text = projectName
        binding.myToolbar.back.setOnClickListener {
            finish()
        }
    }

    private fun sendMessage(projectId: String, message: String) {
        socketManager.sendMessage(projectId, message)
        val chatMessage = ChatMessage(profileManager.getProfileName(), message, profileManager.getProfileAvatar())
        messages.add(chatMessage)
        adapter.addMessage(chatMessage)
        scrollToBottom()
    }

    private fun scrollToBottom() {
        binding.chatListView.post {
            binding.chatListView.setSelection(adapter.count - 1)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        socketManager.disconnect()
    }
}