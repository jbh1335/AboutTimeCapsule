package com.aboutcapsule.android.views.capsule

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aboutcapsule.android.data.capsule.GroupMemberDto
import com.aboutcapsule.android.databinding.DialogMemberlistRecyclerItemBinding
import com.bumptech.glide.Glide

class DialogAdapter : RecyclerView.Adapter<DialogAdapter.ViewHolder>(){
        var itemList = mutableListOf<GroupMemberDto>()

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val binding = DialogMemberlistRecyclerItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
            return ViewHolder(binding)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.bind(itemList[position])
        }

        inner class ViewHolder(val binding: DialogMemberlistRecyclerItemBinding) : RecyclerView.ViewHolder(binding.root){
            fun bind(groupMemberDto: GroupMemberDto) {
                Glide.with(itemView).load(groupMemberDto.profileImageUrl).into(binding.dialogMemberlistItemImg)
                binding.dialogMemberlistItemName.text=groupMemberDto.nickname
            }
        }

        override fun getItemCount(): Int {
            return itemList.count()
        }
}

