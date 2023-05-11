package com.aboutcapsule.android.views.capsule

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.aboutcapsule.android.R
import com.aboutcapsule.android.databinding.FragmentCapsuleRegistBinding
import com.aboutcapsule.android.views.MainActivity
import com.aboutcapsule.android.views.mainpage.CapsuleMapFragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

// 캡슐 등록 버튼 눌러서 넘어오면서 좌표 가져와서 지도에 뿌려주기
class CapsuleRegistFragment : Fragment() ,OnMapReadyCallback {

     companion object{
        lateinit var binding : FragmentCapsuleRegistBinding
        lateinit var navController: NavController
        private lateinit var mMap : GoogleMap

        var radioBtn :String= ""
        var bottomNavFlag : Boolean = true
        private var bellFlag : Boolean = true
        var editTitle : String = ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 바텀 네비 숨기기
        bottomNavToggle()
        // 상단 벨 숨기기
        bellToggle(bellFlag)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_capsule_regist,container,false)

        binding.registAloneMapFragment.onCreate(savedInstanceState)
        binding.registAloneMapFragment.getMapAsync(this)

        // 전체, 친구공개, 나만보기 라디오 버튼
        radioBtnListner()

        // 캡슐 생성하기 버튼 클릭 시
        submitBtnClickListner()



        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setNavigation()
    }

    // 네비게이션 세팅
    private fun setNavigation(){
        val navHostFragment =requireActivity().supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
    }

    // TODO : (개인) 캡슐 생성버튼 클릭 시 , 캡슐생성 api 보내고 페이지 이동
    private fun submitBtnClickListner(){
        // editText 값이 destroy() 시에는 찍히는데 그전에 제출 버튼 클릭시에는 공백으로 찍히는 이유 찾기 ,,
        editTitle = binding.capsuleRegistTitle.text.toString().trim()
        binding.capsuleRegistRegistbtn.setOnClickListener{
//            if(editTitle.isEmpty() || editTitle.length<11){
//                Toast.makeText(requireContext(),"제목의 길이는 1~10글자로 작성 가능합니다.",Toast.LENGTH_SHORT).show()
//            }else{
//                Log.d("submitData", editTitle)
//                Log.d("submitData", radioBtn)
//            }
            navController.navigate(R.id.action_capsuleRegistFragment_to_articleRegistFragment)
        }
    }

    // 라디오 버튼
   private fun radioBtnListner(){
        binding.radiogruop3type.setOnCheckedChangeListener{ group , checkedId ->
            when(checkedId){
                R.id.radio_3type_all -> radioBtn = "ALL"
                R.id.radio_3type_friend -> radioBtn = "FREIND"
                R.id.radio_3type_mine -> radioBtn = "PRIVATE"
            }
        }
    }

    // 바텀 네비 숨기기 토글
    private fun bottomNavToggle(){
        val mainActivity = activity as MainActivity
        mainActivity.hideBottomNavi(bottomNavFlag)
        bottomNavFlag= !bottomNavFlag
    }

    // 상단바 벨 사라지게 / 페이지 전환 시 다시 생성
    private fun bellToggle(sign : Boolean){
        var bell = activity?.findViewById<ImageView>(R.id.toolbar_bell)
        if(sign) {
            bell?.visibility = View.GONE
            bellFlag = false
        }else{
            bell?.visibility = View.VISIBLE
            bellFlag = true
        }

    }

    // 지도 띄워주기
    // onCreateView에서 getMapAsync(this) 사용허가를 구하면 안드로이드가 메서드 실행
    override fun onMapReady(map: GoogleMap) {
        mMap = map

        val deajeonSS = LatLng(36.355038,127.298297)
        map.addMarker(MarkerOptions().position(deajeonSS).title("대전 캠퍼스 "))
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(deajeonSS,16f))
    }

    override fun onStart() {
        super.onStart()
        binding.registAloneMapFragment.onStart()
    }

    override fun onStop() {
        super.onStop()
        binding.registAloneMapFragment.onStop()
    }

    override fun onResume() {
        super.onResume()
        binding.registAloneMapFragment.onResume()
    }

    override fun onPause() {
        super.onPause()
        binding.registAloneMapFragment.onPause()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding.registAloneMapFragment.onLowMemory()
    }

    override fun onDestroy() {

        // 바텀 네비 다시 살리기
        bottomNavToggle()
        // 상단 벨 다시 살리기
        bellToggle(bellFlag)

        Log.d("editTitle",binding.capsuleRegistTitle.text.toString())

        binding.registAloneMapFragment.onDestroy()
        super.onDestroy()
    }
}