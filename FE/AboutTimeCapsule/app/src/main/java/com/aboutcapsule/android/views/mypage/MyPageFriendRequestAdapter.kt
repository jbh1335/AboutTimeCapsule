package com.aboutcapsule.android.views.mypage

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aboutcapsule.android.data.mypage.FriendRequestDtoList
import com.aboutcapsule.android.databinding.MypageFriendRequestRecyclerItemBinding
import com.bumptech.glide.Glide


class MyPageFriendRequestAdapter() :
    RecyclerView.Adapter<MyPageFriendRequestAdapter.ViewHolder>() {
    interface OnItemClickListener {
        fun onItemClick(view:View, position:Int)
    }
    private lateinit var acceptFriendRequestClickListener :OnItemClickListener
    private lateinit var refuseFriendRequestClickListener :OnItemClickListener

    fun setAcceptFriendRequestClickListener(listener: OnItemClickListener) {
        this.acceptFriendRequestClickListener = listener
    }
    fun setRefuseFriendRequestClickListener(listener: OnItemClickListener) {
        this.refuseFriendRequestClickListener = listener
    }

    var itemList = mutableListOf<FriendRequestDtoList>()
    override fun getItemCount(): Int {
        return itemList.count()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = MypageFriendRequestRecyclerItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(itemList[position])
    }


    inner class ViewHolder(val binding: MypageFriendRequestRecyclerItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            acceptFriendRequestClickListener()
            refuseFriendRequestClickListener()
        }

        fun bind(friendRequestDtoList: FriendRequestDtoList) {
            binding.myPageFriendRequestNickname.text = friendRequestDtoList.nickname
            Glide.with(itemView).load(friendRequestDtoList.profileImageUrl)
                .into(binding.myPageFriendRequestImg)
        }
        fun acceptFriendRequestClickListener() {
            binding.friendRequestacceptBtn.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    acceptFriendRequestClickListener.onItemClick(binding.friendRequestacceptBtn, position)
                    Log.i("포지션", "${position}")
                }
            }
        }
        fun refuseFriendRequestClickListener() {
            binding.friendRequestRefuseBtn.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    refuseFriendRequestClickListener.onItemClick(binding.friendRequestRefuseBtn, position)
                    Log.i("포지션", "${position}")
                }
            }
        }

    }

    }


