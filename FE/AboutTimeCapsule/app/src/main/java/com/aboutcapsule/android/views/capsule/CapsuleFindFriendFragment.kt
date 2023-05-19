package com.aboutcapsule.android.views.capsule

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.aboutcapsule.android.R
import com.aboutcapsule.android.data.capsule.FriendDto
import com.aboutcapsule.android.databinding.FragmentCapsuleFindFriendBinding
import com.aboutcapsule.android.factory.CapsuleViewModelFactory
import com.aboutcapsule.android.model.CapsuleViewModel
import com.aboutcapsule.android.repository.CapsuleRepo
import com.aboutcapsule.android.views.MainActivity
import com.aboutcapsule.android.views.mainpage.CapsuleMapFragment
import com.bumptech.glide.Glide


class CapsuleFindFriendFragment : Fragment() , TextWatcher {

    companion object {
        lateinit var binding: FragmentCapsuleFindFriendBinding
        private lateinit var findFriendAdapter : FindFriendAdapter
        lateinit var navController: NavController

        lateinit var sendMemberId : ArrayList<Int>   // 멤버로 추가된 유저 id
        lateinit var sendMemberName : ArrayList<String>// 멤버로 추가된 유저 nickname

        private var friendList = mutableListOf<FriendDto>()

        lateinit var viewModel : CapsuleViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_capsule_find_friend,container,false)

        // 바텀 네비 숨기기
        bottomNavToggle(true)

        recyclerToggle()

        setNavigation()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setNavigation()

        redirectPage()

        callingApi()

    }

    // api 호출
    fun callingApi(){
        sendMemberId = ArrayList<Int>()
        sendMemberName = ArrayList<String>()

        val repository = CapsuleRepo()
        val capsuleViewModelFactory = CapsuleViewModelFactory(repository)
        viewModel = ViewModelProvider  (this, capsuleViewModelFactory).get(CapsuleViewModel::class.java)

        viewModel.getMyFriendList(1) // 멤버 id 넣어주기 ( 내 아이디 )
        viewModel.friendList.observe(viewLifecycleOwner){
            friendList = it.friendList

            // 리사이클러뷰 세팅
            setFindFriendRecylcer()
        }
    }

    private fun redirectPage(){
        // 추가된 멤버 번들에 담아서 넘어가서 뿌려주기
        binding.completeBtn.setOnClickListener{
            val bundle = Bundle() // 번들로 객체 넘겨주기
            bundle.putStringArrayList("memberNameList", sendMemberName)
            bundle.putIntegerArrayList("memberIdList", sendMemberId)
            navController.navigate(R.id.action_capsuleFindFriendFragment_to_capsuleRegistGroupFragment,bundle)
        }
    }

    // 네비게이션 세팅
    private fun setNavigation(){
        val navHostFragment =requireActivity().supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
    }

    // 리사이클러 뷰 버튼 클릭 시 , 토글
    private fun recyclerToggle(){
        // editText 클릭시 보여주기
        binding.searchEditText.setOnClickListener{
                binding.findFriendsRecyclerView.visibility=View.VISIBLE
        }
        // 다른 부분 클릭 시 리사이클러뷰 숨기기
        binding.searchEditText.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                binding.findFriendsRecyclerView.visibility=View.VISIBLE
            }else {
                binding.findFriendsRecyclerView.visibility=View.GONE
            }
        }
    }

    // 리사이클러뷰 세팅
    fun setFindFriendRecylcer(){
        binding.searchEditText.addTextChangedListener(this)

        findFriendAdapter = FindFriendAdapter(onClickDeleteIcon = { deleteTask(it)} )
        findFriendAdapter.itemList = friendList
        findFriendAdapter.filterList = friendList // 필터용
        binding.findFriendsRecyclerView.adapter=findFriendAdapter
    }

    // 리사이클러뷰에서 하나 지우고 아래 멤버로 추가 되기
    fun deleteTask(friendDto: FriendDto){

        createView(friendDto)
        friendList.remove(friendDto)
        binding.findFriendsRecyclerView.adapter?.notifyDataSetChanged()
        binding.findFriendsRecyclerView.visibility=View.GONE
        Log.d("추가멤버","${friendDto}")
    }

    // 후순위로 구현하기
//    // 제거 버튼 누르면 검색 리사이클러뷰에 다시 추가
//    fun addTask(friendDto: FriendDto){
//
//        createView(friendDto)
//        friendList.remove(friendDto)
//        binding.findFriendsRecyclerView.adapter?.notifyDataSetChanged()
//        binding.findFriendsRecyclerView.visibility=View.GONE
//        Log.d("추가멤버","${friendDto}")
//    }

    //멤버 추가 동적으로 뷰 생성
    private fun createView(friendDto: FriendDto){
        val customView = LayoutInflater.from(requireContext()).inflate(R.layout.added_member_list,null,false)
        val param: LinearLayout.LayoutParams =
            LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT)
        param.bottomMargin = 30
        customView.layoutParams=param
        sendMemberId.add(friendDto.memberId) // 넘겨줄 멤버 id들
        sendMemberName.add(friendDto.nickname) // 넘겨줄 멤버 이름
        Glide.with(customView).load(friendDto.profileImageUrl).into(customView.findViewById<ImageView>(R.id.added_member_img))
        customView.findViewById<TextView>(R.id.added_member_name).text=friendDto.nickname
        binding.addedMemberListView.addView(customView)
        customView.findViewById<Button>(R.id.added_member_removeBtn).setOnClickListener{// 제거버튼 클릭 로직
            binding.addedMemberListView.removeView(customView) // 뷰 제거
            sendMemberId.remove(friendDto.memberId) // 보내줄 데이터에서 제거
            sendMemberName.remove(friendDto.nickname) // 보내줄 데이터에서 제거
        }
    }

//    edittext watcher
    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }
    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        findFriendAdapter.getFilter().filter(p0.toString())
    }
    override fun afterTextChanged(p0: Editable?) {
    }
// ---------

    // 바텀 네비 숨기기 토글
    private fun bottomNavToggle(sign : Boolean){
        val mainActivity = activity as MainActivity
        mainActivity.hideBottomNavi(sign)
    }

    override fun onDestroy() {
        super.onDestroy()
    }

}