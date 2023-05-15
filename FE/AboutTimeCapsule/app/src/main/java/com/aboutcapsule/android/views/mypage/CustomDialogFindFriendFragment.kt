package com.aboutcapsule.android.views.mypage

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.aboutcapsule.android.R
import com.aboutcapsule.android.databinding.FragmentCustomDialogFindFriendBinding
import com.aboutcapsule.android.databinding.FragmentMypageSearchDialogBinding
import com.aboutcapsule.android.views.capsule.CustomDialogMemberList
import com.aboutcapsule.android.views.capsule.DialogAdapter
import com.aboutcapsule.android.views.capsule.DialogData

class CustomDialogFindFriendFragment : DialogFragment() {

    companion object{
        private var binding : FragmentMypageSearchDialogBinding? = null
        lateinit var dialogFindFriendAdapter: DialogFindFriendAdapter
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_mypage_search_dialog, container, false)

        setDialog()

        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setmemberListRecycler()

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
    private fun setmemberListRecycler(){
        val findFriendsDataList = getFindFriendsdatas()
        dialogFindFriendAdapter = DialogFindFriendAdapter()

        dialogFindFriendAdapter.itemList = findFriendsDataList
        // 어댑터 연결 시키고 리사이클러뷰 레이아웃, 방향 정해주기
        // 검색 리사이클러뷰
    }


    // 멤버 리사이클러뷰 아이템
    private fun getFindFriendsdatas() : MutableList<DialogFindFriendData>{
        var itemList = mutableListOf<DialogFindFriendData>()

        for(i in 1..9){
            var name = "user ${i}"
            var img = R.drawable.heartimg
            val tmp = DialogFindFriendData(img,name)
            itemList.add(tmp)
        }

        return itemList
    }




    // 다이얼로그 없애기
    override fun onDestroy() {
        binding = null
        super.onDestroy()
    }

}