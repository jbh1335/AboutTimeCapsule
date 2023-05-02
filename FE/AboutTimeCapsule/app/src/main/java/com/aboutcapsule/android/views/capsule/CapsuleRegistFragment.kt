package com.aboutcapsule.android.views.capsule

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.aboutcapsule.android.R
import com.aboutcapsule.android.databinding.FragmentCapsuleRegistBinding

class CapsuleRegistFragment : Fragment() {

    lateinit var binding : FragmentCapsuleRegistBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_capsule_regist,container,false)

        radiobtnListner()

        return binding.root
    }


    fun radiobtnListner(){
        binding.radiogruop3type.setOnCheckedChangeListener{ group , checkedId ->
            when(checkedId){
                R.id.radio_3type_all -> "api에 번호로 넘겨주려나 ? "
                R.id.radio_3type_friend -> " 확인해보기 "
                R.id.radio_3type_mine -> Log.d("라디오버튼", "클릭 체크 " )
            }
        }
    }

}