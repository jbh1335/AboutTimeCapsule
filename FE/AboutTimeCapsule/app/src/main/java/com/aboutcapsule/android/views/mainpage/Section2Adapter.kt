package com.aboutcapsule.android.views.mainpage

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aboutcapsule.android.R
import com.aboutcapsule.android.data.capsule.AroundCapsuleDto
import com.aboutcapsule.android.databinding.MainSection2RecyclerItemBinding
import com.bumptech.glide.Glide

class Section2Adapter(private val listener: MainPageMainFragment.OnItemClickListener)
    : RecyclerView.Adapter<Section2Adapter.ViewHolder>() {
    var itemList = mutableListOf<AroundCapsuleDto>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = MainSection2RecyclerItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(itemList[position])
    }


   inner class ViewHolder(val binding: MainSection2RecyclerItemBinding) : RecyclerView.ViewHolder(binding.root) , View.OnClickListener {

        fun bind(aroundCapsuleDto: AroundCapsuleDto) {
            binding.mainSection2Username.text=aroundCapsuleDto.memberNickname
            binding.mainSection2Position.text=aroundCapsuleDto.address
            Glide.with(itemView).load(R.drawable.redcapsule).into(binding.mainSection2Capsule)
        }

       init{
           itemView.setOnClickListener(this)
       }

       override fun onClick(view: View?) {
           val position = adapterPosition
           if( position != RecyclerView.NO_POSITION){
               listener.onItemClick(position)
           }
       }

   }

    override fun getItemCount(): Int {
        return itemList.count()
    }

}
