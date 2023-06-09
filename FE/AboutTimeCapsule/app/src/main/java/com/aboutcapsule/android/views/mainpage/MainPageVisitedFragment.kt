package com.aboutcapsule.android.views.mainpage

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.viewpager2.widget.ViewPager2
import com.aboutcapsule.android.R
import com.aboutcapsule.android.databinding.FragmentMainPageVisitedBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class MainPageVisitedFragment : Fragment() {

    lateinit var binding : FragmentMainPageVisitedBinding
    lateinit var navController: NavController
    private lateinit var  viewPager : ViewPager2
    private lateinit var  tabLayout : TabLayout
    private lateinit var pagerAdapter : PagerFragmentStateAdapter

    private var lat : Double = 0.0 //좌 표
    private var lng : Double = 0.0 //좌 표


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_main_page_visited,container,false)
        viewPager = binding.viewPagerLayout
        tabLayout = binding.tabLayout

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setViewPager()

        setNavigation()

        setToolbar()

        setData()

        sendDataToFragment()


        redirectPage()
    }


    // 자식한테 데이터 보내주기 위한 커스텀 인터페이스 정의 및 데이터 연결
    interface DataPassListner{
        fun onDataPass(lat : Double, lng : Double)
    }

    fun setData(){
        lat = requireArguments().getDouble("lat")
        lng = requireArguments().getDouble("lng")
    }

    // 네비게이션 세팅
    private fun setNavigation(){
        val navHostFragment =requireActivity().supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
    }

    fun sendDataToFragment() {
        val mainPageVisitedListFragment =pagerAdapter.fragments[0] as? MainPageVistiedCapsuleListFragment
        mainPageVisitedListFragment?.onDataPass(lat, lng)
        val mainPageVisitedMapFragment = pagerAdapter.fragments[1] as? MainPageVisitedCapsuleMapFragment
        mainPageVisitedMapFragment?.onDataPass(lat, lng)
    }
    // 뷰페이저 세팅
    private fun setViewPager(){
        //       뷰페이저 ( 목록보기, 지도보기 )
        pagerAdapter = PagerFragmentStateAdapter(requireActivity())

        pagerAdapter.addFragment(MainPageVistiedCapsuleListFragment())
        pagerAdapter.addFragment(MainPageVisitedCapsuleMapFragment())

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

    private fun redirectPage(){

        // 상단 툴바 알림페이지로 리다이렉트
        val notiBtn = activity?.findViewById<ImageView>(R.id.toolbar_bell)
        notiBtn?.setOnClickListener{
            navController.navigate(R.id.action_mainPageVisitedFragment_to_notificationMainFragment)
        }
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