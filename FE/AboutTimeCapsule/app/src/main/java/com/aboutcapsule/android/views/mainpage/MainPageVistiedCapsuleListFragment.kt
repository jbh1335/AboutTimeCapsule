package com.aboutcapsule.android.views.mainpage

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.GridLayoutManager
import com.aboutcapsule.android.R
import com.aboutcapsule.android.data.capsule.OpenedCapsuleDto
import com.aboutcapsule.android.databinding.FragmentCapsuleVistiedBinding
import com.aboutcapsule.android.factory.CapsuleViewModelFactory
import com.aboutcapsule.android.model.CapsuleViewModel
import com.aboutcapsule.android.repository.CapsuleRepo

class MainPageVistiedCapsuleListFragment : Fragment() , MainPageVisitedFragment.DataPassListner {

    lateinit var binding: FragmentCapsuleVistiedBinding
    lateinit var navController: NavController
    lateinit var visitedAdapter: VisitedAdapter

    private lateinit var viewModel: CapsuleViewModel

    private var latitude : Double = 0.0
    private var longitude : Double = 0.0


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_capsule_vistied, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        callingApi()

        setNavigation()

    }

    interface OnVisitedItemClickListener{
        fun onItemClick(position:Int)
    }


    // 방문한 캡슐 ( view )
    private fun setVisitedView(data : MutableList<OpenedCapsuleDto>) {

        visitedAdapter = VisitedAdapter(object : OnVisitedItemClickListener{
            override fun onItemClick(position: Int) {
                val dialog = CustomDialogMainpage()
                val bundle = Bundle()
                val capsuleId = viewModel.myCapsuleList.value?.openedCapsuleDtoList?.get(position)!!.capsuleId

                bundle.putInt("capsuleId", capsuleId)
                bundle.putDouble("lat", latitude)
                bundle.putDouble("lng", longitude)
                dialog.arguments = bundle
                dialog.show(parentFragmentManager, "customDialog")
            }
        })

        var gridManager = GridLayoutManager(context, 3)
        visitedAdapter.itemList = data
        binding.capsuleVisitedRecyclerView.adapter = visitedAdapter
        binding.capsuleVisitedRecyclerView.layoutManager = gridManager
    }

    private fun callingApi() {

        val repository = CapsuleRepo()
        val capsuleViewModelFactory = CapsuleViewModelFactory(repository)
        viewModel = ViewModelProvider(this, capsuleViewModelFactory)[CapsuleViewModel::class.java]

        viewModel.getVisitedCapsuleList(1)
        viewModel.visitedCapsuleList.observe(viewLifecycleOwner){
            setVisitedView(it.openedCapsuleDtoList)
        }
    }

    // 네비게이션 세팅
    private fun setNavigation() {
        val navHostFragment =
            requireActivity().supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
    }

    override fun onDataPass(lat: Double, lng: Double) {
        latitude = lat
        longitude = lng
    }
}