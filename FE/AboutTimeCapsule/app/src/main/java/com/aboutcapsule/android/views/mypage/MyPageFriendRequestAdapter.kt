package com.aboutcapsule.android.views.mypage

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aboutcapsule.android.databinding.FragmentMyPageMainBinding
import com.aboutcapsule.android.databinding.MainSection2RecyclerItemBinding


class MyPageFriendRequestAdapter() :
    RecyclerView.Adapter<MyPageFriendRequestAdapter.ViewHolder>() {

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = FragmentMyPageMainBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        TODO("Not yet implemented")
    }
    class ViewHolder(val binding: FragmentMyPageMainBinding) : RecyclerView.ViewHolder(binding.root) {

    }
}