package com.aboutcapsule.android.views.mainpage

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager2.widget.ViewPager2
import com.aboutcapsule.android.R
import com.aboutcapsule.android.databinding.FragmentMainPageMyCapsuleBinding
import com.aboutcapsule.android.factory.CapsuleViewModelFactory
import com.aboutcapsule.android.factory.MyPageViewModelFactory
import com.aboutcapsule.android.model.CapsuleViewModel
import com.aboutcapsule.android.model.MyPageViewModel
import com.aboutcapsule.android.repository.CapsuleRepo
import com.aboutcapsule.android.repository.MypageRepo
import com.aboutcapsule.android.util.GlobalAplication
import com.aboutcapsule.android.views.MainActivity
import com.aboutcapsule.android.views.capsule.CapsuleRegistFragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator


class MainPageMyCapsuleFragment : Fragment() {
    companion object{
        lateinit var binding : FragmentMainPageMyCapsuleBinding
        private lateinit var  viewPager : ViewPager2
        private lateinit var  tabLayout : TabLayout
        private lateinit var pagerAdapter : PagerFragmentStateAdapter

        private var lat : Double = 0.0
        private var lng : Double = 0.0

        private var memberId = GlobalAplication.preferences.getInt("currentUser",-1)
        private var userNickname = GlobalAplication.preferences.getString("currentUserNickname","null")
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_main_page_my_capsule,container,false)

        getData()

        viewPager = binding.viewPagerLayout
        tabLayout = binding.tabLayout

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        setViewPager()

        // 나의 캡슐 or 친구의 캡슐 분기정보 뷰페이저 프래그먼트에 전달
        setSendDataInfo()

        // 툴바 뒤로가기 버튼 세팅
        setToolbar()

    }

    fun getData(){
        lat = requireArguments().getDouble("lat")
        lng = requireArguments().getDouble("lng")
    }

    // TODO : 내캡슐 or 친구의 캡슐 api 불러오기 ( 분기처리 완료 )
    private fun setSendDataInfo(){

        var calledApiName = requireArguments().getString("apiName").toString()

        when(calledApiName){
            "myCapsuleApi" -> {
                binding.whosCapsule.text="나의 캡슐"
                sendDataToFragment("myCapsuleApi")
            }
            "friendApi" -> {
                binding.whosCapsule.text="친구의 캡슐"
                sendDataToFragment("friendApi")
            }
        }
    }

    // 자식한테 데이터 보내주기 위한 커스텀 인터페이스 정의 및 데이터 연결
    interface DataPassListner{
        fun onDataPass(data: String,lat : Double, lng : Double)
    }
    fun sendDataToFragment(data: String) {
        val capsuleFragment = pagerAdapter.fragments[0] as? CapsuleListFragment
        capsuleFragment?.onDataPass(data,lat,lng)
        val capsuleMapFragment = pagerAdapter.fragments[1] as? CapsuleMapFragment
        capsuleMapFragment?.onDataPass(data,lat,lng)
    }

    private fun setViewPager(){
        //       뷰페이저 ( 목록보기, 지도보기 )

        pagerAdapter = PagerFragmentStateAdapter(requireActivity())

        pagerAdapter.addFragment(CapsuleListFragment())
        pagerAdapter.addFragment(CapsuleMapFragment())

        viewPager.adapter = pagerAdapter

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
            }
        })

//        뷰페이저와 탭레이아웃 연동
        TabLayoutMediator(tabLayout,viewPager) { tab, position ->
            when(position){
                0 -> tab.text = "목록 보기"
                1 -> tab.text = "지도 보기"
            }
        }.attach()
    }

    private fun setToolbar() {
        // 액티비티에서 툴바 가져오기
        val toolbar = requireActivity().findViewById<Toolbar>(R.id.toolbar)

        // Navigation Component와 툴바 연결
        val navController = findNavController()
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        toolbar.setupWithNavController(navController, appBarConfiguration)

        // 프래그먼트 전환 이벤트 감지 및 툴바 업데이트
        navController.addOnDestinationChangedListener { _, destination, _ ->
            toolbar.title = ""
            toolbar.setNavigationOnClickListener {
                navController.navigateUp()
            }
        }
    }

}
