package com.aboutcapsule.android.views.notification

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.aboutcapsule.android.R
import com.aboutcapsule.android.databinding.FragmentNotificationMainBinding


class NotificationMainFragment : Fragment() {

    lateinit var binding : FragmentNotificationMainBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_notification_main,container,false)

        return binding.root

    }
}