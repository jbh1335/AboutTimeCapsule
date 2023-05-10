package com.aboutcapsule.android.views.capsule

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.aboutcapsule.android.R
import com.aboutcapsule.android.databinding.FragmentCapsuleMineBinding


class CapsuleMineFragment : Fragment() {

    companion object{
        lateinit var binding: FragmentCapsuleMineBinding
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_capsule_mine,container,false)



        return binding.root
    }

}