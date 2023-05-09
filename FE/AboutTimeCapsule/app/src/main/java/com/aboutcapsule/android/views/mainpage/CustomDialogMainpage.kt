package com.aboutcapsule.android.views.mainpage

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import com.aboutcapsule.android.R
import com.aboutcapsule.android.databinding.FragmentCustomDialogMainpageBinding
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng


class CustomDialogMainpage : DialogFragment() , OnMapReadyCallback {
    companion object{
        private var binding : FragmentCustomDialogMainpageBinding? = null
        private lateinit var mapView : MapView
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        binding = FragmentCustomDialogMainpageBinding.inflate(inflater, container, false)

        setDialog()

        val rootView = inflater.inflate(R.layout.fragment_custom_dialog_mainpage,container,false)
        mapView = rootView.findViewById(R.id.mapView) as MapView
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)
//        return binding?.root
        return rootView

    }

    // 다이얼로그 없애기
    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    // 다이얼로그 테두리 설정
    private fun setDialog(){

//      테두리 둥글게 만들기 위한 설정
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)

        binding?.mainDialogCloseBtn!!.setOnClickListener {
            dismiss()
        }
    }

    //지도
    override fun onMapReady(p0: GoogleMap) {
        val point = LatLng(37.514655, 126.979974)
//        map.addMarker(MarkerOptions().position(point).titile("현위치")
//                map.moveCamera(CameraUpdateFactory.newLatLngZoom(point,12f))
    }

}