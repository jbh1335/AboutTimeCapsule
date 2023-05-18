package com.aboutcapsule.android.views.mainpage

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.location.LocationManager
import android.location.LocationRequest
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.aboutcapsule.android.R
import com.aboutcapsule.android.data.capsule.AroundCapsuleDto
import com.aboutcapsule.android.data.capsule.AroundCapsuleReq
import com.aboutcapsule.android.data.capsule.MapAroundCapsuleReq
import com.aboutcapsule.android.databinding.FragmentMainPageMainBinding
import com.aboutcapsule.android.factory.CapsuleViewModelFactory
import com.aboutcapsule.android.model.CapsuleViewModel
import com.aboutcapsule.android.repository.CapsuleRepo
import com.aboutcapsule.android.util.GlobalAplication
import com.aboutcapsule.android.views.MainActivity
import com.aboutcapsule.android.views.map.MapMainFragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Circle
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

class MainPageMainFragment : Fragment() {

    companion object {
        private lateinit var navController: NavController
        private lateinit var binding: FragmentMainPageMainBinding
        lateinit var section2adapter: Section2Adapter
        lateinit var section3adapter: Section3Adapter

        private lateinit var viewModel: CapsuleViewModel

        private var memberId = GlobalAplication.preferences.getInt("currentUser", -1)
        private var userNickname =
            GlobalAplication.preferences.getString("currentUserNickname", "null")

        private var lat: Double = 0.0
        private var lng: Double = 0.0
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_main_page_main, container, false)


//      최초 렌더링시, 말풍선 비활성화
        binding.section2Banner.visibility = View.INVISIBLE
//      000님의 타임 캡슐 view 설정
        binding.mainSection1Title.text = "${userNickname}님의 타임 캡슐"

//        물음표 버튼 토글버틀
        bannerToggle()

