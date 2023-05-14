package com.aboutcapsule.android.views.capsule

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.ContextThemeWrapper
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.view.menu.MenuBuilder
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import com.aboutcapsule.android.R
import com.aboutcapsule.android.databinding.FragmentCapsuleMineBinding
import com.aboutcapsule.android.views.mainpage.MainPageMainFragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import okhttp3.internal.notify


class CapsuleArticleFragment : Fragment() {

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

//        setPopUpMenu()
        bottomDialog()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)

        // 댓글 리사이클러뷰 세팅
        setCommentsRecycler()
    }


    // 팝업메뉴 세팅 (수정하기 ,삭제하기 )
//    private fun setPopUpMenu(){
//        binding.threeDots.setOnClickListener{
//         var popupMenu = PopupMenu(context, binding.threeDots)
////            val popupMenu = PopupMenu(context, binding.threeDots, Gravity.NO_GRAVITY,R.style.articlePopupMenu,0)
//            popupMenu.setOnMenuItemClickListener { menuItem ->
//                when(menuItem.itemId){
//                    R.id.menu_delete ->{
//                        true
//                    }
//                    R.id.menu_modify ->{
//                        true
//                    }
//                    else -> false
//                }
//            }
//            popupMenu.show()
////            val customeStyle = R.style.articlePopupMenu
////            if(customeStyle!=0){ // 팝업 커스텀 메뉴 존재하면 ( 혹시 몰라 유효성 처리 )
////                val contextThemeWrapper = ContextThemeWrapper(context,customeStyle)
////                popupMenu.menu.setGroupDividerEnabled(true)
////            popupMenu.menuInflater.inflate(contextThemeWrapper,R.menu.article_popup_menu)
////                popupMenu.menuInflater = MenuInflater(contextThemeWrapper)
////            }
////            val popupStyle = com.nhn.android.oauth.R.attr.popupMenuStyle
////            val popupStyleRes = if (popupStyle != 0) R.style.articlePopupMenu else popupStyle
////            val popup = PopupMenu(context, binding.threeDots,Gravity.NO_GRAVITY,0,popupStyleRes)
//        }
//    }

    private fun bottomDialog(){
        binding.threeDots.setOnClickListener{
            val articleBottomDialogFragment = ArticleBottomDialogFragment {
                when(it){
                    0 -> Toast.makeText(requireContext(), "수정", Toast.LENGTH_SHORT).show()
                    1 -> Toast.makeText(requireContext(), "삭제", Toast.LENGTH_SHORT).show()
                }
            }
            articleBottomDialogFragment.show(requireActivity().supportFragmentManager, articleBottomDialogFragment.tag)
        }
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