package com.aboutcapsule.android.views.map

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.widget.Toolbar
import androidx.core.graphics.createBitmap
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.aboutcapsule.android.R
import com.aboutcapsule.android.databinding.FragmentMainPageMainBinding
import com.aboutcapsule.android.databinding.FragmentMapMainBinding

class MapMainFragment : Fragment() {

    lateinit var binding : FragmentMapMainBinding
    lateinit var navController: NavController
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_map_main,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)

        setNavigation()

        setToolbar()

        redirectNotification()
    }


    fun redirectNotification(){
        val notiBtn = activity?.findViewById<ImageView>(R.id.toolbar_bell)
        notiBtn?.setOnClickListener{
            navController.navigate(R.id.action_mapMainFragment_to_notificationMainFragment)
        }
    }

    // 네비게이션 세팅
    private fun setNavigation(){
        val navHostFragment =requireActivity().supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
    }

    private fun setToolbar() {
        // 액티비티에서 툴바 가져오기
        val toolbar = requireActivity().findViewById<Toolbar>(R.id.toolbar)

        // Navigation Component와 툴바 연결
        val navController = findNavController()
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        toolbar.setupWithNavController(navController, appBarConfiguration)

        // 프래그먼트 전환 이벤트 감지 및 툴바 업데이트
        navController.addOnDestinationChangedListener { _, destination, _ ->
            toolbar.title = ""
            toolbar.setNavigationOnClickListener {
                navController.navigateUp()
            }
        }
    }

}