        return binding.root

    }

    //    리싸이클러뷰나 뷰페이저 ,,는  onViewCreated 에서 초기화 해주는 것이 좋다
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)

        // 리사이클러뷰 세팅
        setSection3View()

        // 버튼 클릭시 페이지 전환
        redirectPages()

        setLocationUpdates()

        settingView()

    }
    // 바텀 네비 숨기기 토글
    private fun bottomNavToggle(sign : Boolean){
        val mainActivity = activity as MainActivity
        mainActivity.hideBottomNavi(sign)
    }
    // 리사이클러 뷰 두개 세팅
    private fun settingView(){

        val repository = CapsuleRepo()
        val capsuleViewModelFactory = CapsuleViewModelFactory(repository)
        viewModel = ViewModelProvider  (this, capsuleViewModelFactory)[CapsuleViewModel::class.java]
        Log.d("dd","getCapsule _ frag : $memberId " )
        // 1번째 , 캡슐 수
        viewModel.getCapsuleCount(memberId) // 멤버 ID 넣어주기
        viewModel.capsuleCountDatas.observe(viewLifecycleOwner){
                binding.mainSection1Capsule1.text = it.capsuleCountRes.myCapsuleCnt.toString()
                binding.mainSection1Capsule2.text = it.capsuleCountRes.friendCapsuleCnt.toString()
                binding.mainSection1Capsule3.text = it.capsuleCountRes.openCapsuleCnt.toString()
        }


        // 2번째 , 내 주변의 타임 캡슐 세팅
        memberId = 1 // 임시
        Log.d("main/2section" ,"$memberId / $lat / $lng")
        var aroundCapsuleReq = AroundCapsuleReq(memberId,lat,lng)
        viewModel.getAroundCapsuleList(aroundCapsuleReq)
        viewModel.aroundCapsuleList.observe(viewLifecycleOwner){
            setAroundCapsule(it.aroundCapsuleDtoList)
        }


        // 3번째 , 내 주변의 인기장소 세팅
    }

    private fun setLocationUpdates(){
        val lm = requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
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
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 10.0f, locationListener)
    }
    val locationListener = object : android.location.LocationListener {
        override fun onLocationChanged(location: Location) {
            // 위치가 변경되었을 때 호출됩니다.
            lat = location.latitude
            lng = location.longitude
            Log.d("메인페이지 위치", "$lat  $lng" )
        }

        override fun onLocationChanged(locations: MutableList<Location>) {
            super.onLocationChanged(locations)
            for(i in 0 until locations.size){
                lat =locations.get(i).latitude
                lng =locations.get(i).longitude
            }
            //위치가 변경되어 위치가 일괄 전달될 때 호출됩니다.
        }
        override fun onProviderDisabled(provider: String) {
            super.onProviderDisabled(provider)
            // 사용자가 GPS를 끄는 등의 행동을 해서 위치값에 접근할 수 없을 때 호출됩니다.
        }

        override fun onProviderEnabled(provider: String) {
            super.onProviderEnabled(provider)
            // 사용자가 GPS를 on하는 등의 행동을 해서 위치값에 접근할 수 있게 되었을 때 호출됩니다.
        }

        override fun onFlushComplete(requestCode: Int) {
            super.onFlushComplete(requestCode)
            //플러시 작업이 완료되고 플러시된 위치가 전달된 후 호출됩니다.
        }
    }

    // 내 주변 캡슐 아이템 세팅 및 , 데이터 클릭 리스너 생성
    private fun setAroundCapsule(list : MutableList<AroundCapsuleDto>){

        section2adapter = Section2Adapter(object : OnItemClickListener{
            override fun onItemClick(position: Int) {
                val dialog=CustomDialogMainpage()
                val bundle = Bundle()
                val capsuleId = viewModel.aroundCapsuleList.value?.aroundCapsuleDtoList?.get(position)!!.capsuleId
                bundle.putInt("capsuleId",capsuleId)
                bundle.putDouble("lat",lat)
                bundle.putDouble("lng",lng)
                dialog.arguments = bundle
                dialog.show(parentFragmentManager,"customDialog")
            }
        })

        //       어댑터에 api로 받아온 데이터 넘겨주기
        section2adapter.itemList = list
        binding.section2RecyclerView.adapter = section2adapter
    }

    private fun setSection3View(){
        val section3DataList = getSection3datas()
        section3adapter = Section3Adapter()
        //       section3 어댑터의 itemList라는 곳에 데이터 넘겨주기
        section3adapter.itemList = section3DataList
        binding.section3RecyclerView.adapter = section3adapter
    }

    interface OnItemClickListener{
        fun onItemClick(position:Int)
    }

    //  물음표 버튼 토글 로직
    private fun bannerToggle() {
        val banner = binding.section2Banner
        val btn = binding.mainSection2Question
        btn.setOnClickListener {
            if (banner.visibility == View.INVISIBLE) {
                banner.visibility = View.VISIBLE
            } else {
                banner.visibility = View.INVISIBLE
            }
        }
    }

    //    나의 캡슐 클릭시 페이지 이동
    private fun redirectPages() {
        // 나의 캡슐 페이지로 이동 ( 마이캡슐 페이지로 이동, 분기처리해서 api 불러오기 )
        binding.mainSection1Capsule1img.setOnClickListener {
            val bundle = bundleOf("apiName" to "myCapsuleApi" , "lat" to lat ,"lng" to lng)
            navController.navigate(R.id.action_mainPageMainFragment_to_mainPageMyCapsuleFragment,bundle)
        }

        // 친구의 캡슐 ( 마이캡슐 페이지로 이동 , 분기처리해서 api 불러오기 )
        binding.mainSection1Capsule2img.setOnClickListener{
            val bundle = bundleOf("apiName" to "friendApi" , "lat" to lat , "lng" to lng)
            navController.navigate(R.id.action_mainPageMainFragment_to_mainPageMyCapsuleFragment,bundle)
        }

        // 나의 방문 캡슐 기록
        binding.mainSection1Capsule3img.setOnClickListener{
            val bundle = bundleOf("lat" to lat , "lng" to lng)
            navController.navigate(R.id.action_mainPageMainFragment_to_mainPageVisitedFragment,bundle)
        }

        // 상단 툴바 알림페이지로 리다이렉트
        val notiBtn = activity?.findViewById<ImageView>(R.id.toolbar_bell)
        notiBtn?.setOnClickListener{
            navController.navigate(R.id.action_mainPageMainFragment_to_notificationMainFragment)
        }
    }

    //    TODO: retrofit으로 내 주변의 인기장소 데이터 가져오기
    private fun getSection3datas(): MutableList<Section3Data> {
        var itemList = mutableListOf<Section3Data>()

        itemList.apply {
            add(Section3Data(R.drawable.sunglass, "투썸 플레이스"))
            add(Section3Data(R.drawable.heartimg, "삼성 화재 유성연수원"))
            add(Section3Data(R.drawable.sunglass, "한밭 대학교"))
            add(Section3Data(R.drawable.heartimg, "옥녀봉"))
        }
        return itemList
    }


}
