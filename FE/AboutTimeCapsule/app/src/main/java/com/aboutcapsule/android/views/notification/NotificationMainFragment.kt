package com.aboutcapsule.android.views.notification

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val notificationData = getNotidatas()
        val notificationAdapter = NotificationAdapter()

        notificationAdapter.itemList = notificationData
        binding.notiftcationRecyclerView.adapter= notificationAdapter
        binding.notiftcationRecyclerView.layoutManager =
            LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)

    }

    private fun getNotidatas(): MutableList<NotificationData>{
        var itmeList = mutableListOf<NotificationData>()

        for(i in 1 .. 10 ){
            val img =R.drawable.redcapsule
            val text = "알림 내용 ${i}"
            val tmp = NotificationData(img,text)
            itmeList.add(tmp)
        }
        return itmeList
    }
}