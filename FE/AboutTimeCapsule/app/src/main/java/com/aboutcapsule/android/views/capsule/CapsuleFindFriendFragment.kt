package com.aboutcapsule.android.views.capsule

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.aboutcapsule.android.R
import com.aboutcapsule.android.databinding.FragmentCapsuleFindFriendBinding


class CapsuleFindFriendFragment : Fragment() , TextWatcher {

    companion object {
        lateinit var binding: FragmentCapsuleFindFriendBinding
        private lateinit var findFriendAdapter : FindFriendAdapter
        lateinit var navController: NavController
        private var itemList = mutableListOf<FindFriendData>()
        // 라시이클러뷰 에서 삭제된 친구 아래에 추가해주기
        private var chkidx = 0 // 임시로, 리사이클러뷰 지우는거 체크용
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
        // 추가된 멤버 번들에 담아서 넘어가서 뿌려주기
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

        findFriendAdapter = FindFriendAdapter(onClickDeleteIcon = { deleteTask(it)} )
        findFriendAdapter.itemList = getFriendList()
        findFriendAdapter.filterList = getFriendList() // 필터용
        binding.findFriendsRecyclerView.adapter=findFriendAdapter
    }

    // 리사이클러뷰에서 하나 지우고 아래 멤버로 추가 되기
    fun deleteTask(findFriendData: FindFriendData){

        createView(findFriendData)
        itemList.remove(findFriendData)
        binding.findFriendsRecyclerView.adapter?.notifyDataSetChanged()
        binding.findFriendsRecyclerView.visibility=View.GONE
        Log.d("추가멤버","${findFriendData}")
    }

    //멤버 추가 동적으로 뷰 생성
    private fun createView(findFriendData: FindFriendData){
        val customView = LayoutInflater.from(requireContext()).inflate(R.layout.added_member_list,null,false)
        val param: LinearLayout.LayoutParams =
            LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT)
        param.bottomMargin = 30
        customView.layoutParams=param
        customView.findViewById<ImageView>(R.id.added_member_img).setImageResource(findFriendData.Img)
        customView.findViewById<TextView>(R.id.added_member_name).text=findFriendData.name
        binding.addedMemberListView.addView(customView)
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
        itemList = mutableListOf()

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