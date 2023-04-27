package com.aboutcapsule.android.views.mainpage

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.aboutcapsule.android.R
import com.bumptech.glide.Glide

class Section3Adapter()
    : RecyclerView.Adapter<Section3Adapter.ViewHolder>() {
    var itemList = mutableListOf<Section3Data>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.main_section3_recycler_item,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(itemList[position])
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val image = itemView.findViewById<ImageView>(R.id.main_section3_img)
        val text_place = itemView.findViewById<TextView>(R.id.main_section3_position)

        fun bind(item : Section3Data){
            Glide.with(itemView).load(item.img).into(image)
            text_place.text =item.place
        }
    }

    override fun getItemCount(): Int {
        return itemList.count()
    }

}
