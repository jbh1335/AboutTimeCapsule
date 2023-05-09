package com.aboutcapsule.android.views.mypage

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aboutcapsule.android.databinding.MypageFriendRequestRecyclerItemBinding
import com.bumptech.glide.Glide


class MyPageFriendRequestAdapter() :
    RecyclerView.Adapter<MyPageFriendRequestAdapter.ViewHolder>() {
        var itemList = mutableListOf<MyPageFriendRequestData>()
    override fun getItemCount(): Int {
        return itemList.count()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = MypageFriendRequestRecyclerItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(itemList[position])
    }
    class ViewHolder(val binding: MypageFriendRequestRecyclerItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(myPageFriendRequestData: MyPageFriendRequestData) {
            binding.myPageFriendRequestNickname.text = myPageFriendRequestData.userName
            Glide.with(itemView).load(myPageFriendRequestData.profileImg).into(binding.myPageFriendRequestImg)
        }
    }
}