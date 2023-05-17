package com.aboutcapsule.android.views.mainpage

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.aboutcapsule.android.R
import com.aboutcapsule.android.data.capsule.MapInfoDto
import com.aboutcapsule.android.data.capsule.OpenedCapsuleDto
import com.aboutcapsule.android.data.capsule.UnopenedCapsuleDto
import com.aboutcapsule.android.databinding.FragmentCapsuleListBinding
import com.aboutcapsule.android.factory.CapsuleViewModelFactory
import com.aboutcapsule.android.model.CapsuleViewModel
import com.aboutcapsule.android.repository.CapsuleRepo


class CapsuleListFragment : Fragment() , MainPageMyCapsuleFragment.DataPassListner {

    lateinit var binding : FragmentCapsuleListBinding
    lateinit var navController: NavController
    lateinit var unOpenedAdapter : CapsuleUnOpenedAdapter
    lateinit var openedAdapter : CapsuleOpenedAdapter

    companion object{
        private lateinit var findHost : String // 분기처리 정보 넘겨받음 , api 판별용
        private lateinit var viewModel : CapsuleViewModel
        private var lat : Double = 0.0 //좌 표
        private var lng : Double = 0.0 //좌 표

        private const val TAG = "CapsuleListFragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_capsule_list,container,false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        Log.d("캡슐리스트프래그먼트" ,MainActivity.preferences.getString("meOrFriend","notKey"))

        setData()

        setNavigation()

        redirectPage()

        callingApi() // api 받아오기

    }

    // 뷰페이저 페이지에서 넘어온 데이터 받아서 api 통신 ( 나 , 친구 분기처리 )
    override fun onDataPass(data: String) {
        findHost = data
    }
    // onDataPass로 MainPageMyCapsuleFragment에서 넘겨받은 분기처리 정보 받은 후 api 통신

    fun setData(){
        lat = requireArguments().getDouble("lat")
        lng = requireArguments().getDouble("lng")
    }
    private fun callingApi(){

        val repository = CapsuleRepo()
        val capsuleViewModelFactory = CapsuleViewModelFactory(repository)
        viewModel = ViewModelProvider  (this, capsuleViewModelFactory).get(CapsuleViewModel::class.java)

        when(findHost){
            "myCapsuleApi" -> {
                Log.d("api", " 내 캡슐  ")

               viewModel.getMyCapsuleList(1)
               viewModel.myCapsuleList.observe(viewLifecycleOwner) {
                   setWaitingView(it.unopenedCapsuleDtoList)
                   setMeetView(it.openedCapsuleDtoList)

               }
            }
            "friendApi" -> {
                Log.d("api", " 친구 캡슐  ")
                viewModel.getFriendCapsuleList(1)
                viewModel.friendCapsuleList.observe(viewLifecycleOwner){
                    setWaitingView(it.unopenedCapsuleDtoList)
                    setMeetView(it.openedCapsuleDtoList)
                }
            }
        }
    }

    // 000님을 기다리고 있어요 ( view )
    private fun setWaitingView(data : MutableList<UnopenedCapsuleDto>){
        unOpenedAdapter = CapsuleUnOpenedAdapter(object : OnWaitingItemClickListener{
            override fun onItemClick(position: Int) {

                when(findHost) {
                    "myCapsuleApi" -> {
                        val dialog = CustomDialogMainpage()
                        val bundle = Bundle()
                        val capsuleId = viewModel.myCapsuleList.value?.unopenedCapsuleDtoList?.get(position)!!.capsuleId

                        bundle.putInt("capsuleId", capsuleId)
                        bundle.putDouble("lat", lat)
                        bundle.putDouble("lng", lng)
                        dialog.arguments = bundle
                        dialog.show(parentFragmentManager, "customDialog")
                    }
                    "friendApi" -> {
                        val dialog = CustomDialogMainpage()
                        val bundle = Bundle()
                        val capsuleId = viewModel.friendCapsuleList.value?.unopenedCapsuleDtoList?.get(position)!!.capsuleId

                        bundle.putInt("capsuleId", capsuleId)
                        bundle.putDouble("lat", lat)
                        bundle.putDouble("lng", lng)
                        dialog.arguments = bundle
                        dialog.show(parentFragmentManager, "customDialog")
                    }
                }
            }
        })

        unOpenedAdapter.itemList = data
        binding.capsuleListSection1RecyclerView.adapter =unOpenedAdapter
    }

    interface OnWaitingItemClickListener{
        fun onItemClick(position : Int)
    }

    interface OnMeetingItemClickListener{
        fun onItemClick(position : Int)
    }

    // 000님과 만났어요 ( view )
    private fun setMeetView(data : MutableList<OpenedCapsuleDto>){
        openedAdapter = CapsuleOpenedAdapter(object : OnMeetingItemClickListener{
            override fun onItemClick(position: Int) {

                when(findHost) {
                    "myCapsuleApi" -> {
                        val dialog = CustomDialogMainpage()
                        val bundle = Bundle()
                        val capsuleId = viewModel.myCapsuleList.value?.openedCapsuleDtoList?.get(position)!!.capsuleId

                        bundle.putInt("capsuleId", capsuleId)
                        bundle.putDouble("lat", lat)
                        bundle.putDouble("lng", lng)
                        dialog.arguments = bundle
                        dialog.show(parentFragmentManager, "customDialog")
                    }
                    "friendApi" -> {
                        val dialog = CustomDialogMainpage()
                        val bundle = Bundle()
                        val capsuleId = viewModel.friendCapsuleList.value?.openedCapsuleDtoList?.get(position)!!.capsuleId

                        bundle.putInt("capsuleId", capsuleId)
                        bundle.putDouble("lat", lat)
                        bundle.putDouble("lng", lng)
                        dialog.arguments = bundle
                        dialog.show(parentFragmentManager, "customDialog")
                    }
                }

            }
        })

        openedAdapter.itemList = data
        binding.capsuleListSection2RecyclerView.adapter =openedAdapter
    }



    // 네비게이션 세팅
    private fun setNavigation(){
        val navHostFragment =requireActivity().supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
    }

    private fun redirectPage(){

        // 상단 툴바 알림페이지로 리다이렉트
        val notiBtn = activity?.findViewById<ImageView>(R.id.toolbar_bell)
        notiBtn?.setOnClickListener{
            navController.navigate(R.id.action_mainPageMyCapsuleFragment_to_notificationMainFragment)
        }
    }

}