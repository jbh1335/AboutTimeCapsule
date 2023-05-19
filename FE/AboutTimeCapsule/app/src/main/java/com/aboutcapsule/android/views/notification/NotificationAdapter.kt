package com.aboutcapsule.android.views.notification

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aboutcapsule.android.databinding.NotificationRecyclerItemBinding
import com.bumptech.glide.Glide

class NotificationAdapter() : RecyclerView.Adapter<NotificationAdapter.ViewHolder>() {
    var itemList = mutableListOf<NotificationData>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : ViewHolder {
        val binding = NotificationRecyclerItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(itemList[position])
    }

    inner class ViewHolder(val binding: NotificationRecyclerItemBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(notificationData: NotificationData) {
            Glide.with(itemView).load(notificationData.img).into(binding.notiImg)
            binding.notiText.text=notificationData.text
        }
    }

    override fun getItemCount(): Int {
        return itemList.count()
    }


}