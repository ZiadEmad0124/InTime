package com.ziad_emad_dev.in_time.ui.projects.members

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.google.android.material.card.MaterialCardView
import com.ziad_emad_dev.in_time.R
import com.ziad_emad_dev.in_time.network.project.project_members.MemberRecord
import de.hdodenhof.circleimageview.CircleImageView

class MembersAdapter(private val context: Context, private val members: ArrayList<MemberRecord>, private val isAdmin: Boolean, private val adminId: String) :
    RecyclerView.Adapter<MembersAdapter.MembersViewHolder>() {

    class MembersViewHolder(itemView: View) : ViewHolder(itemView) {
        val memberCard: MaterialCardView = itemView.findViewById(R.id.memberCard)
        val memberImage: CircleImageView = itemView.findViewById(R.id.memberImage)
        val memberName: TextView = itemView.findViewById(R.id.memberName)
        val isAdmin: ImageView = itemView.findViewById(R.id.isAdmin)
        val memberTitle: TextView = itemView.findViewById(R.id.memberTitle)
        val isMemberActive: TextView = itemView.findViewById(R.id.isMemberActive)
        val memberStatus: ImageView = itemView.findViewById(R.id.memberStatus)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MembersViewHolder {
        return MembersViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.layout_member, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return members.size
    }

    override fun onBindViewHolder(holder: MembersViewHolder, position: Int) {
        val member = members[position]

        Glide.with(context)
            .load("https://intime-9hga.onrender.com/api/v1/images/${member.avatar}")
            .placeholder(R.drawable.project_image)
            .error(R.drawable.project_image)
            .into(holder.memberImage)

        holder.memberName.text = member.name
        holder.memberTitle.text = member.title

        if (member.id  == adminId) {
            holder.isAdmin.visibility = View.VISIBLE
        } else {
            holder.isAdmin.visibility = View.GONE
        }

        if (member.isActive) {
            holder.memberStatus.visibility = View.VISIBLE
            holder.isMemberActive.visibility = View.VISIBLE
        } else {
            holder.memberStatus.visibility = View.GONE
            holder.isMemberActive.visibility = View.GONE
        }

        holder.memberCard.setOnClickListener {
            val intent = Intent(context, MemberPage::class.java)
            intent.putExtra("member", member)
            intent.putExtra("isAdmin", isAdmin)
            context.startActivity(intent)
        }
    }
}