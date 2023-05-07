package com.aboutcapsule.android.views.capsule

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.aboutcapsule.android.R
import com.aboutcapsule.android.databinding.FragmentCapsuleRegistBinding
import com.aboutcapsule.android.views.MainActivity

class CapsuleRegistFragment : Fragment() {

    lateinit var binding : FragmentCapsuleRegistBinding
     companion object{
        var radioBtn :String= ""
        var bottomNavFlag : Boolean = false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 바텀 네비 숨기기
        bottomNavToggle()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_capsule_regist,container,false)

        // 전체, 친구공개, 나만보기 라디오 버튼
        radioBtnListner()



        return binding.root
    }
    override fun onDestroy() {
        // 바텀 네비 다시 살리기
        bottomNavToggle()

        super.onDestroy()
    }

    // 라디오 버튼
   private fun radioBtnListner(){
        binding.radiogruop3type.setOnCheckedChangeListener{ group , checkedId ->
            when(checkedId){
                R.id.radio_3type_all -> radioBtn = "ALL"
                R.id.radio_3type_friend -> radioBtn = "FREIND"
                R.id.radio_3type_mine -> radioBtn = "PRIVATE"
            }
        }
    }

    // 바텀 네비 숨기기 토글
    private fun bottomNavToggle(){
        val mainActivity = activity as MainActivity
        mainActivity.hideBottomNavi(!bottomNavFlag)
    }

}