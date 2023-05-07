package com.aboutcapsule.android.views.capsule

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.aboutcapsule.android.R
import com.aboutcapsule.android.databinding.FragmentCapsuleRegistBinding

class CapsuleRegistFragment : Fragment() {

    lateinit var binding : FragmentCapsuleRegistBinding

    companion object{
        var radioBtn :String= ""
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_capsule_regist,container,false)

        radioBtnListner()

        return binding.root


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

    


}