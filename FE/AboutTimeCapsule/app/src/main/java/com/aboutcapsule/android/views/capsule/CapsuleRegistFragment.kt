package com.aboutcapsule.android.views.capsule

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
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
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.aboutcapsule.android.R
import com.aboutcapsule.android.data.capsule.PostRegistCapsuleReq
import com.aboutcapsule.android.databinding.FragmentCapsuleRegistBinding
import com.aboutcapsule.android.factory.CapsuleViewModelFactory
import com.aboutcapsule.android.model.CapsuleViewModel
import com.aboutcapsule.android.repository.CapsuleRepo
import com.aboutcapsule.android.util.GlobalAplication
import com.aboutcapsule.android.views.MainActivity
import com.aboutcapsule.android.views.mainpage.CapsuleMapFragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

// 캡슐 등록 버튼 눌러서 넘어오면서 좌표 가져와서 지도에 뿌려주기
class CapsuleRegistFragment : Fragment() , OnMapReadyCallback {

     companion object{
        lateinit var binding : FragmentCapsuleRegistBinding
        lateinit var navController: NavController
        private lateinit var mMap : GoogleMap
        lateinit var markerOptions: MarkerOptions
        private var isGroup : Boolean = false
        private var lat : Double = 0.0
        private var lng : Double = 0.0
        private lateinit var address : String
        private var radioBtn :String= ""
        private lateinit var viewModel : CapsuleViewModel

         private var memberId = GlobalAplication.preferences.getInt("currentUser",-1)
         private var userNickname = GlobalAplication.preferences.getString("currentUserNickname","null")

     }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 바텀 네비 숨기기
        bottomNavToggle(true)

        // 상단 벨 숨기기
        bellToggle(true)

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

        // 주소 ,위도, 경도 넘겨 받아옴
        getBundleData()


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

    private fun getBundleData(){
        // 친구 찾기 페이지 가버리면 날라가서 shared로 받음
        lat = GlobalAplication.preferences.getString("lat","-1").toDouble()
        lng = GlobalAplication.preferences.getString("lng","-1").toDouble()
        address = GlobalAplication.preferences.getString("address","null")
    }

    // TODO : (개인) 캡슐 생성버튼 클릭 시 , 캡슐생성 api 보내고 페이지 이동
    private fun submitBtnClickListner(){

        binding.capsuleRegistRegistbtn.setOnClickListener{

            val title = binding.capsuleRegistTitle.editableText.toString()

            if(radioBtn == ""){
                Toast.makeText(requireContext(),"공개 범위를 설정해주세요",Toast.LENGTH_SHORT).show()
            }else if (title.isEmpty() || title.length > 30) {
                Toast.makeText(requireContext(), "제목길이는 1~30글자로 작성 가능합니다.", Toast.LENGTH_SHORT).show()
            } else {
                val memberlist = ArrayList<Int>(memberId)
                Log.d("success in radio","${radioBtn}")
                val repository = CapsuleRepo()
                val capsuleViewModelFactory = CapsuleViewModelFactory(repository)
                viewModel = ViewModelProvider  (this, capsuleViewModelFactory).get(
                    CapsuleViewModel::class.java)
                var postRegistCapsuleData = PostRegistCapsuleReq(memberlist,title, radioBtn, isGroup,
                    lat,
                    lng,
                   address
                )
               viewModel.addCapsule(postRegistCapsuleData)
                val bundle = bundleOf("capsuleTitle" to  title,"isGroup" to false) // 그룹 여부도 보내줌
                navController.navigate(R.id.action_capsuleRegistFragment_to_articleRegistFragment, bundle)
            }
        }
    }

    // 라디오 버튼
   private fun radioBtnListner(){
        binding.radiogruop3type.setOnCheckedChangeListener{ group , checkedId ->
            when(checkedId){
                R.id.radio_3type_all -> radioBtn = "ALL"
                R.id.radio_3type_friend -> radioBtn = "FRIEND"
                R.id.radio_3type_mine -> radioBtn = "PRIVATE"
            }
        }

        if(radioBtn =="ALL"){ // 선택후 친구 찾으러 갔을 경우 체크 상태로 유지
            binding.radiogruop3type.check(R.id.radio_3type_all)
        }else if (radioBtn =="FRIEND"){
            binding.radiogruop3type.check(R.id.radio_3type_friend)
        }else if(radioBtn =="PRIVATE"){
            binding.radiogruop3type.check(R.id.radio_3type_mine)
        }
    }

    // 바텀 네비 숨기기 토글
    private fun bottomNavToggle(sign : Boolean){
        val mainActivity = activity as MainActivity
        mainActivity.hideBottomNavi(sign)
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

    // 지도 띄워주기
    // onCreateView에서 getMapAsync(this) 사용허가를 구하면 안드로이드가 메서드 실행
    override fun onMapReady(map: GoogleMap) {
        mMap = map

        markerOptions = MarkerOptions()
        setCustomMarker()

        val deajeonSS = LatLng(36.355038,127.298297)
        mMap.addMarker(markerOptions.position(deajeonSS).title("대전 캠퍼스 "))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(deajeonSS,16f))
    }

    fun setCustomMarker(){
        var bitmapdraw : BitmapDrawable = resources.getDrawable(R.drawable.mine_marker) as BitmapDrawable
        var bitmap = bitmapdraw.bitmap
        var customMarker = Bitmap.createScaledBitmap(bitmap,90,120,false)
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(customMarker))
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
        bottomNavToggle(false)
        // 상단 벨 다시 살리기
        bellToggle(false)

        Log.d("editTitle",binding.capsuleRegistTitle.text.toString())

        binding.registAloneMapFragment.onDestroy()
        super.onDestroy()
    }
}