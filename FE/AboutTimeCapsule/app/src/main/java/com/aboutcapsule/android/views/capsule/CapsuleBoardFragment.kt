package com.aboutcapsule.android.views.capsule

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.view.menu.MenuView.ItemView
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.aboutcapsule.android.R
import com.aboutcapsule.android.data.memory.GroupOpenDateReq
import com.aboutcapsule.android.data.memory.MemoryReq
import com.aboutcapsule.android.data.memory.MemoryRes
import com.aboutcapsule.android.databinding.FragmentCapsuleGroupBinding
import com.aboutcapsule.android.factory.CapsuleViewModelFactory
import com.aboutcapsule.android.factory.MemoryViewModelFactory
import com.aboutcapsule.android.factory.MyPageViewModelFactory
import com.aboutcapsule.android.model.CapsuleViewModel
import com.aboutcapsule.android.model.MemoryViewModel
import com.aboutcapsule.android.model.MyPageViewModel
import com.aboutcapsule.android.repository.CapsuleRepo
import com.aboutcapsule.android.repository.MemoryRepo
import com.aboutcapsule.android.repository.MypageRepo
import com.aboutcapsule.android.util.GlobalAplication
import com.aboutcapsule.android.util.SpinnerGroupAvailAdapter
import com.aboutcapsule.android.views.mainpage.CapsuleOpenedAdapter
import com.aboutcapsule.android.views.mainpage.MainPageMainFragment
import com.aboutcapsule.android.views.mainpage.MainPageVisitedCapsuleMapFragment
import com.aboutcapsule.android.views.mypage.MyPageFriendRequestAdapter
import com.bumptech.glide.Glide
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar

class CapsuleBoardFragment : Fragment() {

    companion object{
        lateinit var binding : FragmentCapsuleGroupBinding
        lateinit var navController: NavController
        private var capsuleId :Int= 0
        private var currentUser = GlobalAplication.preferences.getInt("currentUser", -1)
        private var flag = false // 달력 visible 체크용
        private lateinit var memoryViewModel: MemoryViewModel
        private lateinit var capsuleViewModel : CapsuleViewModel
        private lateinit var capsuleArticleInBoardAdapter: CapsuleArticleInBoardAdapter
        private var lat = 0.0
        private var lng = 0.0
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_capsule_group,container,false)
        getDataFromBack()
        getCalendarDate()

