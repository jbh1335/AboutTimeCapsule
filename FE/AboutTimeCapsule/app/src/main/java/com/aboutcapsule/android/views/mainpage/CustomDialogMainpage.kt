package com.aboutcapsule.android.views.mainpage

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import com.aboutcapsule.android.databinding.FragmentCustomDialogMainpageBinding
import net.daum.mf.map.api.MapView


class CustomDialogMainpage : DialogFragment() {
    private var binding : FragmentCustomDialogMainpageBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCustomDialogMainpageBinding.inflate(inflater,container,false)
        val view = binding?.root

//      테두리 둥글게 만들기 위한 설정
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)

        binding?.mainDialogCloseBtn!!.setOnClickListener {
            dismiss()
        }

//        val mapView = MapView(requireActivity())
//        val mapViewContainer = binding!!.mapView
//        mapViewContainer.addView(mapView)

        return view


    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

}