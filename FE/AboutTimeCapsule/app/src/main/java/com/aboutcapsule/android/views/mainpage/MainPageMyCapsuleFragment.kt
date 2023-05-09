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
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.viewpager2.widget.ViewPager2
import com.aboutcapsule.android.R
import com.aboutcapsule.android.databinding.FragmentMainPageMyCapsuleBinding
import com.aboutcapsule.android.views.capsule.CapsuleRegistFragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator


class MainPageMyCapsuleFragment : Fragment() {
    companion object{
        lateinit var binding : FragmentMainPageMyCapsuleBinding
        private lateinit var  viewPager : ViewPager2
        private lateinit var  tabLayout : TabLayout
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_main_page_my_capsule,container,false)

        viewPager = binding.viewPagerLayout
        tabLayout = binding.tabLayout

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setViewPager()
        callingApi()

        // 툴바 뒤로가기 버튼 세팅
        setToolbar()

    }


    // TODO : 내캡슐 or 친구의 캡슐 api 불러오기 ( 분기처리 완료 )
    private fun callingApi(){

        var calledApi = requireArguments().getString("apiName").toString()

        when(calledApi){
            "myCapsuleApi" -> {
                Log.d("api", " 내 캡슐  ")
            }
            "friendApi" -> {
                Log.d("api", " 친구 캡슐  ")
            }
        }
    }


    private fun setViewPager(){
        //       뷰페이저 ( 목록보기, 지도보기 )
        val pagerAdapter = PagerFragmentStateAdapter(requireActivity())

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
