package com.aboutcapsule.android.views.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.aboutcapsule.android.R
import com.aboutcapsule.android.databinding.FragmentNicknameSettingBinding


class NicknameSettingFragment : Fragment() {
    lateinit var binding:FragmentNicknameSettingBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_nickname_setting, container, false)
        return binding.root
    }

}