package com.aboutcapsule.android.views.map

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.os.Looper
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
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.aboutcapsule.android.R
import com.aboutcapsule.android.data.capsule.MapAroundCapsuleReq
import com.aboutcapsule.android.data.capsule.MapAroundCapsuleRes
import com.aboutcapsule.android.databinding.FragmentMapMainBinding
import com.aboutcapsule.android.factory.CapsuleViewModelFactory
import com.aboutcapsule.android.model.CapsuleViewModel
import com.aboutcapsule.android.repository.CapsuleRepo
import com.aboutcapsule.android.util.GlobalAplication
import com.aboutcapsule.android.views.MainActivity
import com.aboutcapsule.android.views.capsule.CapsuleRegistGroupFragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener
import com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener
import com.google.android.gms.maps.GoogleMap.OnMyLocationClickListener
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.Circle
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.net.PlacesClient
import java.util.Locale

class MapMainFragment : Fragment() ,OnMapReadyCallback ,OnMyLocationButtonClickListener ,OnMyLocationClickListener ,OnRequestPermissionsResultCallback , OnMarkerClickListener{

    companion object{
        lateinit var binding : FragmentMapMainBinding
        lateinit var mainActivity: MainActivity
        lateinit var navController: NavController
        private var btnFlag : Boolean = true   // 캡슐 등록버튼 view 용 변수

        // ------ viewModel -----
        private lateinit var viewModel : CapsuleViewModel

        // ------------ 지도 ---------------
        //클라이언트(사용자위치) 변수 ( provider -> 배터리 소모 줄이고 정확도 높이게 도와줌 )
        private lateinit var fusedLocationClient :FusedLocationProviderClient
        // 기본 위치
        private val defaultLocation = LatLng(37.514644,126.979974) // 대전캠퍼스
        // 권한 체크용 boolean 변수
        private var locationPermissionGranted = false
        // 구글맵 변수
        private lateinit var mMap:GoogleMap
        // 사용자의 마지막 위치 가져오기
        private var lastKnownLocation: Location? = null
        // 마커 옵션
        private lateinit var markerOptions : MarkerOptions
        // 반경 원
        private  var previousCircle: Circle? = null
        // 마커
        private lateinit var marker : Marker
        // 서버에서 받은 마커들
        private lateinit var mapMarkerList : MutableList<MapAroundCapsuleRes>

        private val TAG = MapMainFragment::class.java.simpleName

        private const val DEFAULT_ZOOM = 15
        private const val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1 // 위치 권한 체크용

        private const val KEY_CAMERA_POSITION = "카메라 위치"
        private const val KEY_LOCATION = "위치정보"

        // -- sharedPerferenced --
        private var memberId = GlobalAplication.preferences.getInt("currentUser",-1)
        private var userNickname = GlobalAplication.preferences.getString("currentUserNickname","null")

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

        // 상단 벨 숨기기
        bellToggle(true)

        setRepo()

        return binding.root
    }

    // 활동이 일시 중지되었을 경우 저장해놓은 정보를 가져오기 위해 저장
    override fun onSaveInstanceState(outState: Bundle) {
        mMap?.let{
            outState.putParcelable(KEY_CAMERA_POSITION,mMap.cameraPosition)
            outState.putParcelable(KEY_LOCATION, lastKnownLocation)
        }
        super.onSaveInstanceState(outState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 사용자 위치 권한 얻기
        getLocationPermission()

        navController = Navigation.findNavController(view)

        // 지도
        binding.mapFragment.onCreate(savedInstanceState)
        binding.mapFragment.getMapAsync(this)

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
        }else{
            bell?.visibility = View.VISIBLE
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
            var bundle = bundleOf( "lat" to "${lastKnownLocation?.latitude}" , "lng" to "${lastKnownLocation?.longitude}" )
            navController.navigate(R.id.action_mapMainFragment_to_capsuleRegistFragment)
        }

        // 그룹 캡슐 클릭 시
        binding.capsuleRegistGroupBtn.setOnClickListener{
            Log.d("사용자 위치 : 위도 / 전역변수 넘어갈 위도  ", "${lastKnownLocation?.latitude}")
            Log.d("사용자 위치 : 경도 / 전역변수 넘어갈 경도 ", "${lastKnownLocation?.longitude}")

            val geocoder = Geocoder(mainActivity, Locale.KOREA)

            val lat : Double = lastKnownLocation?.latitude!!
            val lng : Double = lastKnownLocation?.longitude!!

            val addressInfo = geocoder.getFromLocation(lat,lng,1)
            val address = addressInfo?.get(0)?.getAddressLine(0)
            Log.d("address" , address.toString())
            GlobalAplication.preferences.setString("lat",lat.toString())
            GlobalAplication.preferences.setString("lng",lng.toString())
            GlobalAplication.preferences.setString("address",address.toString())
            navController.navigate(R.id.action_mapMainFragment_to_capsuleRegistGroupFragment)
        }

        // AR 카메라 클릭 시
        binding.cameraBtn.setOnClickListener{
            GlobalAplication.preferences.setString("ar_lat", lastKnownLocation!!.latitude.toString())
            GlobalAplication.preferences.setString("ar_lng", lastKnownLocation!!.longitude.toString())
            navController.navigate(R.id.action_mapMainFragment_to_arActivity)
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
        mMap = map

        // 사용자 위치
        callbackLocations()

        // 지도 UI 업데이트
        updateLocationUI()

        mMap.isMyLocationEnabled = true
        mMap.setOnMyLocationButtonClickListener(this)
        mMap.setOnMyLocationClickListener(this)
        enableMyLocation()

        // 마커 클릭 이벤트 처리
        mMap.setOnMarkerClickListener(this)

        // 사용자의 위치,카메라 가져오기
        getDeviceLocation()


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
                    if(task.isSuccessful && task.result!=null) { // 위치 접근 성공
                        lastKnownLocation = task.result
                        if(lastKnownLocation != null){
                            val latlng = LatLng(lastKnownLocation?.latitude!!, lastKnownLocation?.longitude!!)
                             mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng, 15f))

                        }else {
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, DEFAULT_ZOOM.toFloat()))
                            mMap.uiSettings?.isMyLocationButtonEnabled = false
                        }
