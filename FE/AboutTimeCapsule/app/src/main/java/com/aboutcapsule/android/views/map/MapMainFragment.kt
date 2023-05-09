package com.aboutcapsule.android.views.map

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import androidx.appcompat.widget.Toolbar
import androidx.core.graphics.createBitmap
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.aboutcapsule.android.R
import com.aboutcapsule.android.databinding.FragmentMainPageMainBinding
import com.aboutcapsule.android.databinding.FragmentMapMainBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapMainFragment : Fragment() ,OnMapReadyCallback{

    companion object{
        lateinit var binding : FragmentMapMainBinding
        lateinit var navController: NavController
        var btnFlag : Boolean = true
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_map_main,container,false)
        binding.mapFragment.onCreate(savedInstanceState)
        binding.mapFragment.getMapAsync(this)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)

        setNavigation()

        setToolbar()

        redirectPage()

        // 캡슐 생성하기 버튼 클릭 시 view 띄워주기
        registBtnToggle()
    }


    fun redirectPage(){
        // 알림 페이지로 이동
        val notiBtn = activity?.findViewById<ImageView>(R.id.toolbar_bell)
        notiBtn?.setOnClickListener{
            navController.navigate(R.id.action_mapMainFragment_to_notificationMainFragment)
        }

        // 개인 캡슐 클릭 시
        binding.capsuleRegistAloneBtn.setOnClickListener {
            navController.navigate(R.id.action_mapMainFragment_to_capsuleRegistFragment)
        }

        // 그룹 캡슐 클릭 시
        binding.capsuleRegistGroupBtn.setOnClickListener{
            navController.navigate(R.id.action_mapMainFragment_to_capsuleRegistGroupFragment)
        }


    }

    // 네비게이션 세팅
    private fun setNavigation(){
        val navHostFragment =requireActivity().supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
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

    private fun registBtnToggle(){
        binding.capsuleRegistBtn.setOnClickListener{
            if(btnFlag){
                binding.capsuleRegistBtn.text="닫기"
                binding.capsuleRegistGroupBtn.visibility=View.VISIBLE
                binding.capsuleRegistAloneBtn.visibility=View.VISIBLE
                btnFlag=false
            }else{
                binding.capsuleRegistBtn.text="캡슐 생성하기"
                binding.capsuleRegistGroupBtn.visibility=View.GONE
                binding.capsuleRegistAloneBtn.visibility=View.GONE
                btnFlag=true
            }
        }
    }

    // 지도 띄워주기
    override fun onMapReady(map: GoogleMap) {
        val point =LatLng(37.514644,126.979974)
        map.addMarker(MarkerOptions().position(point).title("현위치"))
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(point,12f))
    }
    override fun onStart() {
        super.onStart()
        binding.mapFragment.onStart()
    }

    override fun onStop() {
        super.onStop()
        binding.mapFragment.onStop()
    }

    override fun onResume() {
        super.onResume()
        binding.mapFragment.onResume()
    }

    override fun onPause() {
        super.onPause()
        binding.mapFragment.onPause()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding.mapFragment.onLowMemory()
    }

    override fun onDestroy() {
        binding.mapFragment.onDestroy()

        btnFlag=true
        super.onDestroy()
    }
}