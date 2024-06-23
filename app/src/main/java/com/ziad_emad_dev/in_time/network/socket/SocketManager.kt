package com.ziad_emad_dev.in_time.network.socket

import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import com.ziad_emad_dev.in_time.ui.chat.ChatAdapter
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import org.json.JSONArray
import org.json.JSONObject
import java.net.URISyntaxException

class SocketManager(private val accessToken: String, private val projectId: String,
                    private val profileName: String, private val profileAvatar: String,
                    private val chatAdapter: ChatAdapter, private val membersName: List<String>,
                    private val membersImage: List<String>, private val membersIds: List<String>) {

//    Done

    private lateinit var mSocket: Socket

    fun connect() {
        try {
            val options = IO.Options().apply {
                reconnection = true
                reconnectionAttempts = 5
                reconnectionDelay = 1000
                extraHeaders = mapOf("accessToken" to listOf(accessToken))  // Correct header key
            }
            mSocket = IO.socket("https://intime-9hga.onrender.com", options)
            Log.d("SocketManager", "Socket initialized with accessToken: $accessToken")
        } catch (e: URISyntaxException) {
            Log.e("SocketManager", "Error initializing socket: ${e.localizedMessage}")
            return
        }

        mSocket.on(Socket.EVENT_CONNECT, onConnect)
        mSocket.on(Socket.EVENT_DISCONNECT, onDisconnect)
        mSocket.on(Socket.EVENT_CONNECT_ERROR, onConnectError)
        mSocket.on("chatMessage", onChatMessage)
        mSocket.on("loadOldMessages", onLoadOldMessages)
        mSocket.on("error", onError)

        mSocket.connect()
    }

    private val onConnect = Emitter.Listener {
        Log.d("SocketManager", "Socket connected")
        val joinData = JSONObject().apply {
            put("username", profileName)
            put("projectId", projectId)
        }
        Log.d("SocketManager", "Emitting joinProjectChat with data: $joinData")
        mSocket.emit("joinProjectChat", joinData)
        mSocket.emit("loadOldMessages", joinData)
    }

//

    private val onChatMessage = Emitter.Listener { args ->
        try {
            val data = args[0] as JSONObject
            Log.d("SocketManager", "New chat message: ${data.getString("message")}")
            val chatMessage = ChatMessage(
                senderName = data.getString("senderName"),
                messageText = data.getString("message"),
                senderImageResource = profileAvatar
            )
            Handler(Looper.getMainLooper()).post {
                chatAdapter.addMessage(chatMessage)
            }
        } catch (e: Exception) {
            Log.e("SocketManager", "Error processing chat message: ${e.localizedMessage}")
        }
    }

    private val onLoadOldMessages = Emitter.Listener { args ->
        try {
            val data = args[0] as JSONArray
            Log.d("SocketManager", "Old messages loaded: $data")
            val oldMessages = mutableListOf<ChatMessage>()
            for (i in 0 until data.length()) {
                val messageData = data.getJSONObject(i)
                val userId = messageData.getString("userId")
                val userIndex = membersIds.indexOf(userId)

                val senderImageResource = if (userIndex != -1) {
                    "https://intime-9hga.onrender.com/api/v1/images/${membersImage[userIndex]}"
                } else {
                    profileAvatar
                }

                val chatMessage = ChatMessage(
                    senderName = if (userIndex != -1) membersName[userIndex] else "Unknown",
                    messageText = messageData.getString("message"),
                    senderImageResource = senderImageResource
                )
                oldMessages.add(chatMessage)
            }
            Handler(Looper.getMainLooper()).post {
                chatAdapter.addMessages(oldMessages)
            }
        } catch (e: Exception) {
            Log.e("SocketManager", "Error processing old messages: ${e.localizedMessage}")
        }
    }


    fun sendMessage(projectId: String, message: String) {
        try {
            val messageData = JSONObject().apply {
                put("projectId", projectId)
                put("message", message)
            }
            Log.d("SocketManager", "Sending chatMessage with data: $messageData")
            mSocket.emit("message", messageData)
        } catch (e: Exception) {
            Log.e("SocketManager", "Error sending message: ${e.localizedMessage}")
        }
    }

    private val onDisconnect = Emitter.Listener {
        Log.d("SocketManager", "Socket disconnected")
    }

    private val onConnectError = Emitter.Listener { args ->
        Log.e("SocketManager", "Connection error: ${args[0]}")
    }

    private val onError = Emitter.Listener { args ->
        try {
            val error = args[0] as JSONObject
            Log.e("SocketManager", "Error: ${error.getString("message")}")
            Handler(Looper.getMainLooper()).post {
                Toast.makeText(chatAdapter.context, "Error: ${error.getString("message")}", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            Log.e("SocketManager", "Error processing error event: ${e.localizedMessage}")
        }
    }

    fun disconnect() {
        mSocket.disconnect()
        mSocket.off(Socket.EVENT_CONNECT, onConnect)
        mSocket.off(Socket.EVENT_DISCONNECT, onDisconnect)
        mSocket.off(Socket.EVENT_CONNECT_ERROR, onConnectError)
        mSocket.off("chatMessage", onChatMessage)
        mSocket.off("loadOldMessages", onLoadOldMessages)
        mSocket.off("error", onError)
    }
}