        redirectPage()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)



        setNavigation()

        callingApi()

    }

    fun getBundleData(){
        // 맵에서 넘어온 데이터들 ( 다이얼로그 열기버튼 클릭 시 ,)
        val bundleCapsuleId = requireArguments().getInt("capsuleId",-1)
        val bundleLat =requireArguments().getDouble("lat",-1.0)
        val bundleLng =requireArguments().getDouble("lng",-1.0)
        if(bundleCapsuleId != -1){
            capsuleId = bundleCapsuleId
            lat = bundleLat
            lng = bundleLng
            requireArguments().clear() // 다음 데이터를 위해 일단 날려주기
        }else {
            // 그룹캡슐 등록 데이터
            capsuleId = GlobalAplication.preferences.getInt("capsuleId", -1)
            lat = GlobalAplication.preferences.getString("lat", "null").toDouble()
            lng = GlobalAplication.preferences.getString("lng", "null").toDouble()
            Log.d("그룹","$capsuleId , $lat ,$lng")
        }
    }

    // memory 정보 받아오기 최초 렌더링
    fun getDataFromBack() {
        getBundleData()
        val repository = MemoryRepo()
        val memoryViewModelFactory = MemoryViewModelFactory(repository)
        memoryViewModel = ViewModelProvider  (this, memoryViewModelFactory).get(MemoryViewModel::class.java)
        Log.d("체크 getDataFromBack" , "$capsuleId / $currentUser / $lat /$lng")
        val memoryReq = MemoryReq(capsuleId, currentUser, lat, lng)
        memoryViewModel.getCapsuleMemory(memoryReq)
        memoryViewModel.MemoryResData.observe(viewLifecycleOwner, Observer {
            uiSetting(it)


        })
    }

    private fun uiSetting(memoryRes : MemoryRes) {
        val isFirst = memoryRes.isFirstGroup // 최초 생성인지 아닌지 ( 그룹캡슐 최초 생성 체크용 )
        val isMine = memoryRes.isCapsuleMine // 내 게시물인지 아닌지 ( + 버튼 게시글 추가 버튼 체크용 )
        val isGroup = memoryRes.isGroup // 그룹인지 아닌지 체크

        setSpinner(isGroup)

        if(isFirst){ // 최초 생성의 경우
            binding.dateCommentlayout.visibility=View.VISIBLE // 달력 버튼
            binding.capsuleRegistBtn.visibility=View.VISIBLE // 추억 봉인 버튼
            binding.fabBtn.visibility=View.VISIBLE // + 버튼

            binding.capsuleRegistBtn.setOnClickListener {
                val dateString = binding.openAvailDate.text.toString()
                Log.d("데이트스트링", "${dateString}")
                memoryRes.rangeType
                if (dateString == "지정하신 날짜 ") {
                    Toast.makeText(requireContext(), "날짜를 지정해 주세요.", Toast.LENGTH_SHORT).show()
                } else {
                    val openDateString = binding.openAvailDate.text.toString()
                    val dataPattern = "yyyy년 M월 d일"
                    val formatter = DateTimeFormatter.ofPattern(dataPattern)
                    val localDate = LocalDate.parse(openDateString, formatter).toString()
                    memoryViewModel.sealMemoryFirst(GroupOpenDateReq(capsuleId, localDate))

                }
            }

        } else { // 최초 생성 아닌 경우
            if(isMine){ // 내가 수정 ,작성할 수 있는 게시물의 경우
                binding.dateCommentlayout.visibility=View.GONE // 달력 버튼
                binding.capsuleRegistBtn.visibility=View.GONE // 추억 봉인 버튼
                binding.fabBtn.visibility=View.VISIBLE // + 버튼
                binding.fabBtn.visibility=View.VISIBLE // 게시글 작성 버튼 제거
                binding.deleteBtn.visibility=View.VISIBLE // 삭제버튼 제거
                binding.spinnerOpenRange.visibility=View.VISIBLE // 공개범위 수정 버튼 제거


            } else { // 남의 게시물의 경우
                binding.dateCommentlayout.visibility=View.GONE // 달력 버튼
                binding.capsuleRegistBtn.visibility=View.GONE // 추억 봉인 버튼
                binding.fabBtn.visibility=View.GONE // 게시글 작성 버튼 제거
                binding.deleteBtn.visibility=View.GONE // 삭제버튼 제거
                binding.spinnerOpenRange.visibility=View.GONE // 공개범위 수정 버튼 제거

            }
        }

        if(isGroup) { // 그룹일 때
            binding.groupSign.visibility=View.VISIBLE // 그룹 버튼
            binding.memberlistSign.visibility=View.VISIBLE // 멤버 목록 버튼
            binding.privateSign.visibility=View.GONE // 개인 버튼
        }else{ // 그룹이 아닐 경우
            binding.groupSign.visibility=View.GONE // 그룹 버튼
            binding.memberlistSign.visibility=View.GONE // 멤버 목록 버튼
            binding.privateSign.visibility=View.VISIBLE // 개인 버튼
        }

        binding.capsuleTitle.text = memoryRes.capsuleTitle // 제목
        binding.capsulePlace.text = memoryRes.address // 주소
        Glide.with(this).load(R.drawable.redcapsule).into(binding.capsuleImg) // 캡슐 사진

        memoryUiSetting()

    }

    fun memoryUiSetting() {
        capsuleArticleInBoardAdapter = CapsuleArticleInBoardAdapter()
        capsuleArticleInBoardAdapter.itemList = memoryViewModel.MemoryResData.value?.memoryDetailDtoList!!.toMutableList()
        binding.boardCommentsRecylcerView.adapter = capsuleArticleInBoardAdapter
//        binding.boardCommentsRecylcerView.layoutManager =
//            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }


    // 스피너 설정
    private fun setSpinner (sign : Boolean) {
        val repository = CapsuleRepo()
        val capsuleViewModelFactory = CapsuleViewModelFactory(repository)
        capsuleViewModel = ViewModelProvider(this, capsuleViewModelFactory).get(CapsuleViewModel::class.java)
        if(sign) { // 그룹 일 경우
            var list = mutableListOf<String>("전체공개", "그룹공개", "공개범위 ▼") // 스피너 목록 placeholder 가장 마지막으로
            var adapter = SpinnerGroupAvailAdapter(
                requireContext(),
                android.R.layout.simple_spinner_dropdown_item,
                list
            ) // 목록 연결 ,simple요거는 안드로이드가 제공하는 거
            binding.spinnerOpenRange.adapter = adapter // 어댑터 연결
            binding.spinnerOpenRange.setSelection(2) // 스피너 최초로 볼 수 있는 값 ( placeholder) 가장 마지막 idx로 넣어주면 됨
            binding.spinnerOpenRange.dropDownVerticalOffset = dipToPixels(17f).toInt() // 드롭다운 내려오는 위치 ( 스피너 높이만큼 )
            binding.spinnerOpenRange.onItemSelectedListener = object : // 스피너 목록 클릭 시
                AdapterView.OnItemSelectedListener { override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                    if (list.size==3 &&binding.spinnerOpenRange.getItemAtPosition(2).equals("공개범위 ▼")) { // 플레이스 홀더 역할 클릭 시
                        list.remove("공개범위 ▼") // placeholder 역할 제거해주기
                    } else { // 스피너 목록 클릭 시 ( 범위 설정 api )
                        var text = binding.spinnerOpenRange.getItemAtPosition(position)
                        Log.d("스피너", "$text")
                        capsuleViewModel.modifyCapsule(1, replaceRange(text.toString()))  //  "수정할 캡슐 번호 , 범위 "
                    }
                }
                override fun onNothingSelected(p0: AdapterView<*>?) {
                    //  아무것도 선택 안했을 경우
                }
            }
        }else{ // 개인일 경우
            var list =
                mutableListOf<String>("전체공개", "친구만", "나만 보기" , "공개범위 ▼") // 스피너 목록 placeholder 가장 마지막으로
            var adapter = SpinnerGroupAvailAdapter(
                requireContext(),
                android.R.layout.simple_spinner_dropdown_item,
                list
            ) // 목록 연결 ,simple요거는 안드로이드가 제공하는 거
            binding.spinnerOpenRange.adapter = adapter // 어댑터 연결
            binding.spinnerOpenRange.setSelection(3) // 스피너 최초로 볼 수 있는 값 ( placeholder) 가장 마지막 idx로 넣어주면 됨
            binding.spinnerOpenRange.dropDownVerticalOffset =
                dipToPixels(17f).toInt() // 드롭다운 내려오는 위치 ( 스피너 높이만큼 )
            binding.spinnerOpenRange.onItemSelectedListener = object : // 스피너 목록 클릭 시
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    p0: AdapterView<*>?,
                    p1: View?,
                    position: Int,
                    p3: Long
                ) {
                    if (list.size==4 && binding.spinnerOpenRange.getItemAtPosition(3).equals("공개범위 ▼")
                    ) { // 플레이스 홀더 역할 클릭 시
                        list.remove("공개범위 ▼") // placeholder 역할 제거해주기
                    } else { // 스피너 목록 클릭 시 ( 범위 설정 api )
                        var text = binding.spinnerOpenRange.getItemAtPosition(position)
                        Log.d("스피너", "$text")
                        capsuleViewModel.modifyCapsule(1, replaceRange(text.toString())) //  "수정할 캡슐 번호 , 범위 "
                    }
                }
                override fun onNothingSelected(p0: AdapterView<*>?) {
                    //  아무것도 선택 안했을 경우
                }
            }
        }
    }
    // 공개범위 (서버로 보내줄 형태로 변환 )
    private fun replaceRange(range :String ): String{
        if(range == "전체공개") return "ALL"
        else if(range == "친구만") return "FRIEND"
        else if(range == "나만 보기") return "PRIVATE"
        else if(range == "그룹공개") return "GROUP"
        else return "null"
    }


    // 스피너 드롭다운 클릭 시 아래로 내려오는 크기
    private fun dipToPixels(dipValue : Float) : Float{
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dipValue,
            resources.displayMetrics
        )
    }

    private fun redirectPage(){
        // 상단 툴바 알림페이지로 리다이렉트
        val notiBtn = activity?.findViewById<ImageView>(R.id.toolbar_bell)
        notiBtn?.setOnClickListener{
           navController.navigate(R.id.action_capsuleGroupFragment_to_notificationMainFragment)
        }

        // 멤버 목록 클릭 시 , 다이얼로그 보여주기
        binding.memberlistSign.setOnClickListener{
            val dialog = CustomDialogMemberList()
            GlobalAplication.preferences.setInt("memberlist_capsuleId",1); // 캡슐 아이디 넘겨주기
            // TODO: 캡슐 ID 넣어주기
            dialog.show(parentFragmentManager, "customDialog")
        }

        // 하단 '+'버튼 클릭 시 (Floating Action Btn )
        binding.fabBtn.setOnClickListener{
            navController.navigate(R.id.action_capsuleGroupFragment_to_articleRegistFragment)
        }
    }

    // api 호출
    private fun callingApi(){

        // 삭제 버튼 클릭 시
        binding.deleteBtn.setOnClickListener{
            val repository = CapsuleRepo()
            val capsuleViewModelFactory = CapsuleViewModelFactory(repository)
            capsuleViewModel = ViewModelProvider(this, capsuleViewModelFactory).get(CapsuleViewModel::class.java)
            capsuleViewModel.removeCapsule(capsuleId) // 삭제후 리다이렉트 로직 작성 , "삭제할 캡슐 번호 "

        }
    }

    // 네비게이션 세팅
    private fun setNavigation(){
        val navHostFragment =requireActivity().supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        CapsuleRegistFragment.navController = navHostFragment.navController
    }

    // 달력 표기
    private fun getCalendarDate(){

        binding.dateCommentlayout.setOnClickListener {
            flag = true

            if(!flag){
                binding.dateCommentlayout.visibility=View.VISIBLE
                binding.datepickedlayout.visibility=View.GONE
            }else{
                binding.dateCommentlayout.visibility = View.GONE
                binding.datepickedlayout.visibility=View.VISIBLE
            }

            val cal = Calendar.getInstance()

            val data = DatePickerDialog.OnDateSetListener { _, year, month, day ->
                binding.openAvailDate.text = "${year}년 ${month+1}월 ${day}일"
            }

            val textColor= ContextCompat.getColor(requireContext(),R.color.datePickerColor)

            val datePickerDialog = DatePickerDialog(requireContext(),R.style.MyDatePicker ,data, cal.get(
                Calendar.YEAR),cal.get(Calendar.MONTH),cal.get(Calendar.DAY_OF_MONTH))

            datePickerDialog.show()

            val posBtn =datePickerDialog.getButton(DatePickerDialog.BUTTON_POSITIVE)
            val negBtn =datePickerDialog.getButton(DatePickerDialog.BUTTON_NEGATIVE)
            posBtn.setText("확인")
            negBtn.setText("취소")
            posBtn.setTextColor(textColor)
            negBtn.setTextColor(textColor)
        }

        binding.datepickedlayout.setOnClickListener {
            flag = true

            val cal = Calendar.getInstance()

            val data = DatePickerDialog.OnDateSetListener { _, year, month, day ->
                binding.openAvailDate.text = "${year}년 ${month+1}월 ${day}일"
            }

            val textColor= ContextCompat.getColor(requireContext(),R.color.datePickerColor)

            val datePickerDialog = DatePickerDialog(requireContext(),R.style.MyDatePicker ,data, cal.get(
                Calendar.YEAR),cal.get(Calendar.MONTH),cal.get(Calendar.DAY_OF_MONTH))

            datePickerDialog.show()

            val posBtn =datePickerDialog.getButton(DatePickerDialog.BUTTON_POSITIVE)
            val negBtn =datePickerDialog.getButton(DatePickerDialog.BUTTON_NEGATIVE)
            posBtn.setText("확인")
            negBtn.setText("취소")
            posBtn.setTextColor(textColor)
            negBtn.setTextColor(textColor)
        }

    }



}
