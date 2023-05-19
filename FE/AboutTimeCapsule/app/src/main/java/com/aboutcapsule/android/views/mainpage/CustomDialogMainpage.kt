package com.aboutcapsule.android.views.mainpage

import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.aboutcapsule.android.R
import com.aboutcapsule.android.data.capsule.CapsuleDetailReq
import com.aboutcapsule.android.databinding.FragmentCustomDialogMainpageBinding
import com.aboutcapsule.android.factory.CapsuleViewModelFactory
import com.aboutcapsule.android.model.CapsuleViewModel
import com.aboutcapsule.android.repository.CapsuleRepo
import com.aboutcapsule.android.util.GlobalAplication
import com.aboutcapsule.android.views.map.MapMainFragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions


class CustomDialogMainpage() : DialogFragment(), OnMapReadyCallback  {
    companion object{
        private var binding : FragmentCustomDialogMainpageBinding? = null
        private lateinit var mMap : GoogleMap
        // 마커
        private lateinit var marker : Marker
        // 마커 옵션
        private lateinit var markerOptions : MarkerOptions

        private var memberId = GlobalAplication.preferences.getInt("currentUser",-1)
        private var userNickname = GlobalAplication.preferences.getString("currentUserNickname","null")

        private  var resLat : Double = 0.0
        private  var resLng : Double = 0.0
        private var userLat : Double = 0.0
        private var userLng : Double = 0.0
        private var isLocked : Boolean = false
        private lateinit var viewModel : CapsuleViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCustomDialogMainpageBinding.inflate(inflater, container, false)
        binding?.mapDialogFragment?.onCreate(savedInstanceState)
        binding?.mapDialogFragment?.getMapAsync(this)

        setDialog()

        callingApi()

        return binding?.root

    }

    private fun callingApi(){

        val bundle = arguments
        if(bundle != null) {
            val capsuleId = bundle.getInt("capsuleId")
            userLat = bundle.getDouble("lat")
            userLng = bundle.getDouble("lng")

            val repository = CapsuleRepo()
            val capsuleViewModelFactory = CapsuleViewModelFactory(repository)
            viewModel = ViewModelProvider  (this, capsuleViewModelFactory)[CapsuleViewModel::class.java]

            viewModel.getCapsuleDetail(CapsuleDetailReq(capsuleId,memberId,userLat,userLng))
            viewModel.capsuleDetailDatas.observe(viewLifecycleOwner){
               isLocked = it.isLocked
               val leftTime = it.leftTime
               val name = it.memberNickname
               val capsuleId = it.capsuleId
               val address = it.address
               val distance = it.distance
               resLat = it.latitude
               resLng = it.longitude
                if(isLocked){ // 잠겨 있을 경우
                    binding!!.dialogUserRemainTime.visibility=View.VISIBLE
                    binding?.dialogUserRemainTime!!.text=leftTime
                }else{
                    binding!!.dialogUserRemainTime.visibility=View.GONE
                }
                binding!!.dialogUsername.text="${name}님의 캡슐"
                binding!!.dialogUserDistance.text="${distance}m 떨어져 있어요"

            }
        }

    }

    // 다이얼로그 테두리 설정
    private fun setDialog(){

    //  테두리 둥글게 만들기 위한 설정
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)

        binding?.mainDialogCloseBtn!!.setOnClickListener {
            dismiss()
        }
    }


    // 지도 띄워주기
    // onCreateView에서 getMapAsync(this) 사용허가를 구하면 안드로이드가 메서드 실행
    override fun onMapReady(map: GoogleMap) {
        mMap = map

        markerOptions = MarkerOptions()
        val user = LatLng(userLat, userLng)
        setCustomMarker(R.drawable.friend_marker)
        marker = mMap.addMarker(markerOptions.position(user))!!

        markerOptions = MarkerOptions()
        val place = LatLng(resLat, resLng)
        if(isLocked){
            setCustomMarker(R.drawable.locked_marker)
        }else{
            setCustomMarker(R.drawable.mine_marker)
        }
        marker = mMap.addMarker(markerOptions.position(place))!!

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(user,14f))
    }

    fun setCustomMarker(img : Int){
        var bitmapdraw : BitmapDrawable = resources.getDrawable(img) as BitmapDrawable
        var bitmap = bitmapdraw.bitmap
        var customMarker = Bitmap.createScaledBitmap(bitmap,90,120,false)
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(customMarker))
    }


    override fun onStart() {
        super.onStart()
        binding?.mapDialogFragment?.onStart()
    }

    override fun onStop() {
        super.onStop()
        binding?.mapDialogFragment?.onStop()
    }

    override fun onResume() {
        super.onResume()
        binding?.mapDialogFragment?.onResume()
    }

    override fun onPause() {
        super.onPause()
        binding?.mapDialogFragment?.onPause()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding?.mapDialogFragment?.onLowMemory()
    }

    override fun onDestroy() {

        binding?.mapDialogFragment?.onDestroy()
        // 다이얼로그 없애기
        binding = null

        super.onDestroy()
    }
}