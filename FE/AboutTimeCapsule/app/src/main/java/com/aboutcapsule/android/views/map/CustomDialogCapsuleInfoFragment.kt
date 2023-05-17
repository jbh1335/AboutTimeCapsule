package com.aboutcapsule.android.views.map

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.aboutcapsule.android.R
import com.aboutcapsule.android.data.capsule.MapCapsuleDetailReq
import com.aboutcapsule.android.databinding.FragmentCustomDialogCapsuleInfoBinding
import com.aboutcapsule.android.factory.CapsuleViewModelFactory
import com.aboutcapsule.android.model.CapsuleViewModel
import com.aboutcapsule.android.repository.CapsuleRepo
import com.aboutcapsule.android.util.GlobalAplication


class CustomDialogCapsuleInfoFragment : DialogFragment() {

    private var binding : FragmentCustomDialogCapsuleInfoBinding? = null
    private lateinit var viewModel : CapsuleViewModel
    private lateinit var navController: NavController

    // -- sharedPerferenced --
    private var memberId = GlobalAplication.preferences.getInt("currentUser",-1)
    private var userNickname = GlobalAplication.preferences.getString("currentUserNickname","null")

    // bundle 값
    private  var capsuleId :Int = 0
    private var lat : Double = 0.0
    private var lng : Double = 0.0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentCustomDialogCapsuleInfoBinding.inflate(inflater,container,false)

        setDialog()

        setNavigation()

        callingApi()

        return binding?.root
    }

    // 네비게이션 세팅
    private fun setNavigation(){
        val navHostFragment =requireActivity().supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
    }

    private fun callingApi(){

        val bundle = arguments
        if(bundle != null) {
             capsuleId = bundle.getInt("capsuleId")
             lat = bundle.getDouble("lat")
             lng = bundle.getDouble("lng")

            val data = MapCapsuleDetailReq(capsuleId,memberId,lat,lng)

            val repository = CapsuleRepo()
            val capsuleViewModelFactory = CapsuleViewModelFactory(repository)
            viewModel = ViewModelProvider(this, capsuleViewModelFactory)[CapsuleViewModel::class.java]

            viewModel.getCapsuleInMapDetail(data)
            viewModel.capsuleInMapDetailDatas.observe(viewLifecycleOwner){
                var isGroup = it.isGroup // 그룹 여부
                var isLocked = it.isLocked // 잠김 여부
                var openDate = it.openDate // 오픈 날짜 ( LocalDate타입 )
                var memberName =it.memberNickname // 멤버 이름
                var leftTime = it.leftTime // 남은 시간

                if(isLocked) {// 잠겨있을 경우
                    binding?.xBtn?.visibility=View.GONE
                    binding?.capsuleInfoUsername?.text=memberName
                    binding?.capsuleinfoDialogBtn?.text="닫기"
                    if(isGroup){
                        binding?.groupSign?.visibility=View.VISIBLE
                        binding?.privateSign?.visibility=View.GONE
                    }else{
                        binding?.groupSign?.visibility=View.GONE
                        binding?.privateSign?.visibility=View.VISIBLE
                    }
                    binding?.capsuleInfoOpendate?.visibility=View.GONE
                    binding?.capsuleInfoAvailDate?.visibility=View.VISIBLE
                    binding?.capsuleInfoAvailDate?.text=leftTime

                    binding?.capsuleinfoDialogBtn!!.setOnClickListener { // 닫기 버튼
                        dismiss()
                    }
                }else{ // 열려있을 경우
                    binding?.xBtn?.visibility=View.VISIBLE
                    binding?.capsuleInfoUsername?.text=memberName
                    binding?.capsuleinfoDialogBtn?.text="열기"
                    if(isGroup){
                        binding?.groupSign?.visibility=View.VISIBLE
                        binding?.privateSign?.visibility=View.GONE
                    }else{
                        binding?.groupSign?.visibility=View.GONE
                        binding?.privateSign?.visibility=View.VISIBLE
                    }
                    binding?.capsuleInfoOpendate?.visibility=View.VISIBLE
                    binding?.capsuleInfoAvailDate?.visibility=View.GONE
                    binding?.capsuleInfoOpendate?.text=openDate

                    binding?.xBtn!!.setOnClickListener {
                        dismiss()
                    }
                    binding?.capsuleinfoDialogBtn?.setOnClickListener{
                        // 상세 페이지로 이동 !
                        // 열기 버튼 클릭했는지 판단용 클릭리스너
                        dismiss()
                    }
                }
            }
        }
    }



    // 다이얼로그 테두리 설정
    private fun setDialog(){

        //  테두리 둥글게 만들기 위한 설정
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
    }

    override fun onDestroy() {
        // 다이얼로그 없애기
        binding = null

        super.onDestroy()
    }

}