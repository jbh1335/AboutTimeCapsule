package com.aboutcapsule.android.views.mainpage

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.GridLayoutManager
import com.aboutcapsule.android.R
import com.aboutcapsule.android.databinding.FragmentCapsuleVistiedBinding

class CapsuleVistiedFragment : Fragment() {

    lateinit var binding : FragmentCapsuleVistiedBinding
    lateinit var navController: NavController
    lateinit var visitedAdapter : CapsuleUnOpenedAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_capsule_vistied,container,false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setVisitedView()

        setNavigation()

    }

    // 방문한 캡슐 ( view )
    private fun setVisitedView(){
        var gridManager : GridLayoutManager

        val visitedDataList = getVisitedDatas()
        visitedAdapter = CapsuleUnOpenedAdapter()

        gridManager =GridLayoutManager(context,3)
//        visitedAdapter.itemList = visitedDataList
        binding.capsuleVisitedRecyclerView.adapter =visitedAdapter
        binding.capsuleVisitedRecyclerView.layoutManager = gridManager
    }

    // 방문한 캡슐 ( data )
    private fun getVisitedDatas(): MutableList<CapsuleListData>{
        var itemList = mutableListOf<CapsuleListData>()

        for(i in 1..9){
            var time = "2023.05.0${i}"
            var place = "장소 위치 ${i}"
            var img = R.drawable.redcapsule
            var isLock = false
            var lockimg =R.drawable.lockimg
            val tmp = CapsuleListData(img,time,place,isLock,lockimg)
            itemList.add(tmp)
        }

        return itemList
    }

    // 네비게이션 세팅
    private fun setNavigation(){
        val navHostFragment =requireActivity().supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
    }
}