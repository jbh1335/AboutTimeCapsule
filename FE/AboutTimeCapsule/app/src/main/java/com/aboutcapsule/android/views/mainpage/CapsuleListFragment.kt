package com.aboutcapsule.android.views.mainpage

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.aboutcapsule.android.R
import com.aboutcapsule.android.databinding.FragmentCapsuleListBinding


class CapsuleListFragment : Fragment() {

    lateinit var binding : FragmentCapsuleListBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_capsule_list,container,false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val section1DataList = getSection1datas()
        val section1Adapter = CapsuleListWaitingAdapter()

        section1Adapter.itemList = section1DataList
        binding.capsuleListSection1RecyclerView.adapter =section1Adapter
        binding.capsuleListSection1RecyclerView.layoutManager =
            LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)

        val section2DataList = getSection2datas()
        val section2Adapter = CapsuleListWaitingAdapter()

        section2Adapter.itemList = section2DataList
        binding.capsuleListSection2RecyclerView.adapter =section2Adapter
        binding.capsuleListSection2RecyclerView.layoutManager =
            LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)

    }

    private fun getSection1datas(): MutableList<CapsuleListWaitingData>{
        var itemList = mutableListOf<CapsuleListWaitingData>()

        for(i in 1..9){
            var time = "2023.05.0${i}"
            var place = "장소 위치 ${i}"
            var img = R.drawable.redcapsule
            var isLock = false
            var lockimg =R.drawable.lockimg
            val tmp = CapsuleListWaitingData(img,time,place,isLock,lockimg)
            itemList.add(tmp)
        }

        return itemList
    }

    private fun getSection2datas(): MutableList<CapsuleListWaitingData>{
        var itemList = mutableListOf<CapsuleListWaitingData>()

        for(i in 1..9){
            var time = "2023.05.1${i}"
            var place = "삼성 화재 유성캠퍼스${i}"
            var img = R.drawable.redcapsule
            var isLock = false
            var lockimg =R.drawable.lockimg
            val tmp = CapsuleListWaitingData(img,time,place,isLock,lockimg)
            itemList.add(tmp)
        }

        return itemList
    }

}