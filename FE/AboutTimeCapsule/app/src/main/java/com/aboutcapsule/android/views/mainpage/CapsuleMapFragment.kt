package com.aboutcapsule.android.views.mainpage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.aboutcapsule.android.R
import com.aboutcapsule.android.databinding.FragmentCapsuleMapBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions


class CapsuleMapFragment : Fragment() , OnMapReadyCallback {

    companion object {
        lateinit var binding: FragmentCapsuleMapBinding
        lateinit var navController: NavController
        private lateinit var mMap : GoogleMap

        //권한
        private val permission = arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION
            ,android.Manifest.permission.ACCESS_COARSE_LOCATION)
        private val PERM_FLAG = 99
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_capsule_map,container,false)
        binding.mainpageMapFragment.onCreate(savedInstanceState)
        binding.mainpageMapFragment.getMapAsync(this)

        getPermission()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setNavigation()

    }

    private fun setNavigation(){
        val navHostFragment =requireActivity().supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
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
       binding.mainpageMapFragment.getMapAsync(this)
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
        binding.mainpageMapFragment.onStart()
    }

    override fun onStop() {
        super.onStop()
        binding.mainpageMapFragment.onStop()
    }

    override fun onResume() {
        super.onResume()
        binding.mainpageMapFragment.onResume()
    }

    override fun onPause() {
        super.onPause()
        binding.mainpageMapFragment.onPause()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding.mainpageMapFragment.onLowMemory()
    }

    override fun onDestroy() {
      binding.mainpageMapFragment.onDestroy()
        super.onDestroy()
    }

}