package com.aboutcapsule.android.views.capsule

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aboutcapsule.android.databinding.CommentsRecyclerItemBinding
import com.bumptech.glide.Glide

class CommentsAdapter : RecyclerView.Adapter<CommentsAdapter.ViewHolder>() {
        var itemList = mutableListOf<CommentsData>()

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val binding = CommentsRecyclerItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
            return ViewHolder(binding)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.bind(itemList[position])
        }

        inner class ViewHolder(val binding: CommentsRecyclerItemBinding) : RecyclerView.ViewHolder(binding.root){
            fun bind(commentsData: CommentsData) {
                Glide.with(itemView).load(commentsData.img).into(binding.commentsItemImg)
                binding.commentsItemName.text=commentsData.nickname
                binding.commentsItemDate.text=commentsData.date
                binding.commentsItemComment.text=commentsData.comment
            }
        }

        override fun getItemCount(): Int {
            return itemList.count()
        }
 }

