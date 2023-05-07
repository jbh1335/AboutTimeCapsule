package com.aboutcapsule.android.views.mainpage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.aboutcapsule.android.R
import com.aboutcapsule.android.databinding.FragmentCapsuleListBinding


class CapsuleListFragment : Fragment() {

    lateinit var binding : FragmentCapsuleListBinding
    lateinit var navController: NavController
    lateinit var section1Adapter : CapsuleListAdapter
    lateinit var section2Adapter : CapsuleListAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_capsule_list,container,false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setWaitingView()
        setMeetView()

        setNavigation()

        redirectPage()

        // 리다이렉트 체크용 나중에 제거하기
        binding.testbtn.setOnClickListener {
            navController.navigate(R.id.action_mainPageMyCapsuleFragment_to_capsuleRegistGroupFragment)
        }

    }

    // 000님을 기다리고 있어요 ( view )
    private fun setWaitingView(){
        val section1DataList = getSection1datas()
        section1Adapter = CapsuleListAdapter()

        section1Adapter.itemList = section1DataList
        binding.capsuleListSection1RecyclerView.adapter =section1Adapter
    }

    // 000님과 만났어요 ( view )
    private fun setMeetView(){
        val section2DataList = getSection2datas()
        section2Adapter = CapsuleListAdapter()

        section2Adapter.itemList = section2DataList
        binding.capsuleListSection2RecyclerView.adapter =section2Adapter
    }

    // 000님을 기다리고 있어요 (데이터)
    private fun getSection1datas(): MutableList<CapsuleListData>{
        var itemList = mutableListOf<CapsuleListData>()

        for(i in 1..9){
            var time = "2023.05.0${i}"
            var place = "장소 위치 ${i}"
            var img = R.drawable.redcapsule
            var isLock = false
            var lockimg =R.drawable.lockimg
            val tmp = CapsuleListData(img,time,place,isLock,lockimg)
            itemList.add(tmp)
        }

        return itemList
    }

    // 000 님과 만났어요 (데이터)
    private fun getSection2datas(): MutableList<CapsuleListData>{
        var itemList = mutableListOf<CapsuleListData>()

        for(i in 1..9){
            var time = "2023.05.1${i}"
            var place = "삼성 화재 유성캠퍼스${i}"
            var img = R.drawable.redcapsule
            var isLock = false
            var lockimg =R.drawable.lockimg
            val tmp = CapsuleListData(img,time,place,isLock,lockimg)
            itemList.add(tmp)
        }

        return itemList
    }

    // 네비게이션 세팅
    private fun setNavigation(){
        val navHostFragment =requireActivity().supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
    }

    private fun redirectPage(){

        // 상단 툴바 알림페이지로 리다이렉트
        val notiBtn = activity?.findViewById<ImageView>(R.id.toolbar_bell)
        notiBtn?.setOnClickListener{
            navController.navigate(R.id.action_mainPageMyCapsuleFragment_to_notificationMainFragment)
        }
    }
}