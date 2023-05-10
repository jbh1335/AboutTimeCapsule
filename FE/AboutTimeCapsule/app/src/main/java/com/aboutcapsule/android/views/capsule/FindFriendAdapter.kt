package com.aboutcapsule.android.views.capsule

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.lifecycle.ProcessLifecycleOwner.get
import androidx.recyclerview.widget.RecyclerView
import com.aboutcapsule.android.databinding.FindFriendRecyclerItemBinding
import com.bumptech.glide.Glide

class FindFriendAdapter(val onClickDeleteIcon : (findFriendData: FindFriendData )-> Unit) : RecyclerView.Adapter<FindFriendAdapter.ViewHolder>() , Filterable{
    var itemList = mutableListOf<FindFriendData>()
    var filterList = mutableListOf<FindFriendData>()
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
        fun bind(findFriendData: FindFriendData) {
            Glide.with(itemView).load(findFriendData.Img).into(binding.friendImg)
            binding.friendName.text=findFriendData.name

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
                val filteringList = mutableListOf<FindFriendData>()

                for(item in itemList){
                    if(item.name.contains(str))
                        filteringList.add(item)
                }

                filterList = filteringList
            }

                val filterResult = FilterResults()
                filterResult.values = filterList

            return filterResult
        }

        override fun publishResults(c: CharSequence?, result: FilterResults?) {
                filterList = result?.values as MutableList<FindFriendData>
                notifyDataSetChanged()
        }

    }
}