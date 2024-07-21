package com.ziad_emad_dev.in_time.ui.chat

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.bumptech.glide.Glide
import com.ziad_emad_dev.in_time.R
import com.ziad_emad_dev.in_time.databinding.ItemChatMessageReceiverBinding
import com.ziad_emad_dev.in_time.databinding.ItemChatMessageSenderBinding
import com.ziad_emad_dev.in_time.network.socket.ChatMessage

class ChatAdapter(context: Context, private val currentUser: String) :
    ArrayAdapter<ChatMessage>(context, 0, mutableListOf()) {

    private val messages: MutableList<ChatMessage> = mutableListOf()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val message = messages[position]
        val view: View

        if (message.senderName == currentUser) {
            val binding: ItemChatMessageSenderBinding
            if (convertView == null || convertView.tag !is ItemChatMessageSenderBinding) {
                binding = ItemChatMessageSenderBinding.inflate(LayoutInflater.from(context), parent, false)
                view = binding.root
                view.tag = binding
            } else {
                binding = convertView.tag as ItemChatMessageSenderBinding
                view = convertView
            }
            binding.senderName.text = message.senderName
            binding.message.text = message.messageText
            Glide.with(context).load(message.senderImageResource)
                .error(R.drawable.ic_profile_default)
                .into(binding.senderImage)
        } else {
            val binding: ItemChatMessageReceiverBinding
            if (convertView == null || convertView.tag !is ItemChatMessageReceiverBinding) {
                binding = ItemChatMessageReceiverBinding.inflate(LayoutInflater.from(context), parent, false)
                view = binding.root
                view.tag = binding
            } else {
                binding = convertView.tag as ItemChatMessageReceiverBinding
                view = convertView
            }
            binding.senderName.text = message.senderName
            binding.message.text = message.messageText
            Glide.with(context).load(message.senderImageResource)
                .error(R.drawable.ic_profile_default)
                .into(binding.senderImage)
        }

        Log.d("ChatAdapter", "getView called for position $position")

        return view
    }

    override fun getCount(): Int {
        return messages.size
    }

    fun addMessage(message: ChatMessage) {
        messages.add(message)
        notifyDataSetChanged()
    }

    fun addMessages(newMessages: List<ChatMessage>) {
        messages.addAll(newMessages)
        notifyDataSetChanged()
    }
}