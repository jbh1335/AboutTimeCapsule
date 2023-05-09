package com.aboutcapsule.android.views.capsule

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.aboutcapsule.android.R
import com.aboutcapsule.android.databinding.FragmentCapsuleRegistGroupBinding

class CapsuleRegistGroupFragment : Fragment() {

   lateinit var binding : FragmentCapsuleRegistGroupBinding
   lateinit var navController : NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_capsule_regist_group,container,false)

        radiobtnListner()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setNavigation()
        redirectFindFriend()
        redirectTest()
    }



    private fun setNavigation(){
        val navHostFragment =requireActivity().supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
    }

    fun redirectFindFriend(){
        binding.addMemberBtn.setOnClickListener {
            navController.navigate(R.id.action_capsuleRegistGroupFragment_to_capsuleFindFriendFragment)
        }
    }
    fun redirectTest(){
        binding.addMemberView.setOnClickListener{
            navController.navigate(R.id.action_capsuleRegistGroupFragment_to_articleRegistFragment)
        }
    }


    fun radiobtnListner(){
        binding.radiogruop2type.setOnCheckedChangeListener{ group , checkedId ->
            when(checkedId){
                R.id.radio_2type_all -> "api에 번호로 넘겨주려나 ? "
                R.id.radio_2type_group -> Log.d("라디오2type버튼", "체크 확인 !" )
            }
        }
    }
}