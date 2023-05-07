package com.aboutcapsule.android.views.capsule

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.aboutcapsule.android.R
import com.aboutcapsule.android.databinding.FragmentCapsuleRegistGroupBinding
import com.aboutcapsule.android.views.MainActivity

class CapsuleRegistGroupFragment : Fragment() {

   lateinit var binding : FragmentCapsuleRegistGroupBinding
   lateinit var navController : NavController
   companion object{
       var radioBtn: String =""
       var bottomNavFlag : Boolean = true
       var bellFlag : Boolean = true
   }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 바텀 네비 숨기기
        bottomNavToggle()
        // 상단 벨 숨기기
        bellToggle(bellFlag)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_capsule_regist_group,container,false)

        radioBtnListner()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setNavigation()
        redirectFindFriend()
        redirectTest()

       binding.capsuleRegistGruopRegistbtn.setOnClickListener {
                Log.d("allData", radioBtn)
                Log.d("allData", binding.capsuleRegistGroupTitle.text.toString())
        }

//        submitDatas()

    }

    override fun onDestroy() {

        // 바텀 네비 다시 살리기
        bottomNavToggle()
        // 상단 벨 다시 살리기
        bellToggle(bellFlag)

        super.onDestroy()
    }

    // TODO : (그룹캡슐) 캡슐 생성버튼 클릭 시 , 캡슐생성 api 보내고 페이지 이동
    private fun submitDatas(){
        val text = binding.capsuleRegistGroupTitle.text.toString()
        binding.capsuleRegistGruopRegistbtn.setOnClickListener {
            if (text.isEmpty() || text.length < 11) {
                Toast.makeText(requireContext(), "제목길이는 1~10글자로 작성 가능합니다.", Toast.LENGTH_SHORT)
                    .show()
            } else {
                Log.d("allData", radioBtn)
                Log.d("allData", binding.capsuleRegistGroupTitle.text.toString())
            }
        }
    }

    private fun setNavigation(){
        val navHostFragment =requireActivity().supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
    }

    private fun redirectFindFriend(){
        binding.addMemberBtn.setOnClickListener {
            navController.navigate(R.id.action_capsuleRegistGroupFragment_to_capsuleFindFriendFragment)
        }
    }
    private fun redirectTest(){
        binding.addMemberView.setOnClickListener{
            navController.navigate(R.id.action_capsuleRegistGroupFragment_to_articleRegistFragment)
        }
    }

    private fun radioBtnListner(){
        binding.radiogruop2type.setOnCheckedChangeListener{ _ , checkedId ->
            when(checkedId){
                R.id.radio_2type_all ->  radioBtn="ALL"
                R.id.radio_2type_group -> radioBtn="GROUP"
            }
        }
    }

    // 바텀 네비 숨기기 토글
    private fun bottomNavToggle(){
        val mainActivity = activity as MainActivity
        mainActivity.hideBottomNavi(bottomNavFlag)
        bottomNavFlag= !bottomNavFlag
    }

    // 상단바 벨 사라지게 / 페이지 전환 시 다시 생성
    private fun bellToggle(sign : Boolean){
        var bell = activity?.findViewById<ImageView>(R.id.toolbar_bell)
        if(sign) {
            bell?.visibility = View.GONE
        }else{
            bell?.visibility = View.VISIBLE
        }
        bellFlag = !bellFlag
    }

}