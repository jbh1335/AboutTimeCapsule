package com.aboutcapsule.android.views.mypage

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aboutcapsule.android.data.mypage.AllFriendRes
import com.aboutcapsule.android.databinding.MypageAllFriendsRecyclerItemBinding
import com.bumptech.glide.Glide

class MyAllFriendsAdapter : RecyclerView.Adapter<MyAllFriendsAdapter.ViewHolder>() {
    var itemList = mutableListOf<AllFriendRes>()
    interface OnItemClickListener {
        fun onItemClick(view: View, position:Int)
    }

    private lateinit var toFriendPageClickListener : OnItemClickListener
    fun setToFriendPageClickListener(listener: OnItemClickListener) {
        this.toFriendPageClickListener = listener
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = MypageAllFriendsRecyclerItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(itemList[position])
    }

    override fun getItemCount(): Int {
        return itemList.count()
    }

    inner class ViewHolder(val binding: MypageAllFriendsRecyclerItemBinding) : RecyclerView.ViewHolder(binding.root){
        init {
            toFriendPageClickListener()
        }
        fun bind(allFriendRes: AllFriendRes) {
            Glide.with(itemView).load(allFriendRes.profileImageUrl).into(binding.myallfriendsProfile)
            binding.myallfriendsUsername.text=allFriendRes.nickname
        }
        fun toFriendPageClickListener() {
            binding.myPageAllfriendItem.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    toFriendPageClickListener.onItemClick(binding.myPageAllfriendItem, position)
                    Log.i("포지션", "${position}")
                }
            }
        }
    }


}