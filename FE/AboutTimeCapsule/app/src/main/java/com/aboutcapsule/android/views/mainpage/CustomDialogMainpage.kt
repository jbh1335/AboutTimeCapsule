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
import com.aboutcapsule.android.R
import com.aboutcapsule.android.databinding.FragmentCustomDialogMainpageBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions


class CustomDialogMainpage : DialogFragment(), OnMapReadyCallback  {
    companion object{
        private var binding : FragmentCustomDialogMainpageBinding? = null
        private lateinit var mMap : GoogleMap
        private lateinit var markerOptions : MarkerOptions
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCustomDialogMainpageBinding.inflate(inflater, container, false)
        binding?.mapDialogFragment?.onCreate(savedInstanceState)
        binding?.mapDialogFragment?.getMapAsync(this)

        setDialog()

        return binding?.root

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

//        TODO : API로 받아온 좌표 넣어줘서 지도 띄워주기
        val deajeonSS = LatLng(36.355038,127.298297)

        markerOptions = MarkerOptions()

        setCustomMarker()

        map.addMarker(markerOptions.position(deajeonSS).title("대전 캠퍼스 "))
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(deajeonSS,16f))
    }

    fun setCustomMarker(){
        var bitmapdraw : BitmapDrawable = resources.getDrawable(R.drawable.friend_marker) as BitmapDrawable
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