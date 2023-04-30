package com.aboutcapsule.android.views.mainpage

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleObserver
import androidx.viewpager2.widget.ViewPager2
import com.aboutcapsule.android.R
import com.aboutcapsule.android.databinding.FragmentMainPageMyCapsuleBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator


class MainPageMyCapsuleFragment : Fragment() {

    lateinit var binding : FragmentMainPageMyCapsuleBinding
    private lateinit var  viewPager : ViewPager2
    private lateinit var  tabLayout : TabLayout
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


}