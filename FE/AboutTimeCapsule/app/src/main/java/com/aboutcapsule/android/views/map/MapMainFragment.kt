package com.aboutcapsule.android.views.map

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.OnRequestPermissionsResultCallback
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.aboutcapsule.android.R
import com.aboutcapsule.android.databinding.FragmentMapMainBinding
import com.aboutcapsule.android.views.MainActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener
import com.google.android.gms.maps.GoogleMap.OnMyLocationClickListener
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng

class MapMainFragment : Fragment() ,OnMapReadyCallback ,OnMyLocationButtonClickListener ,OnMyLocationClickListener ,OnRequestPermissionsResultCallback{

    companion object{
        lateinit var binding : FragmentMapMainBinding
        lateinit var mainActivity: MainActivity
        lateinit var navController: NavController
        private var bellFlag : Boolean = true // 상단 벨 제거
        private var btnFlag : Boolean = true   // 캡슐 등록버튼 view 용 변수

        // ------------ 지도 ----------------
        //클라이언트(사용자위치) 변수 ( provider -> 배터리 소모 줄이고 정확도 높이게 도와줌 )
        private lateinit var fusedLocationClient :FusedLocationProviderClient
        // 기본 위치
        private val defaultLocation = LatLng(37.514644,126.979974) // 대전캠퍼스
        // 권한 체크용 boolean 변수
        private var locationPermissionGranted = false
        // 카메라 위치
        private lateinit var cameraPosition : CameraPosition
        // 구글맵 변수
        private lateinit var mMap:GoogleMap
        // 사용자의 마지막 위치 가져오기
        private var lastKnownLocation: Location? = null
        private val TAG = MapMainFragment::class.java.simpleName

        private const val DEFAULT_ZOOM = 15
        private const val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1 // 위치 권한 체크용
        private var permissionDenied = false

        private const val KEY_CAMERA_POSITION = "카메라 위치"
        private const val KEY_LOCATION = "위치정보"
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

        // 마지막 위치 저장
        if(savedInstanceState != null ){
            lastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION)
            cameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION)!!
        }

       // 지도
        binding.mapFragment.onCreate(savedInstanceState)
        binding.mapFragment.getMapAsync(this)

        // 상단 벨 숨기기
        bellToggle(bellFlag)

        return binding.root
    }

    // 활동이 일시 중지되었을 경우 저장해놓은 정보를 가져오기 위해 저장
//    override fun onSaveInstanceState(outState: Bundle) {
//        mMap?.let{
//            outState.putParcelable(KEY_CAMERA_POSITION,mMap.cameraPosition)
//            outState.putParcelable(KEY_LOCATION, lastKnownLocation)
//        }
//        super.onSaveInstanceState(outState)
//    }

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

    // 페이지 이동
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

        // AR 카메라 클릭 시
        binding.cameraBtn.setOnClickListener{
            navController.navigate(R.id.action_mapMainFragment_to_arFragment)
        }
    }

    // 네비게이션 세팅
    private fun setNavigation(){
        val navHostFragment =requireActivity().supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
    }

    // 액티비티에서 툴바 가져오기
    private fun setToolbar() {
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


    // 지도 띄워주기
    // onCreateView에서 getMapAsync(this) 사용허가를 구하면 안드로이드가 메서드 실행
    @SuppressLint("MissingPermission")
    override fun onMapReady(map: GoogleMap) {
        // 사용자 권한 얻기
        getLocationPermission()

        // 사용자 위치
        mMap = map
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(mainActivity)


        // 지도 UI 업데이트
        updateLocationUI()

        // 사용자의 위치,카메라 가져오기
        getDeviceLocation()

        val latLng = LatLng(37.566168, 126.901609)
//        mMap.addMarker(MarkerOptions().position(latLng).title("여기"))
//        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
//        mMap.isMyLocationEnabled = true
        map.setOnMyLocationButtonClickListener(this)
        map.setOnMyLocationClickListener(this)
        enableMyLocation()
    }


    // 내 위치 권한 체크
    @SuppressLint("MissingPermission")
    private fun enableMyLocation(){
        if (ContextCompat.checkSelfPermission(
                mainActivity,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                mainActivity,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            mMap.isMyLocationEnabled = true
            return
        }

        // 2. If if a permission rationale dialog should be shown
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                mainActivity,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) || ActivityCompat.shouldShowRequestPermissionRationale(
                mainActivity,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        ) {
           Toast.makeText(mainActivity,"권한 허용이 필요합니다",Toast.LENGTH_SHORT).show()
            return
        }

        // 3. Otherwise, request permission
        ActivityCompat.requestPermissions(
            mainActivity,
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ),
            LOCATION_PERMISSION_REQUEST_CODE
        )
    }

    // 사용자 위치 정보 권한 체크
    private fun getLocationPermission() {

        if (ContextCompat.checkSelfPermission(mainActivity,
                Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted = true
        } else {
            ActivityCompat.requestPermissions(
                mainActivity, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION)
        }
    }

    // 사용자 위치 권한 콜백
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        locationPermissionGranted = false
        when (requestCode) {
            PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION -> {

                if (grantResults.isNotEmpty() &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    locationPermissionGranted = true
                }
            }
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
        updateLocationUI()
    }

    @SuppressLint("MissingPermission")
    private fun updateLocationUI() {
        if (mMap == null) {
            return
        }
        try {
            if (locationPermissionGranted) {
                mMap.isMyLocationEnabled = true
                mMap.uiSettings.isMyLocationButtonEnabled = true
            } else {
                mMap.isMyLocationEnabled = false
                mMap.uiSettings.isMyLocationButtonEnabled = false
                lastKnownLocation = null
                getLocationPermission()
            }
        } catch (e: SecurityException) {
            Log.e("Exception: %s", e.message, e)
        }
    }

    // 사용자 위치 ,카메라 정보 얻기
    @SuppressLint("MissingPermission")
    private fun getDeviceLocation() {
        // 사용자가 접근 할 수 없을 때도 마지막 위치 가져오기
        try{
            if(locationPermissionGranted){
                val locationResult = fusedLocationClient.lastLocation
                locationResult.addOnCompleteListener(mainActivity) { task ->
                    if(task.isSuccessful){ // 위치 접근 성공
                        lastKnownLocation = task.result
                        if(lastKnownLocation != null){
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                LatLng(lastKnownLocation!!.latitude,
                                lastKnownLocation!!.longitude), DEFAULT_ZOOM.toFloat()))
                        }
                    } else {
                        Log.d(TAG, "접근이 어려워 기본 지정 위치로 출력 ")
                        Log.e(TAG, "Exception: %s", task.exception)
                        mMap.moveCamera(CameraUpdateFactory
                            .newLatLngZoom(defaultLocation, DEFAULT_ZOOM.toFloat()))
                        mMap.uiSettings?.isMyLocationButtonEnabled = false
                    }
                }
            }
        }catch (e: SecurityException) { // 에러 발생
            Log.e("Exception: %s", e.message, e)
        }
    }

    // 버튼 클릭 시 내 현재 위치로 이동
    override fun onMyLocationButtonClick(): Boolean {
        Toast.makeText(mainActivity, "onMyLocationButtonClick/curr location", Toast.LENGTH_SHORT)
            .show()
        return false
    }
    // 현재 내 위치 표시 ( 파란 점 )
    override fun onMyLocationClick(p0: Location) {
        Toast.makeText(mainActivity,"onMyLocationClick/curr location",Toast.LENGTH_SHORT).show()
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