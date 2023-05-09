package com.aboutcapsule.android.views.mypage

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aboutcapsule.android.databinding.MypageAllFriendsRecyclerItemBinding
import com.bumptech.glide.Glide

class MyAllFriendsAdapter : RecyclerView.Adapter<MyAllFriendsAdapter.ViewHolder>() {
    var itemList = mutableListOf<MyAllFriendsData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = MypageAllFriendsRecyclerItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(itemList[position])
    }

    inner class ViewHolder(val binding: MypageAllFriendsRecyclerItemBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(myAllFriendsData: MyAllFriendsData) {
            Glide.with(itemView).load(myAllFriendsData.img).into(binding.myallfriendsProfile)
            binding.myallfriendsUsername.text=myAllFriendsData.text
        }
    }

    override fun getItemCount(): Int {
        return itemList.count()
    }
}