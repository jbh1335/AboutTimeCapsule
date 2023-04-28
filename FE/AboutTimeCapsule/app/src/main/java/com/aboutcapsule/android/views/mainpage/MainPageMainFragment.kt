package com.aboutcapsule.android.views.mainpage

import android.content.Intent
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

    lateinit var binding: FragmentMainPageMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {

        binding= FragmentMainPageMainBinding.inflate(inflater,container,false)

//      최초 렌더링시,  말풍선 비활성화
        binding.section2Banner.visibility=View.INVISIBLE

//        물음표 버튼 토글버틀
        bannerToggle()

        return binding.root
    }

//    리싸이클러뷰나 뷰페이저 ,,는  onViewCreated 에서 초기화 해주는 것이 좋다
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val section2DataList = getSection2datas()
        val section2adapter = Section2Adapter()
    //       어댑터에 api로 받아온 데이터 넘겨주기
        section2adapter.itemList = section2DataList
        binding.section2RecyclerView.adapter = section2adapter
        binding.section2RecyclerView.layoutManager=LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)

    //      TODO: 내 주변의 타임캡슐 요소 하나 클릭시 이동 필요한 데이터 가지고 넘어가기 ( 위,경도 시간,유저이름,거리,댓글수?  )
    //            val intent = Intent(this.context,이동할 장소 )
        section2adapter.setOnItemClickListner(object: Section2Adapter.OnItemClickListner{
            override fun onItemClick(view: View, position: Int) {
    //                    intent.putExtra("이동한 페이지에서 인식할 이름",데이터 )
    //                    페이지 이동
                val dialog = CustomDialogMainpage()
                dialog.show(parentFragmentManager,"customDialog")
            }
        })



        val section3DataList = getSection3datas()
        val section3adapter = Section3Adapter()
    //       section3 어댑터의 itemList라는 곳에 데이터 넘겨주기
            section3adapter.itemList = section3DataList
        binding.section3RecyclerView.adapter= section3adapter
        binding.section3RecyclerView.layoutManager=LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)

    }

//  물음표 버튼 토글 로직
    private fun bannerToggle() {
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

//   TODO: retrofit으로 내 주변의 타임캡슐 데이터 가져오기
    private fun getSection2datas() : MutableList<Section2Data>{
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

//    TODO: retrofit으로 내 주변의 인기장소 데이터 가져오기
    private fun getSection3datas() : MutableList<Section3Data>{
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