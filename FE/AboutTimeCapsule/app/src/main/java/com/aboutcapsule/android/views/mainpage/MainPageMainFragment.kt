package com.aboutcapsule.android.views.mainpage

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.aboutcapsule.android.R
import com.aboutcapsule.android.databinding.FragmentMainPageMainBinding

class MainPageMainFragment : Fragment() {

    val binding by lazy { FragmentMainPageMainBinding.inflate(layoutInflater)}
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {

        val section2DataList = getSection2datas()
        val section2adapter = Section2Adapter()
//       어댑터에 api 받아온 데이터 넘겨주기
        section2adapter.itemList = section2DataList
        binding.section2RecyclerView.adapter = section2adapter
        binding.section2RecyclerView.layoutManager=LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)

        val section3DataList = getSection3datas()
        val section3adapter = Section3Adapter()
//       section3 어댑터에 itemList라는 곳에 데이터 넘겨주기
        section3adapter.itemList = section3DataList
        binding.section3RecyclerView.adapter= section3adapter
        binding.section3RecyclerView.layoutManager=LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)

//      최초 렌더링시,  말풍선 비활성화
        binding.section2Banner.visibility=View.INVISIBLE

        bannerToggle()

        return binding.root
    }

    fun bannerToggle() {
        val banner = binding.section2Banner
        val btn = binding.mainSection2Question
        btn.setOnClickListener{
            if(banner.visibility == View.INVISIBLE) {
                banner.visibility = View.VISIBLE
            }else{
                banner.visibility = View.INVISIBLE
            }
        }
    }

    fun getSection2datas() : MutableList<Section2Data>{
        var itemList = mutableListOf<Section2Data>()

        for(i in 1..10) {
            var username="유저${i}님의 캡슐"
            var userpos ="장소 ${i}"
            var img = R.drawable.redcapsule
            val tmp =Section2Data(img,username,userpos )
            itemList.add(tmp)
        }
        return itemList
    }

    fun getSection3datas() : MutableList<Section3Data>{
        var itemList = mutableListOf<Section3Data>()

        itemList.apply {
            add(Section3Data(R.drawable.sunglass,"투썸 플레이스1"))
            add(Section3Data(R.drawable.heartimg,"삼성 화재 유성연수원1"))
            add(Section3Data(R.drawable.sunglass,"투썸 플레이스2"))
            add(Section3Data(R.drawable.heartimg,"삼성 화재 유성연수원2"))
        }
        return itemList
    }


}