package com.aboutcapsule.android.views.capsule

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.aboutcapsule.android.data.capsule.FriendDto
import com.aboutcapsule.android.databinding.FindFriendRecyclerItemBinding
import com.bumptech.glide.Glide

class FindFriendAdapter(val onClickDeleteIcon : (friendDto: FriendDto )-> Unit) : RecyclerView.Adapter<FindFriendAdapter.ViewHolder>() , Filterable{
    var itemList = mutableListOf<FriendDto>()
    var filterList = mutableListOf<FriendDto>()
    var itemFilter = ItemFilter()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding = FindFriendRecyclerItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FindFriendAdapter.ViewHolder, position: Int) {
        holder.bind(itemList[position])

        holder.binding.friendAddBtn.setOnClickListener{
            onClickDeleteIcon.invoke(itemList[position])
        }

    }

    inner class ViewHolder(val binding: FindFriendRecyclerItemBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(friendDto: FriendDto) {
            Glide.with(itemView).load(friendDto.profileImageUrl).into(binding.friendImg)
            binding.friendName.text=friendDto.nickname
        }
    }

    override fun getItemCount(): Int {
        return filterList.count()
    }

    override fun getFilter(): Filter {
        return itemFilter
    }

    inner class ItemFilter : Filter(){
        override fun performFiltering(c: CharSequence?): FilterResults {
            val str = c.toString()
            if(str.isEmpty()){
                filterList=itemList
            }else{
                val filteringList = mutableListOf<FriendDto>()

                for(item in itemList){
                    if(item.nickname.contains(str))
                        filteringList.add(item)
                }
                filterList = filteringList
            }

                val filterResult = FilterResults()
                filterResult.values = filterList

            return filterResult
        }

        override fun publishResults(c: CharSequence?, result: FilterResults?) {
                filterList = result?.values as MutableList<FriendDto>
                notifyDataSetChanged()
        }

    }
}