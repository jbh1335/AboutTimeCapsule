package com.aboutcapsule.android.views.mypage

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.aboutcapsule.android.R
import com.aboutcapsule.android.data.mypage.AllFriendRes
import com.aboutcapsule.android.databinding.FragmentMyAllFriendsBinding
import com.aboutcapsule.android.factory.MyPageViewModelFactory
import com.aboutcapsule.android.model.MyPageViewModel
import com.aboutcapsule.android.repository.mypage.MypageRepo

class MyAllFriendsFragment : Fragment() {

    lateinit var binding : FragmentMyAllFriendsBinding
    private lateinit var navController: NavController
    private lateinit var myAllFriendsAdapter: MyAllFriendsAdapter
    private lateinit var viewModel: MyPageViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_my_all_friends,container,false)
        return binding.root

        // recyclerview 안나온 원인 binding.root로 안함.
//        return inflater.inflate(R.layout.fragment_my_all_friends, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setNavigation()

        getDataFromPreFragment()



    }
    fun getDataFromPreFragment() {
        val friendId = requireArguments().getInt("memberId")
        val repository = MypageRepo()
        val myPageViewModelFactory = MyPageViewModelFactory(repository)
        viewModel = ViewModelProvider  (this, myPageViewModelFactory).get(MyPageViewModel::class.java)
        viewModel.getMyAllFriendList(friendId)
        viewModel.allFriendList.observe(viewLifecycleOwner, {
            getMyAllFriendData(it)

            moveToFriendProfileFromAllFriendList()
        })
    }
    fun getMyAllFriendData(allFriendResList: MutableList<AllFriendRes>) {
        myAllFriendsAdapter = MyAllFriendsAdapter()
        myAllFriendsAdapter.itemList = allFriendResList
        binding.myAllFriendsRecyclerView.adapter= myAllFriendsAdapter
        binding.myAllFriendsRecyclerView.layoutManager=
            LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
    }
    fun moveToFriendProfileFromAllFriendList() {
        myAllFriendsAdapter.setToFriendPageClickListener(object : MyAllFriendsAdapter.OnItemClickListener {

            override fun onItemClick(view: View, position: Int) {
                val friendId = viewModel.allFriendList.value?.get(position)?.friendMemberId
                Log.d("프래그먼트그거", "${friendId}")
                var bundle = bundleOf("friendId" to  friendId)
                navController.navigate(R.id.action_myAllFriendsFragment_to_myPageMainFragment, bundle)
            }
        })
    }

    // 네비게이션 세팅
    private fun setNavigation(){
        val navHostFragment =requireActivity().supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
    }
//    private fun redirectArlamPage(){
//        // 상단 툴바 알림페이지로 리다이렉트
//        val notiBtn = activity?.findViewById<ImageView>(R.id.toolbar_bell)
//        notiBtn?.setOnClickListener{
//            navController.navigate(R.id.action_myAllFriend)
//        }
//    }



//    private fun getMyAllFriendsDatas() : MutableList<MyAllFriendsData>{
//        var itemList = mutableListOf<MyAllFriendsData>()
//
//        for( i in 1..10){
//            var userprofile = R.drawable.heartimg
//            var text = " 유 저 명 ${i}"
//            var tmp = MyAllFriendsData(userprofile,text)
//            itemList.add(tmp)
//        }
//        return itemList
//    }
}