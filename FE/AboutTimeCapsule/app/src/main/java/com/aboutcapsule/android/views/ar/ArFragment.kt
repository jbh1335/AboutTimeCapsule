package com.aboutcapsule.android.views.ar

import android.Manifest
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import com.aboutcapsule.android.R
import com.aboutcapsule.android.databinding.FragmentArBinding
import com.aboutcapsule.android.views.MainActivity
import com.aboutcapsule.android.views.map.MapMainFragment
import com.google.ar.core.ArCoreApk
import com.google.ar.sceneform.ArSceneView
import com.google.ar.sceneform.ux.ArFragment

class ArFragment : ArFragment() {
    lateinit var binding : FragmentArBinding
    lateinit var mainActivity: MainActivity
    private var arSceneView: ArSceneView? = null

    override fun getAdditionalPermissions(): Array<String> =
        listOf(Manifest.permission.ACCESS_FINE_LOCATION).toTypedArray()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_ar,container,false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arSceneView = view.findViewById(R.id.arFragment)
    }

}