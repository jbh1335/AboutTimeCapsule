package com.aboutcapsule.android.views.capsule

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.aboutcapsule.android.R
import com.aboutcapsule.android.databinding.FragmentCapsuleFindFriendBinding


class CapsuleFindFriendFragment : Fragment() , TextWatcher {

    companion object {
        lateinit var binding: FragmentCapsuleFindFriendBinding
        private var findFriendAdapter = FindFriendAdapter()
        lateinit var navController: NavController

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_capsule_find_friend,container,false)


        recyclerToggle()

        setNavigation()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 리사이클러뷰 세팅
        setFindFriendRecylcer()

        setNavigation()

        redirectPage()

    }

    private fun redirectPage(){
        binding.completeBtn.setOnClickListener{
            navController.navigate(R.id.action_capsuleFindFriendFragment_to_capsuleRegistGroupFragment)
        }
    }

    // 네비게이션 세팅
    private fun setNavigation(){
        val navHostFragment =requireActivity().supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
    }

    // 리사이클러 뷰 버튼 클릭 시 , 토글
    private fun recyclerToggle(){
        // editText 클릭시 보여주기
        binding.searchEditText.setOnClickListener{
                binding.findFriendsRecyclerView.visibility=View.VISIBLE
        }
        // 다른 부분 클릭 시 리사이클러뷰 숨기기
        binding.searchEditText.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                binding.findFriendsRecyclerView.visibility=View.VISIBLE
            }else {
                binding.findFriendsRecyclerView.visibility=View.GONE
            }
        }
    }

    // 리사이클러뷰 세팅
    fun setFindFriendRecylcer(){
        binding.searchEditText.addTextChangedListener(this)

        findFriendAdapter = FindFriendAdapter()
        findFriendAdapter.itemList = getFriendList()
        findFriendAdapter.filterList = getFriendList() // 필터용
        binding.findFriendsRecyclerView.adapter=findFriendAdapter
    }

//    edittext watcher
    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }
    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        findFriendAdapter.getFilter().filter(p0.toString())
    }
    override fun afterTextChanged(p0: Editable?) {
    }
// ---------


    // 친구 찾기 데이터
    private fun getFriendList() : MutableList<FindFriendData> {
        var itemList = mutableListOf<FindFriendData>()

        var img = R.drawable.camera
        itemList.add(FindFriendData(img,"강나다"))
        itemList.add(FindFriendData(img,"abc"))
        itemList.add(FindFriendData(img,"다라쥐"))
        itemList.add(FindFriendData(img,"bcd"))
        itemList.add(FindFriendData(img,"귀부인"))
        itemList.add(FindFriendData(img,"카메라"))
        return itemList
    }

}