package com.aboutcapsule.android.views.mypage

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aboutcapsule.android.data.mypage.SearchMemberRes
import com.aboutcapsule.android.databinding.FindFriendRecyclerItemBinding
import com.aboutcapsule.android.databinding.MyPageSearchDialogItemBinding
import com.bumptech.glide.Glide

class DialogFindFriendAdapter : RecyclerView.Adapter<DialogFindFriendAdapter.ViewHolder>(){
    var itemList = mutableListOf<SearchMemberRes>()

    interface OnItemClickListener {
        fun onItemClick(view: View, position:Int)
    }
    companion object {
        private lateinit var acceptFriendRequestClickListener : OnItemClickListener
        private lateinit var refuseFriendRequestClickListener : OnItemClickListener
        private lateinit var sendFriendRequestClickListener: OnItemClickListener
        private lateinit var moveToProfileClickListener: OnItemClickListener
    }

    fun setAcceptFriendRequestClickListener(listener: OnItemClickListener) {
        acceptFriendRequestClickListener = listener
    }
    fun setRefuseFriendRequestClickListener(listener: OnItemClickListener) {
        refuseFriendRequestClickListener = listener
    }
    fun setSendFriendRequestClickListener(listener: OnItemClickListener) {
        sendFriendRequestClickListener = listener
    }
    fun setMoveToProfileClickListener(listener: OnItemClickListener) {
        moveToProfileClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = MyPageSearchDialogItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(itemList[position])
    }

    inner class ViewHolder(val binding: MyPageSearchDialogItemBinding) : RecyclerView.ViewHolder(binding.root){
        init {
            acceptFriendRequestClickListener()
            refuseFriendRequestClickListener()
            moveToProfileClickListener()
        }
        fun bind(searchMemberRes: SearchMemberRes) {
            when(searchMemberRes.state) {
                "" -> {
                    binding.searchAcceptBtn.visibility = View.GONE
                    binding.searchRefuseBtn.visibility = View.GONE
                }
                "승인/거절" -> {
                    binding.searchAcceptBtn.visibility = View.VISIBLE
                    binding.searchRefuseBtn.visibility = View.VISIBLE
                    binding.searchAcceptBtn.text = "승인"
                    binding.searchRefuseBtn.text = "거절"
                }
                "요청" -> {
                    binding.searchAcceptBtn.visibility = View.VISIBLE
                    binding.searchAcceptBtn.text = "요청"
                    binding.searchRefuseBtn.visibility = View.GONE
                }
            }
            Glide.with(itemView).load(searchMemberRes.profileImage).into(binding.myallfriendsProfile)
            binding.myallfriendsUsername.text=searchMemberRes.nickname
           }
        fun acceptFriendRequestClickListener() {
            binding.searchAcceptBtn.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    acceptFriendRequestClickListener.onItemClick(binding.searchAcceptBtn, position)
                }
            }
        }
        fun refuseFriendRequestClickListener() {
            binding.searchRefuseBtn.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    refuseFriendRequestClickListener.onItemClick(binding.searchRefuseBtn, position)
                    Log.i("포지션", "${position}")
                }
            }
        }
        fun moveToProfileClickListener() {
            binding.searchedUserName.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    moveToProfileClickListener.onItemClick(binding.searchedUserName, position)
                }
            }
        }
//        fun sendFriendRequestClickListener() {
//
//        }

    }
    override fun getItemCount(): Int {
        return itemList.count()
    }

}