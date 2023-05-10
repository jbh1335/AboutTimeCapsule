package com.aboutcapsule.android.views.map

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker.PERMISSION_GRANTED
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
import com.aboutcapsule.android.views.MainActivity
import com.aboutcapsule.android.views.capsule.CapsuleRegistFragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapMainFragment : Fragment() ,OnMapReadyCallback{

    companion object{
        lateinit var mainActivity: MainActivity
        lateinit var binding : FragmentMapMainBinding
        lateinit var navController: NavController
        private var bellFlag : Boolean = true // 상단 벨 제거

        private var btnFlag : Boolean = true   // 캡슐 등록버튼 view 용 변수

        //클라이언트 변수 ( provider -> 배터리 소모 줄이고 정확도 높이게 도와줌 )
        private lateinit var fusedLocationClient :FusedLocationProviderClient
        // LocationCallBack -> 좌표값 가져오고 응답값을 받아서 처리
        private lateinit var locationCallback : LocationCallback
        // 구글맵 변수
        private lateinit var mMap:GoogleMap
        //권한
        private val permission = arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION
                                        ,android.Manifest.permission.ACCESS_COARSE_LOCATION)
        private val PERM_FLAG = 99
    }

    // context 가져오기 ( 액티비티 )
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_map_main,container,false)
        binding.mapFragment.onCreate(savedInstanceState)
        binding.mapFragment.getMapAsync(this)

        if(isPermitted()){
            startProcess()
        }else{
            // 권한요청
            ActivityCompat.requestPermissions(mainActivity, permission, PERM_FLAG)
        }

        // 상단 벨 숨기기
        bellToggle(bellFlag)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)

        setNavigation()

        setToolbar()

        redirectPage()

        // 캡슐 생성하기 버튼 클릭 시, view 띄워주기
        registBtnToggle()
    }

    // 상단바 벨 사라지게 / 페이지 전환 시 다시 생성
    private fun bellToggle(sign : Boolean){
        var bell = activity?.findViewById<ImageView>(R.id.toolbar_bell)
        if(sign) {
            bell?.visibility = View.GONE
            bellFlag=false
        }else{
            bell?.visibility = View.VISIBLE
            bellFlag=true
        }
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

    // 등록 버튼 클릭시 , view 토글
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

    // 권한 체크
    private fun isPermitted() : Boolean {
        for(perm in permission){
            if(ContextCompat.checkSelfPermission(mainActivity,perm) !=PERMISSION_GRANTED){
                return false
            }
        }
        return false
    }

    // 권한 체크 후 허용 상태면 로직 스타트
    private fun startProcess(){
        binding.mapFragment.getMapAsync(this)
    }

    // 지도 띄워주기
    // onCreateView에서 getMapAsync(this) 사용허가를 구하면 안드로이드가 메서드 실행
    override fun onMapReady(map: GoogleMap) {
        mMap=map
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(mainActivity)

        //좌표값 계속 갱신 주는 것을 등록 해주는 메서드
        setUpdateLocationListener()

        val point =LatLng(37.514644,126.979974)
        map.addMarker(MarkerOptions().position(point).title("현위치"))
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(point,12f))
    }
    @SuppressLint("MissingPermission")
    private fun setUpdateLocationListener(){
        val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY,1000).apply {
        }.build()

        locationCallback = object: LocationCallback(){
            override fun onLocationResult(locationResult: LocationResult) {
                //값이 있을때만 실행
                locationResult?.let{
                    for((i,location) in it.locations.withIndex()){
                        Log.d("MapMainFragment","setUpdateLocationListner()")
                        Log.d("MapMainFragment", "${i} ${location.latitude} ${location.longitude} ")
                        setLastLocation(location)

                    }
                }
            }
        }
        // 로케이션 요청함수 호출 ( locationRequest , locationCallback )
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback
        , Looper.myLooper())
    }

    private fun setLastLocation(location: Location){
        val myLocation = LatLng(location.latitude, location.longitude)
        val marker = MarkerOptions()
            .position(myLocation)
            .title("I'm here ! ")
        val cameraOption = CameraPosition.Builder()
            .target(myLocation)
            .zoom(15.0f)
            .build()
        val camera = CameraUpdateFactory.newCameraPosition(cameraOption)

        mMap.clear()
        mMap.addMarker(marker)
        mMap.moveCamera(camera)

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode){
            PERM_FLAG ->{
                var check = true
                for ( grant in grantResults){
                    if(grant != PERMISSION_GRANTED){
                        check=false
                        break;
                    }
                }
                if(check){
                    startProcess()
                }else{
                    Toast.makeText(mainActivity,"권한을 승인하여 앱을 사용해 보세요", Toast.LENGTH_SHORT).show()
                    mainActivity.finish()
                }
            }
        }
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

        // 상단 벨 다시 살리기
        bellToggle(bellFlag)

        super.onDestroy()
    }
}