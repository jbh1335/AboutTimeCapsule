package com.aboutcapsule.android.views.map

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import com.aboutcapsule.android.R
import com.aboutcapsule.android.databinding.FragmentCustomDialogCapsuleInfoBinding
import com.aboutcapsule.android.views.mainpage.CustomDialogMainpage


class CustomDialogCapsuleInfoFragment : DialogFragment() {

    private var binding : FragmentCustomDialogCapsuleInfoBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentCustomDialogCapsuleInfoBinding.inflate(inflater,container,false)

        setDialog()

        return binding?.root
    }


    // 다이얼로그 테두리 설정
    private fun setDialog(){

        //  테두리 둥글게 만들기 위한 설정
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)

        binding?.capsuleinfoDialogCloseBtn!!.setOnClickListener {
            dismiss()
        }
    }



    override fun onDestroy() {
        // 다이얼로그 없애기
        binding = null

        super.onDestroy()
    }

}