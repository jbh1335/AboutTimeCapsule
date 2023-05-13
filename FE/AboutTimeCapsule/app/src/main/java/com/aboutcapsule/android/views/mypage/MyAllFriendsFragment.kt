package com.aboutcapsule.android.views.mypage

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.aboutcapsule.android.R
import com.aboutcapsule.android.databinding.FragmentMyAllFriendsBinding

class MyAllFriendsFragment : Fragment() {

    lateinit var binding : FragmentMyAllFriendsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_my_all_friends,container,false)
        getDataFromPreFragment()
        return inflater.inflate(R.layout.fragment_my_all_friends, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val myAllFriendsDataList = getMyAllFriendsDatas()
        val myAllFriendsAdapter = MyAllFriendsAdapter()
        myAllFriendsAdapter.itemList = myAllFriendsDataList
        binding.myAllFriendsRecyclerView.adapter= myAllFriendsAdapter
        binding.myAllFriendsRecyclerView.layoutManager=
            LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
    }
    fun getDataFromPreFragment() {
        val friendId = requireArguments().getInt("memberId")
        Log.d("데이터왔니안왔니", "${friendId}")
    }
    private fun getMyAllFriendsDatas() : MutableList<MyAllFriendsData>{
        var itemList = mutableListOf<MyAllFriendsData>()

        for( i in 1..10){
            var userprofile = R.drawable.heartimg
            var text = " 유 저 명 ${i}"
            var tmp = MyAllFriendsData(userprofile,text)
            itemList.add(tmp)
        }
        return itemList
    }
}