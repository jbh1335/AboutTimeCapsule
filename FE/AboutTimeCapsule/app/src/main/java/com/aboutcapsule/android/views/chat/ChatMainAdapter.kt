package com.aboutcapsule.android.views.chat

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aboutcapsule.android.databinding.ChatMainRecyclerItemBinding
import com.bumptech.glide.Glide

class ChatMainAdapter : RecyclerView.Adapter<ChatMainAdapter.ViewHolder>() {
    var itemList = mutableListOf<ChatMainData>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatMainAdapter.ViewHolder {
        val binding = ChatMainRecyclerItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ChatMainAdapter.ViewHolder, position: Int) {
        holder.bind(itemList[position])
    }


    inner class ViewHolder(val binding: ChatMainRecyclerItemBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(chatMainData: ChatMainData) {
            Glide.with(itemView).load(chatMainData.img).into(binding.chatMainImage)
            binding.chatMainNickname.text=chatMainData.nickname
            binding.chatMainContent.text = chatMainData.content
            binding.chatMainDate.text= chatMainData.date
        }
    }

    override fun getItemCount(): Int {
        return itemList.count()
    }

}