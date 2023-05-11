package com.aboutcapsule.android.views.capsule

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.aboutcapsule.android.R
import com.aboutcapsule.android.databinding.FragmentCapsuleMineBinding
import com.aboutcapsule.android.views.mainpage.MainPageMainFragment


class CapsuleMineFragment : Fragment() {

    companion object{
        lateinit var binding: FragmentCapsuleMineBinding
        lateinit var commentsAdapter: CommentsAdapter
        lateinit var navController: NavController
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_capsule_mine,container,false)

        setNavigation()

        redirectPage()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 댓글 리사이클러뷰 세팅
        setCommentsRecycler()
    }

    private fun redirectPage(){
        // 상단 툴바 알림페이지로 리다이렉트
        val notiBtn = activity?.findViewById<ImageView>(R.id.toolbar_bell)
        notiBtn?.setOnClickListener{
            MainPageMainFragment.navController.navigate(R.id.action_capsuleMineFragment_to_notificationMainFragment)
        }
    }

    // 네비게이션 세팅
    private fun setNavigation(){
        val navHostFragment =requireActivity().supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        CapsuleRegistFragment.navController = navHostFragment.navController
    }


    // 댓글 리사이클러뷰 뷰
    private fun setCommentsRecycler(){
        val commentsDataList = getCommentsdatas()
        commentsAdapter = CommentsAdapter()

        commentsAdapter.itemList = commentsDataList
        binding.commentsRecyclerView.adapter =commentsAdapter
    }


    // 댓글 리사이클러뷰 아이템
    private fun getCommentsdatas() : MutableList<CommentsData>{
        var itemList = mutableListOf<CommentsData>()

        for(i in 1..9){
            var name = "user ${i}"
            var date = "2023.05.0${i}"
            var img = R.drawable.heartimg
            var commnet = " 댓 글  1 2 3 4 ${i}"
            val tmp = CommentsData(img,name,date,commnet)
            itemList.add(tmp)
        }

        return itemList

    }



}