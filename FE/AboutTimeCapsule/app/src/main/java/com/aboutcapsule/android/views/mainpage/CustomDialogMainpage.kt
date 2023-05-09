package com.aboutcapsule.android.views.mainpage

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.aboutcapsule.android.R
import com.aboutcapsule.android.databinding.FragmentCustomDialogMainpageBinding
import com.aboutcapsule.android.views.map.MapMainFragment
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions


class CustomDialogMainpage : DialogFragment(), OnMapReadyCallback  {
    companion object{
        private var binding : FragmentCustomDialogMainpageBinding? = null
        private lateinit var mapFragment : GoogleMap
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

    // 다이얼로그 없애기
    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
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

    // 지도
    override fun onMapReady(p0: GoogleMap) {
        TODO("Not yet implemented")
    }

    // 좌표 띄우기
    private fun addMarkers(googleMap:GoogleMap) {
        val pos = LatLng(37.514644,126.979974)
        val marker = googleMap.addMarker(
            MarkerOptions()
                .title(" __ 서울 마커 __ ")
                .position(pos)
        )
    }

}