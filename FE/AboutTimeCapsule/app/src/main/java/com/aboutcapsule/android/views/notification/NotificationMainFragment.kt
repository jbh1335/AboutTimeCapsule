package com.aboutcapsule.android.views.notification

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.aboutcapsule.android.R
import com.aboutcapsule.android.databinding.FragmentNotificationMainBinding


class NotificationMainFragment : Fragment() {

    lateinit var binding : FragmentNotificationMainBinding
    lateinit var navController : NavController
    lateinit var notificationAdapter : NotificationAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_notification_main,container,false)

        bellToggle(true)

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setNotificationView()

        setNavigation()

        setToolbar()

    }

    override fun onDestroy() {
        bellToggle(false)
        super.onDestroy()
    }

    // 알림들 (view)
    private fun setNotificationView(){
        val notificationData = getNotidatas()
        notificationAdapter = NotificationAdapter()

        notificationAdapter.itemList = notificationData
        binding.notiftcationRecyclerView.adapter= notificationAdapter

    }
    // 알림들 (data)
    private fun getNotidatas(): MutableList<NotificationData>{
        var itmeList = mutableListOf<NotificationData>()

        for(i in 1 .. 10 ){
            val img =R.drawable.redcapsule
            val text = "알림 내용 ${i}"
            val tmp = NotificationData(img,text)
            itmeList.add(tmp)
        }
        return itmeList
    }

    // 네비게이션 세팅
    private fun setNavigation(){
        val navHostFragment =requireActivity().supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
    }

    // 상단바 벨 사라지게 / 페이지 전환 시 다시 생성
    private fun bellToggle(sign : Boolean){
        var bell = activity?.findViewById<ImageView>(R.id.toolbar_bell)
        if(sign) {
            bell?.visibility = View.GONE
        }else{
            bell?.visibility = View.VISIBLE
        }
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