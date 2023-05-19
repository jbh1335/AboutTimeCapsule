package com.aboutcapsule.android.views.capsule

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.aboutcapsule.android.R
import com.aboutcapsule.android.data.capsule.GroupMemberDto
import com.aboutcapsule.android.databinding.FragmentCustomDialogMemberListBinding
import com.aboutcapsule.android.factory.CapsuleViewModelFactory
import com.aboutcapsule.android.model.CapsuleViewModel
import com.aboutcapsule.android.repository.CapsuleRepo
import com.aboutcapsule.android.util.GlobalAplication

class CustomDialogMemberList : DialogFragment() {

    companion object{
      private var binding : FragmentCustomDialogMemberListBinding ? = null
      lateinit var dialogAdapter: DialogAdapter

      lateinit var viewModel : CapsuleViewModel
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

        callingApi()
    }

    private fun callingApi(){
        var capsuleId = GlobalAplication.preferences.getInt("memberlist_capsuleId",0)

        val repository = CapsuleRepo()
        val capsuleViewModelFactory = CapsuleViewModelFactory(repository)
        viewModel = ViewModelProvider(this, capsuleViewModelFactory).get(CapsuleViewModel::class.java)
        viewModel.getGroupMemberList(capsuleId)
        viewModel.groupMemberList.observe(viewLifecycleOwner){
            setmemberListRecycler(it.groupMemberList)
        }
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
    private fun setmemberListRecycler(data : MutableList<GroupMemberDto>){
        dialogAdapter = DialogAdapter()

        dialogAdapter.itemList = data
        binding?.memberListDialogRecyclerView?.adapter = dialogAdapter
    }

    // 다이얼로그 없애기
    override fun onDestroy() {
        binding = null
        GlobalAplication.preferences.getEditor().remove("memberlist_capsuleId") // 가지고 다니면 무거울까봐 지원주기 
        super.onDestroy()
    }

}