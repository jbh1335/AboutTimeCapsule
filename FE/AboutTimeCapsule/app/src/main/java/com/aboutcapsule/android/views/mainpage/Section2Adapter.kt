package com.aboutcapsule.android.views.mainpage

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aboutcapsule.android.databinding.MainSection2RecyclerItemBinding
import com.bumptech.glide.Glide

class Section2Adapter()
    : RecyclerView.Adapter<Section2Adapter.ViewHolder>() {
    var itemList = mutableListOf<Section2Data>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = MainSection2RecyclerItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(itemList[position])
    }


   inner class ViewHolder(val binding: MainSection2RecyclerItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(section2Data: Section2Data) {
            binding.mainSection2Username.text=section2Data.id
            binding.mainSection2Position.text=section2Data.place
            Glide.with(itemView).load(section2Data.img).into(binding.mainSection2Capsule)
        }
       init{
           binding.mainSection2Cardview.setOnClickListener {
               val pos = adapterPosition
               if(pos != RecyclerView.NO_POSITION && itemClickListner != null){
                   itemClickListner.onItemClick(binding.mainSection2Cardview,pos)
               }
           }
       }

    }

    override fun getItemCount(): Int {
        return itemList.count()
    }

//    리사이클러뷰의 구성요소 중 하나 클릭 시 , 클릭 이벤트를 메인프래그먼트로 넘겨주기 (커스텀 리스너)
    interface OnItemClickListner{
        fun onItemClick(view:View , position: Int)
    }
//    인터페이스 객체를 전달할 메서드와 전달받은 객체를 저장할 변수
    private lateinit var itemClickListner : OnItemClickListner
    fun setOnItemClickListner(onItemClickListner: OnItemClickListner){
        itemClickListner = onItemClickListner
    }

}
