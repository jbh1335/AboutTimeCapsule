package com.aboutcapsule.android.views.mainpage

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.aboutcapsule.android.R
import com.bumptech.glide.Glide

class Section2Adapter()
    : RecyclerView.Adapter<Section2Adapter.ViewHolder>() {
    var itemList = mutableListOf<Section2Data>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.main_section2_recycler_item,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(itemList[position])
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val img_capsule = itemView.findViewById<ImageView>(R.id.main_section2_capsule)
        val text_username = itemView.findViewById<TextView>(R.id.main_section2_username)
        val text_userposition = itemView.findViewById<TextView>(R.id.main_section2_position)

        fun bind(item : Section2Data){
            Glide.with(itemView).load(item.img).into(img_capsule)
            text_username.text =item.id
            text_userposition.text = item.place
        }
    }

    override fun getItemCount(): Int {
        return itemList.count()
    }

}
