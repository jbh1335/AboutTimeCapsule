package com.aboutcapsule.android.views.ar

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.aboutcapsule.android.R
import com.aboutcapsule.android.databinding.FragmentArBinding
import com.aboutcapsule.android.views.MainActivity

class ArFragment : Fragment() {
    lateinit var binding : FragmentArBinding
    lateinit var mainActivity: MainActivity

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
}