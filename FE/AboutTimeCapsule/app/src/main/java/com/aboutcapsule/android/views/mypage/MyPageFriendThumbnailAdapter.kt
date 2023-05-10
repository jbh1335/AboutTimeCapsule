package com.aboutcapsule.android.views.mypage

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.aboutcapsule.android.data.mypage.FriendDtoList
import com.aboutcapsule.android.databinding.MypageFriendListRecyclerItemBinding
import com.bumptech.glide.Glide

class MyPageFriendThumbnailAdapter : RecyclerView.Adapter<MyPageFriendThumbnailAdapter.ViewHolder>(){
    var itemList = mutableListOf<FriendDtoList>()

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val binding = MypageFriendListRecyclerItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)

            return ViewHolder(binding)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.bind(itemList[position])
        }
        override fun getItemCount(): Int {
            if (itemList.size < 6) {
                return itemList.count()
            } else {
                return 6
            }

        }
        inner class ViewHolder(val binding: MypageFriendListRecyclerItemBinding) : RecyclerView.ViewHolder(binding.root){
            fun bind(friendDtoList: FriendDtoList) {
                Glide.with(itemView).load(friendDtoList.profileImageUrl).into(binding.friendThumbnailItemImg)
                binding.friendThumbnailText.text = friendDtoList.nickname
            }
        }



}