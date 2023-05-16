package com.aboutcapsule.android.views.mypage

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.aboutcapsule.android.R
import com.aboutcapsule.android.data.mypage.SearchMemberRes
import com.aboutcapsule.android.databinding.FragmentCustomDialogFindFriendBinding
import com.aboutcapsule.android.databinding.FragmentMypageSearchDialogBinding
import com.aboutcapsule.android.factory.MyPageViewModelFactory
import com.aboutcapsule.android.model.MyPageViewModel
import com.aboutcapsule.android.repository.MypageRepo
import com.aboutcapsule.android.util.GlobalAplication
import com.aboutcapsule.android.views.capsule.CustomDialogMemberList
import com.aboutcapsule.android.views.capsule.DialogAdapter
import com.aboutcapsule.android.views.capsule.DialogData
import com.aboutcapsule.android.views.mainpage.CustomDialogMainpage

class CustomDialogFindFriendFragment : DialogFragment() {

    companion object{
        private var binding : FragmentMypageSearchDialogBinding? = null
        lateinit var dialogFindFriendAdapter: DialogFindFriendAdapter
        lateinit var myPageViewModel: MyPageViewModel
        private val currentUser = GlobalAplication.preferences.getInt("currentUser", -1)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMypageSearchDialogBinding.inflate(inflater, container, false)

        setDialog()

        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.searchFriendRecyclerView?.visibility = View.GONE
        getDataFromBack()

    }

    private fun getDataFromBack() {
        val repository = MypageRepo()
        val myPageViewModelFactory = MyPageViewModelFactory(repository)
        myPageViewModel = ViewModelProvider  (this, myPageViewModelFactory).get(MyPageViewModel::class.java)
        //TODO: clicklistner와 함께 묶어주기
        searchMemberListener()
        myPageViewModel.searchUserList.observe(viewLifecycleOwner) {
            if (it.size == 0) {
                Log.d("it이 null이야", "$it")
                binding?.searchFriendRecyclerView?.visibility = View.GONE
                binding?.searchViewResultText?.visibility = View.VISIBLE
                binding?.searchViewResultText?.text = "관련된 유저를 찾을 수 없습니다."
            } else {
                Log.d("it이 null이아니야", "$it")
                binding?.searchViewResultText?.visibility = View.GONE
                binding?.searchFriendRecyclerView?.visibility = View.VISIBLE
                setmemberListRecycler(it)

            }
            Log.d("it", "${it}")

        }
    }


    // 다이얼로그 테두리 설정
    private fun setDialog(){
        //  테두리 둥글게 만들기 위한 설정
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
//        binding?.findfriendsDialogCloseBtn!!.setOnClickListener {
//            dismiss()
//        }
    }

    // 멤버 리사이클러뷰 뷰
    private fun setmemberListRecycler(searchFriendList: MutableList<SearchMemberRes>){
        dialogFindFriendAdapter = DialogFindFriendAdapter()
        dialogFindFriendAdapter.itemList = searchFriendList
        binding?.searchFriendRecyclerView?.adapter = dialogFindFriendAdapter
        clickEventGroup(searchFriendList)

        // 검색 리사이클러뷰
    }
    private fun searchMemberListener() {
        binding!!.memberSearchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                Log.d("searchonQuery", "${query}")
                // TODO: submit이벤트 후 키보드 내려가게 하기
                myPageViewModel.findFriend(currentUser, query!!)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {

                return false
            }


        })
    }
    private fun clickEventGroup(searchFriendList: MutableList<SearchMemberRes>) {
        // TODO: 버튼 분기 처리별로 이벤트
        acceptFriendRequest()
        refuseFriendRequest()
        sendFriendRequest()

    }

    fun acceptFriendRequest() {
        dialogFindFriendAdapter.setAcceptFriendRequestClickListener(object : DialogFindFriendAdapter.OnItemClickListener {
            override fun onItemClick(view: View, position: Int) {
                val friendReqId = myPageViewModel.searchUserList.value?.get(position)?.friendId
                val friendmemberId = myPageViewModel.searchUserList.value?.get(position)?.memberId
                val friendNickname = myPageViewModel.searchUserList.value?.get(position)?.nickname
                myPageViewModel.friendAcceptRequest(currentUser,friendReqId!!)

            }
        })
    }
    fun refuseFriendRequest() {
        dialogFindFriendAdapter.setRefuseFriendRequestClickListener(object :
            DialogFindFriendAdapter.OnItemClickListener {
            override fun onItemClick(view: View, position: Int) {
                val friendReqId = myPageViewModel.searchUserList.value?.get(position)?.friendId
                val friendmemberId = myPageViewModel.searchUserList.value?.get(position)?.memberId
                val friendNickname = myPageViewModel.searchUserList.value?.get(position)?.nickname
                myPageViewModel.refuseFriendRequest(currentUser, friendReqId!!)
                myPageViewModel.findFriend(friendmemberId!!, friendNickname!!)
            }
        })
    }
    fun sendFriendRequest() {
        dialogFindFriendAdapter.setRefuseFriendRequestClickListener(object :
            DialogFindFriendAdapter.OnItemClickListener {
            override fun onItemClick(view: View, position: Int) {
                val friendmemberId = myPageViewModel.searchUserList.value?.get(position)?.memberId
                val friendNickname = myPageViewModel.searchUserList.value?.get(position)?.nickname
                myPageViewModel.sendFriendRequest(currentUser, friendmemberId!!)
                myPageViewModel.findFriend(friendmemberId!!, friendNickname!!)
            }
        })
    }
    fun moveToProfile() {
        dialogFindFriendAdapter.setMoveToProfileClickListener(object :DialogFindFriendAdapter.OnItemClickListener {
            override fun onItemClick(view: View, position: Int) {
                val friendMemberId = myPageViewModel.searchUserList.value?.get(position)?.memberId

            }
        })
    }

    // 다이얼로그 없애기
    override fun onDestroy() {
        binding = null
        super.onDestroy()
    }
}