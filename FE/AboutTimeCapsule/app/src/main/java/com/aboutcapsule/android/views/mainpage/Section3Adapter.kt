package com.aboutcapsule.android.views.mainpage

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.aboutcapsule.android.R
import com.aboutcapsule.android.databinding.MainSection2RecyclerItemBinding
import com.aboutcapsule.android.databinding.MainSection3RecyclerItemBinding
import com.bumptech.glide.Glide

class Section3Adapter()
    : RecyclerView.Adapter<Section3Adapter.ViewHolder>() {
    var itemList = mutableListOf<Section3Data>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = MainSection3RecyclerItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(itemList[position])
    }

    inner class ViewHolder(val binding: MainSection3RecyclerItemBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(section3Data: Section3Data) {
            Glide.with(itemView).load(section3Data.img).into(binding.mainSection3Img)
            binding.mainSection3Position.text=section3Data.place
        }
    }

    override fun getItemCount(): Int {
        return itemList.count()
    }

}
