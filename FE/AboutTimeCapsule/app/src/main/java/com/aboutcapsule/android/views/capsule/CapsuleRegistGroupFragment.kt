package com.aboutcapsule.android.views.capsule

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
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
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.aboutcapsule.android.R
import com.aboutcapsule.android.databinding.FragmentCapsuleRegistGroupBinding
import com.aboutcapsule.android.views.MainActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions


// 캡슐 등록 버튼 눌러서 넘어오면서 좌표 가져와서 지도에 뿌려주기
class CapsuleRegistGroupFragment : Fragment() ,OnMapReadyCallback{

   companion object{
       lateinit var binding : FragmentCapsuleRegistGroupBinding
       lateinit var navController : NavController

       var radioBtn: String =""
       var bottomNavFlag : Boolean = true
       private var bellFlag : Boolean = true

       private lateinit var mMap : GoogleMap

       //권한
       private val permission = arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION
           ,android.Manifest.permission.ACCESS_COARSE_LOCATION)
       private val PERM_FLAG = 99

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

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_capsule_regist_group,container,false)

        binding.registGroupMapFragment.onCreate(savedInstanceState)
        binding.registGroupMapFragment.getMapAsync(this)

        radioBtnListner()

        // 지도 권한
        getPermission()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setNavigation()
        redirectFindFriend()

        submitDatas()

    }

    // TODO : (그룹캡슐) 캡슐 생성버튼 클릭 시 , 캡슐생성 api 보내고 페이지 이동
    private fun submitDatas(){
        val text = binding.capsuleRegistGroupTitle.text.toString()
        binding.capsuleRegistGruopRegistbtn.setOnClickListener {
            if (text.isEmpty() || text.length < 11) {
                Toast.makeText(requireContext(), "제목길이는 1~10글자로 작성 가능합니다.", Toast.LENGTH_SHORT)
                    .show()
            } else {
//                Log.d("allData", radioBtn)
//                Log.d("allData", binding.capsuleRegistGroupTitle.text.toString())
                navController.navigate(R.id.action_capsuleRegistGroupFragment_to_articleRegistFragment)
            }
        }
    }

    private fun setNavigation(){
        val navHostFragment =requireActivity().supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
    }

    private fun redirectFindFriend(){
        binding.addMemberBtn.setOnClickListener {
            navController.navigate(R.id.action_capsuleRegistGroupFragment_to_capsuleFindFriendFragment)
        }
    }

    private fun radioBtnListner(){
        binding.radiogruop2type.setOnCheckedChangeListener{ _ , checkedId ->
            when(checkedId){
                R.id.radio_2type_all ->  radioBtn="ALL"
                R.id.radio_2type_group -> radioBtn="GROUP"
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

    private fun getPermission(){
        // 권한 요청
        if(isPermitted()){
            // 권한 허용 상태면 지도 띄워주기
            startProcess()
        }else{
            // 권한 허용 상태 아니면 허용 체크
            ActivityCompat.requestPermissions(
                requireActivity(),
                permission,
                PERM_FLAG
            )
        }
    }
    // 권한 체크
    private fun isPermitted() : Boolean {
        for(perm in permission){
            if(ContextCompat.checkSelfPermission(requireActivity(),perm) != PermissionChecker.PERMISSION_GRANTED){
                return false
            }
        }
        return false
    }

    // 권한 체크 후 허용 상태면 로직 스타트
    private fun startProcess(){
        binding.registGroupMapFragment.getMapAsync(this)
    }

    // 지도 띄워주기
    // onCreateView에서 getMapAsync(this) 사용허가를 구하면 안드로이드가 메서드 실행
    override fun onMapReady(map: GoogleMap) {
        mMap = map

        val deajeonSS = LatLng(36.355038,127.298297)
        map.addMarker(MarkerOptions().position(deajeonSS).title("대전 캠퍼스 "))
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(deajeonSS,16f))
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
                    if(grant != PermissionChecker.PERMISSION_GRANTED){
                        check=false
                        break;
                    }
                }
                if(check){
                    startProcess()
                }else{
                    Toast.makeText(requireActivity(),"권한을 승인하여 앱을 사용해 보세요", Toast.LENGTH_SHORT).show()
                    requireActivity().finish()
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        binding.registGroupMapFragment.onStart()
    }

    override fun onStop() {
        super.onStop()
        binding.registGroupMapFragment.onStop()
    }

    override fun onResume() {
        super.onResume()
        binding.registGroupMapFragment.onResume()
    }

    override fun onPause() {
        super.onPause()
        binding.registGroupMapFragment.onPause()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding.registGroupMapFragment.onLowMemory()
    }

    override fun onDestroy() {

        // 바텀 네비 다시 살리기
        bottomNavToggle()
        // 상단 벨 다시 살리기
        bellToggle(bellFlag)

        Log.d("editTitle", binding.capsuleRegistGroupTitle.text.toString())

        binding.registGroupMapFragment.onDestroy()
        super.onDestroy()
    }


}