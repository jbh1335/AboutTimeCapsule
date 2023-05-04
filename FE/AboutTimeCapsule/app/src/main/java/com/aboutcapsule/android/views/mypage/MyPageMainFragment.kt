package com.aboutcapsule.android.views.mypage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.aboutcapsule.android.R
import com.aboutcapsule.android.databinding.FragmentMyPageMainBinding


class MyPageMainFragment : Fragment() {

    private lateinit var binding: FragmentMyPageMainBinding


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getMyPageFriendRequestList()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_page_main, container, false)
        return binding.root
    }

    // 유저 프로필정보 가져오기
    fun getMyPageProfileInfo() {

    }


    // 친구목록 썸네일들 띄워주기
    fun getMyPageFriendList() {

    }

    // 친구요청목록리스트띄워주기
    fun getMyPageFriendRequestList() {
        val friendRequestData = getFriendRequestData()
        val friendRequestAdapter = MyPageFriendRequestAdapter()
        friendRequestAdapter.itemList = friendRequestData
        binding.myPageFriendRequestView.adapter = friendRequestAdapter
        binding.myPageFriendRequestView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }

    // retrofit으로 api요청을 통해 데이터 가져와야됨.
    fun getFriendRequestData() :MutableList<MyPageFriendRequestData>{

        var itemList = mutableListOf<MyPageFriendRequestData>()

        for (i in 1..5) {
            var username = "엄준식${i}"
            var img = R.drawable.kakao_login_symbol
            val tmp = MyPageFriendRequestData(img, username)
            itemList.add(tmp)
        }
        return itemList
    }








}