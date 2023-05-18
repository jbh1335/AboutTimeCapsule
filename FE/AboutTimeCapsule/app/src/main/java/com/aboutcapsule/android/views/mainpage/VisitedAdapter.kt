package com.aboutcapsule.android.views.mainpage

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aboutcapsule.android.R
import com.aboutcapsule.android.data.capsule.OpenedCapsuleDto
import com.aboutcapsule.android.databinding.CapsuleListRecyclerItemBinding
import com.bumptech.glide.Glide

class VisitedAdapter(private val listener: MainPageVistiedCapsuleListFragment.OnVisitedItemClickListener)  : RecyclerView.Adapter<VisitedAdapter.ViewHolder>()  {
        var itemList = mutableListOf<OpenedCapsuleDto>()

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val binding = CapsuleListRecyclerItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
            return ViewHolder(binding)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.bind(itemList[position])
        }

        inner class ViewHolder(val binding: CapsuleListRecyclerItemBinding) : RecyclerView.ViewHolder(binding.root),
            View.OnClickListener{
            fun bind(openedCapsuleDto: OpenedCapsuleDto) {
                if(openedCapsuleDto.isAdded){
                    Glide.with(itemView).load(R.drawable.newimg).into(binding.capsuleListLockOrNewImg)
                }
                Glide.with(itemView).load(R.drawable.redcapsule).into(binding.capsuleListImg)
                binding.capsuleListPlace.text=openedCapsuleDto.address
                binding.capsuleListTime.text=openedCapsuleDto.openDate.replace("-",".")
            }
            init{
                itemView.setOnClickListener(this)
            }
            override fun onClick(view: View?) {
                val position = adapterPosition
                if(position!= RecyclerView.NO_POSITION){
                    listener.onItemClick(position)
                }
            }
        }

        override fun getItemCount(): Int {
            return itemList.count()
        }

}


