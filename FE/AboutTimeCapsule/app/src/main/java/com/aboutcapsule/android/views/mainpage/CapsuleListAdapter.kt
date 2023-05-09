package com.aboutcapsule.android.views.mainpage

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aboutcapsule.android.databinding.CapsuleListRecyclerItemBinding
import com.bumptech.glide.Glide

class CapsuleListAdapter()
    : RecyclerView.Adapter<CapsuleListAdapter.ViewHolder>() {
    var itemList = mutableListOf<CapsuleListData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = CapsuleListRecyclerItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(itemList[position])
    }

    inner class ViewHolder(val binding: CapsuleListRecyclerItemBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(capsuleListData: CapsuleListData) {
            Glide.with(itemView).load(capsuleListData.img).into(binding.capsuleListImg)
            binding.capsuleListPlace.text=capsuleListData.place
            Glide.with(itemView).load(capsuleListData.lockimg).into(binding.capsuleListLockimg)
            binding.capsuleListTime.text=capsuleListData.time
        }
    }

    override fun getItemCount(): Int {
        return itemList.count()
    }

}
