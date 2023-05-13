package com.aboutcapsule.android.views.mypage

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.aboutcapsule.android.R
import com.aboutcapsule.android.data.mypage.FriendDtoList
import com.aboutcapsule.android.data.mypage.FriendRequestDtoList
import com.aboutcapsule.android.data.mypage.GetMyPageRes
import com.aboutcapsule.android.databinding.FragmentMyPageMainBinding
import com.aboutcapsule.android.factory.MyPageViewModelFactory
import com.aboutcapsule.android.model.MyPageViewModel
import com.aboutcapsule.android.repository.MypageRepo
import com.bumptech.glide.Glide


class MyPageMainFragment : Fragment() {

    private lateinit var binding: FragmentMyPageMainBinding
    private lateinit var navController: NavController
    private lateinit var myPageFriendRequestAdapter: MyPageFriendRequestAdapter
    private lateinit var myPageFriendThumbnailAdapter: MyPageFriendThumbnailAdapter
    private lateinit var viewModel: MyPageViewModel





    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getMyPageDataFromBack()

        // 네비게이션 세팅
        setNavigation()
        // 페이지 이동
        redirectPage()

        redirectAllFriendsPage()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_page_main, container, false)

        return binding.root
    }
    fun getMyPageDataFromBack() {

        val repository = MypageRepo()
        val myPageViewModelFactory = MyPageViewModelFactory(repository)
        viewModel = ViewModelProvider  (this, myPageViewModelFactory).get(MyPageViewModel::class.java)
        viewModel.getMyPage(viewModel.friendId)
        viewModel.myPageList.observe(viewLifecycleOwner, Observer {
            Log.i("데이터왔다.", "${it.friendDtoList}")

            // 상단 프로필 이미지 렌더링
            getMyPageProfileInfo(it)

            // 중단 친구목록 이미지 렌더링
            getMyPageFriendList(it.friendDtoList)

            //하단 친구 요청 이미지 렌더링
            getMyPageFriendRequestList(it.friendRequestDtoList)

            acceptFriendRequest()

            refuseFriendRequest()

            moveToFriendProfileFromFriendThumbnail()

            // 모든 친구 보기로 이동
            redirectAllFriendsPage()
        })

    }
    // 유저 프로필정보 가져오기
    fun getMyPageProfileInfo(getMyPageRes: GetMyPageRes) {
        binding.profileOptionBtn.visibility = View.GONE
        binding.friendRequestBtn.visibility = View.GONE
        binding.chattingBtn.visibility = View.GONE
        Glide.with(this).load(getMyPageRes.profileImageUrl).into(binding.myPageProfilePicture)
        binding.myPageUserName.text = getMyPageRes.nickname
        binding.myPageUserMail.text = getMyPageRes.email
    }

    // 친구 요청 보내기
    fun sendFriendRequest() {

    }


    // 친구목록 썸네일들 띄워주기
    fun getMyPageFriendList(friendDtoList: MutableList<FriendDtoList>) {
        binding.friendListLength.text = "친구 ${friendDtoList.size}명"
        myPageFriendThumbnailAdapter = MyPageFriendThumbnailAdapter()
        myPageFriendThumbnailAdapter.itemList = friendDtoList
        binding.friendListThumbnailItem.adapter = myPageFriendThumbnailAdapter
        val layout = GridLayoutManager(requireContext(), 3)
        layout.orientation = LinearLayoutManager.VERTICAL
        binding.friendListThumbnailItem.layoutManager = layout



    }

    fun redirectAllFriendsPage() {
        binding.redirectAllFriendPageBtn.setOnClickListener {

            navController.navigate(R.id.action_myPageMainFragment_to_myAllFriendsFragment)
        }
    }


    // 네비게이션 세팅
    private fun setNavigation(){
        val navHostFragment =requireActivity().supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
    }
    private fun redirectPage(){
        // 상단 툴바 알림페이지로 리다이렉트
        val notiBtn = activity?.findViewById<ImageView>(R.id.toolbar_bell)
        notiBtn?.setOnClickListener{
            navController.navigate(R.id.action_myPageMainFragment_to_notificationMainFragment)
        }
    }

    // 친구요청목록리스트띄워주기
    fun getMyPageFriendRequestList(friendRequestDtoList: MutableList<FriendRequestDtoList>) {
        binding.friendRequestLength.text = friendRequestDtoList.size.toString()
        myPageFriendRequestAdapter = MyPageFriendRequestAdapter()
        myPageFriendRequestAdapter.itemList = friendRequestDtoList
        binding.myPageFriendRequestView.adapter = myPageFriendRequestAdapter
        binding.myPageFriendRequestView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }

    fun moveToFriendProfileFromFriendThumbnail() {
        myPageFriendThumbnailAdapter.settoFriendProfileClickListener(object : MyPageFriendThumbnailAdapter.OnItemClickListener {
            override fun onItemClick(view: View, position: Int) {
                val friendId = viewModel.myPageList.value?.friendDtoList?.get(position)?.friendMemberId
                viewModel.friendId = friendId
                Log.d("프래그먼트그거", "${friendId}")
                viewModel.getMyPage(viewModel.friendId)
            }
        })
    }

    fun acceptFriendRequest() {
        myPageFriendRequestAdapter.setAcceptFriendRequestClickListener(object : MyPageFriendRequestAdapter.OnItemClickListener {

            override fun onItemClick(view: View, position: Int) {
                val friendRequestId = viewModel.myPageList.value?.friendRequestDtoList?.get(position)?.friendId
                val friendNickname = viewModel.myPageList.value?.friendRequestDtoList?.get(position)?.nickname
                viewModel.friendAcceptRequest(friendRequestId,1)
            }

        })
    }
    fun refuseFriendRequest() {
        myPageFriendRequestAdapter.setRefuseFriendRequestClickListener(object : MyPageFriendRequestAdapter.OnItemClickListener {
            override fun onItemClick(view: View, position: Int) {
                val friendRequestId = viewModel.myPageList.value?.friendRequestDtoList?.get(position)?.friendId
                val friendNickname = viewModel.myPageList.value?.friendRequestDtoList?.get(position)?.nickname
                viewModel.refuseFriendRequest(friendRequestId, 1)
            }
        })
    }


}