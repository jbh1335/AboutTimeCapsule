package com.aboutcapsule.android.views.mainpage

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.aboutcapsule.android.R
import com.aboutcapsule.android.data.capsule.MapInfoDto
import com.aboutcapsule.android.databinding.FragmentMainPageVisitedCapsuleMapBinding
import com.aboutcapsule.android.factory.CapsuleViewModelFactory
import com.aboutcapsule.android.model.CapsuleViewModel
import com.aboutcapsule.android.repository.CapsuleRepo
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions


class MainPageVisitedCapsuleMapFragment : Fragment() , OnMapReadyCallback ,
    MainPageVisitedFragment.DataPassListner {
    private val TAG = "VisitedCapsuleMapFragment"
    companion object {
        lateinit var binding: FragmentMainPageVisitedCapsuleMapBinding
        lateinit var navController: NavController
        private lateinit var mMap : GoogleMap
        lateinit var markerOptions: MarkerOptions
        lateinit var mapMarkers : MutableList<MapInfoDto>

        private lateinit var viewModel : CapsuleViewModel

        private var latitude : Double = 0.0
        private var longitude : Double = 0.0
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_main_page_visited_capsule_map,container,false)

        mapMarkers = mutableListOf()
        binding.visitedMapFragment.onCreate(savedInstanceState)
        binding.visitedMapFragment.getMapAsync(this)

        callingApi() // api 받아오기

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

    private fun callingApi(){

        val repository = CapsuleRepo()
        val capsuleViewModelFactory = CapsuleViewModelFactory(repository)
        viewModel = ViewModelProvider(this, capsuleViewModelFactory).get(CapsuleViewModel::class.java)
        viewModel.getVisitedCapsuleList(1)
        viewModel.visitedCapsuleList.observe(viewLifecycleOwner) {
                    Log.d(TAG, "${it.mapInfoDtoList}")

                    mapMarkers = it.mapInfoDtoList
                }
    }

    // 지도 띄워주기
    // onCreateView에서 getMapAsync(this) 사용허가를 구하면 안드로이드가 메서드 실행
    override fun onMapReady(map: GoogleMap) {
       mMap = map

        val defalutPos = LatLng(35.894332,127.9)
        for(i in 0 until mapMarkers.size ) {

            var currLatLng = LatLng(mapMarkers[i].latitude,mapMarkers[i].longitude)
            var capsuleId = mapMarkers[i].capsuleId
            var isLock = mapMarkers[i].isLocked
            var isOpen = mapMarkers[i].isOpened

            markerOptions = MarkerOptions()
            setCustomOpenMarker(isOpen,isLock)
           val marker = mMap.addMarker(markerOptions.position(currLatLng))
            marker?.tag = capsuleId
        }

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defalutPos,6.8f))
    }

    fun setCustomOpenMarker(openFlag : Boolean , lockFlag : Boolean){

        if(lockFlag) {
            var bitmapdraw: BitmapDrawable =
                resources.getDrawable(R.drawable.locked_marker) as BitmapDrawable
            var bitmap = bitmapdraw.bitmap
            var customMarker = Bitmap.createScaledBitmap(bitmap, 90, 120, false)
            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(customMarker))
        }else if(openFlag){
            var bitmapdraw: BitmapDrawable =
                resources.getDrawable(R.drawable.mine_marker) as BitmapDrawable
            var bitmap = bitmapdraw.bitmap
            var customMarker = Bitmap.createScaledBitmap(bitmap, 90, 120, false)
            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(customMarker))
        }
    }


    override fun onStart() {
        super.onStart()
        binding.visitedMapFragment.onStart()
    }

    override fun onStop() {
        super.onStop()
        binding.visitedMapFragment.onStop()
    }

    override fun onResume() {
        super.onResume()
        binding.visitedMapFragment.onResume()
    }

    override fun onPause() {
        super.onPause()
        binding.visitedMapFragment.onPause()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding.visitedMapFragment.onLowMemory()
    }

    override fun onDestroy() {
      binding.visitedMapFragment.onDestroy()
        super.onDestroy()
    }

    override fun onDataPass(lat: Double, lng: Double) {
        latitude = lat
        longitude = lng
    }

}