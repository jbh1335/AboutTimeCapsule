package com.aboutcapsule.android.views.mypage

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aboutcapsule.android.databinding.FindFriendRecyclerItemBinding
import com.bumptech.glide.Glide

class DialogFindFriendAdapter : RecyclerView.Adapter<DialogFindFriendAdapter.ViewHolder>(){
        var itemList = mutableListOf<DialogFindFriendData>()

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val binding = FindFriendRecyclerItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
            return ViewHolder(binding)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.bind(itemList[position])
        }

        inner class ViewHolder(val binding: FindFriendRecyclerItemBinding) : RecyclerView.ViewHolder(binding.root){
            fun bind(dialogFindFriendData: DialogFindFriendData) {
                Glide.with(itemView).load(dialogFindFriendData.img).into(binding.friendImg)
                binding.friendName.text=dialogFindFriendData.name
               }
        }

        override fun getItemCount(): Int {
            return itemList.count()
        }
}