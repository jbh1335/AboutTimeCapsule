package com.aboutcapsule.android.views.capsule

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import com.aboutcapsule.android.R
import com.aboutcapsule.android.databinding.FragmentCustomDialogMemberListBinding

class CustomDialogMemberList : DialogFragment() {

    companion object{
      private var binding : FragmentCustomDialogMemberListBinding ? = null
      lateinit var dialogAdapter: DialogAdapter
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentCustomDialogMemberListBinding.inflate(inflater,container,false)

        setDialog()
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setmemberListRecycler()

        getmemberListsdatas()
    }

    // 다이얼로그 테두리 설정
    private fun setDialog(){

        //  테두리 둥글게 만들기 위한 설정
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)

        binding?.memberlistDialogCloseBtn!!.setOnClickListener {
            dismiss()
        }
    }

    // 멤버 리사이클러뷰 뷰
    private fun setmemberListRecycler(){
        val memberDataList = getmemberListsdatas()
        dialogAdapter = DialogAdapter()

        dialogAdapter.itemList = memberDataList
        binding?.memberListDialogRecyclerView?.adapter = dialogAdapter
    }


    // 멤버 리사이클러뷰 아이템
    private fun getmemberListsdatas() : MutableList<DialogData>{
        var itemList = mutableListOf<DialogData>()

        for(i in 1..9){
            var name = "user ${i}"
            var img = R.drawable.heartimg
            val tmp = DialogData(img,name)
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