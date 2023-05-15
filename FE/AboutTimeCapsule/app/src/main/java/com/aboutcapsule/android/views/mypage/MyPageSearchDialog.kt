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
import com.aboutcapsule.android.databinding.FragmentMypageSearchDialogBinding


class MyPageSearchDialog : DialogFragment() {

    companion object {
        lateinit var binding : FragmentMypageSearchDialogBinding
        lateinit var dialogFindFriendAdapter: DialogFindFriendAdapter
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        setDialog()
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_mypage_search_dialog, container, false)
        return binding.root
    }

    private fun setDialog(){

        //  테두리 둥글게 만들기 위한 설정
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)

        binding?.findfriendsDialogCloseBtn!!.setOnClickListener {
            dismiss()
        }
    }

    private fun setmemberListRecycler(){
        val findFriendsDataList = getFindFriendsdatas()
        CustomDialogFindFriendFragment.dialogFindFriendAdapter = DialogFindFriendAdapter()

        CustomDialogFindFriendFragment.dialogFindFriendAdapter.itemList = findFriendsDataList
        // 어댑터 연결 시키고 리사이클러뷰 레이아웃, 방향 정해주기
        // 검색 리사이클러뷰
    }

    override fun onDestroy() {
        binding
        super.onDestroy()
    }


}