//                        mMap.addMarker(MarkerOptions().position(latlng).title("여기"))
                        Log.d("사용자 위치 : 위도 / 전역변수 ", "${lastKnownLocation?.latitude}")
                        Log.d("사용자 위치 : 경도 / 전역변수 ", "${lastKnownLocation?.longitude}")

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
    // 위치 정보 콜백 함수 호출 ( 일정 주기 정해주기 )
    private fun callbackLocations(){
        val locationRequest = LocationRequest.create()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 20 * 1000
        if (ActivityCompat.checkSelfPermission(
                mainActivity,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                mainActivity,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(mainActivity)

        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());

    }

    // 콜백
    private val locationCallback = object : LocationCallback(){
        override fun onLocationResult(locationResult: LocationResult) {
            locationResult ?: return
            for(location in locationResult.locations){
                lastKnownLocation = location
                Log.d("markerCallback" , "${lastKnownLocation!!.latitude} ${lastKnownLocation!!.longitude}")

                val currPos = LatLng(lastKnownLocation?.latitude!!, lastKnownLocation?.longitude!!)
//                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currPos, 15f))
                Log.d("markerPos" , "${lastKnownLocation!!.latitude} ${lastKnownLocation!!.longitude}")

                // 반경 원 설정
                previousCircle?.remove()  // 이전위치 반경원 제거
                var currCircle = CircleOptions().center(currPos)
                    .radius(1000.0) // 반지름 단위 : m
                    .strokeWidth(0f) // 선 너비
                    .fillColor(Color.parseColor("#88B0E0E6"))
                previousCircle = mMap.addCircle(currCircle) // 새로운 반경원 추가

                mapMarkerList  = mutableListOf()

                val datas = MapAroundCapsuleReq(memberId, lastKnownLocation!!.latitude, lastKnownLocation!!.longitude)

                viewModel.getAroundCapsuleInMap(datas)
                viewModel.aroundCapsuleInMapList.observe(viewLifecycleOwner){
                    setMarkers(it.mapAroundCapsuleResList)
                }


            }
        }
    }

    fun setRepo(){
        val repository = CapsuleRepo()
        val capsuleViewModelFactory = CapsuleViewModelFactory(repository)
        viewModel = ViewModelProvider  (this, capsuleViewModelFactory)[CapsuleViewModel::class.java]

    }

    // 버튼 클릭 시 내 현재 위치로 이동
    override fun onMyLocationButtonClick(): Boolean {
        return false
    }

    // 서버에서 데이터를 받아와서 마커 꾸려주기
    private fun setMarkers(list : MutableList<MapAroundCapsuleRes>){
        for(i in 0 until list.size){
            mapMarkerList.add(list[i])
            var curr = list[i]

            var isAllowedDistance = curr.isAllowedDistance
            var isLocked = curr.isLocked
            var isMine = curr.isMine
            var capsuleId = curr.capsuleId
            var currLocation = LatLng(curr.latitude,curr.longitude)

            markerOptions = MarkerOptions()
            // 마커 색상
            if(isLocked){
                setCustomMarker(R.drawable.locked_marker)
            }else if(isMine){
                setCustomMarker(R.drawable.mine_marker)
            }else{
                setCustomMarker(R.drawable.friend_marker)
            }

            marker = mMap.addMarker(markerOptions.position(currLocation))!!
            val tag = Pair(isAllowedDistance,capsuleId)
            marker?.tag= tag
            // 값 가져오는 방법
            //            val tag = marker.getTag() as? Pair<Boolean, Int>
            //if (tag != null) {
            //    val booleanValue = tag.first
            //    val intValue = tag.second
            //    // 필요한 작업 수행
            //}
        }
    }

    // 마커 이미지 변경
    fun setCustomMarker(img : Int ){
//        예시 )  R.drawable.mine_marker
        var bitmapdraw : BitmapDrawable = resources.getDrawable(img) as BitmapDrawable
        var bitmap = bitmapdraw.bitmap
        var customMarker = Bitmap.createScaledBitmap(bitmap,90,120,false)
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(customMarker))
    }

    // 현재 내 위치 표시 ( 파란 점 )
    override fun onMyLocationClick(l: Location) {
        Log.d("내위치표시","$l")
        var currLat = l.latitude
        var currLng = l.longitude
    }

//    // 마커 클릭 시 ,
//    mMap!!.setOnMarkerClickListener(object : GoogleMap.OnMarkerClickListener {
//        override fun onMarkerClick(marker: Marker): Boolean {
//            // 마커를 클릭했을 때 수행할 작업을 여기에 작성하세요.
//            // marker 객체를 통해 클릭한 마커의 정보에 접근할 수 있습니다.
//            // true를 반환하면 기본 동작이 실행되지 않습니다. false를 반환하면 기본 동작이 실행됩니다.
//            return true
//        }
//    })

    // 마커 클릭 리스너
    override fun onMarkerClick(marker: Marker): Boolean {
        Log.d("marker","$marker")
       return true
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
        // 상단 벨 다시 살리기
        bellToggle(false)

        super.onDestroy()
    }

}