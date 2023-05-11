package com.aboutcapsule.android.views.capsule

import android.Manifest
import android.app.Activity.RESULT_OK
import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import com.aboutcapsule.android.R
import com.aboutcapsule.android.databinding.FragmentArticleRegistBinding
import com.bumptech.glide.Glide
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission
import java.util.Calendar

class ArticleRegistFragment : Fragment(),View.OnClickListener {


    /*

        상황에 따라 달력 보이게 안보이게 onCreate에서 받아온 데이터 체크 한다음 view visibility 정해주기

     */


    companion object {
        private lateinit var binding: FragmentArticleRegistBinding
        lateinit var navController: NavController
        private var picture_flag = 0
        private var fileAbsolutePath: String? = null
        private var bellFlag: Boolean = true

        private var flag = false

        // 갤러리에서 데이터(사진) 가져올 때 사용
        lateinit var resultLauncher: ActivityResultLauncher<Intent>
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_article_regist,container,false)


        binding.galleryBtn.setOnClickListener(this)

        getGalleryData()

        getCalendarDate()

        bellToggle(bellFlag)

        setNavigation()

        redirectPage()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)

    }

    override fun onDestroy() {
        // 상단 벨 다시 살리기
        bellToggle(bellFlag)

        super.onDestroy()
    }

    fun redirectPage(){
        // 분기처리 해서 그룹 or 개인 캡슐 페이지로 이동
//        binding.articleRegistRegistbtn.setOnClickListener{
//            navController.navigate(R.id.action_articleRegistFragment_to_capsuleMineFragment)
//        }
        binding.articleRegistRegistbtn.setOnClickListener{
            navController.navigate(R.id.action_articleRegistFragment_to_capsuleGroupFragment)
        }
    }

    // 네비게이션 세팅
    private fun setNavigation(){
        val navHostFragment =requireActivity().supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        CapsuleRegistFragment.navController = navHostFragment.navController
    }

    fun getCalendarDate(){

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

            val datePickerDialog = DatePickerDialog(requireContext(),R.style.MyDatePicker ,data, cal.get(Calendar.YEAR),cal.get(Calendar.MONTH),cal.get(Calendar.DAY_OF_MONTH))

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

            val datePickerDialog = DatePickerDialog(requireContext(),R.style.MyDatePicker ,data, cal.get(Calendar.YEAR),cal.get(Calendar.MONTH),cal.get(Calendar.DAY_OF_MONTH))

            datePickerDialog.show()

            val posBtn =datePickerDialog.getButton(DatePickerDialog.BUTTON_POSITIVE)
            val negBtn =datePickerDialog.getButton(DatePickerDialog.BUTTON_NEGATIVE)
            posBtn.setText("확인")
            negBtn.setText("취소")
            posBtn.setTextColor(textColor)
            negBtn.setTextColor(textColor)
        }

    }

    // 상단바 벨 사라지게 / 페이지 전환 시 다시 생성
    private fun bellToggle(sign : Boolean){
        var bell = activity?.findViewById<ImageView>(R.id.toolbar_bell)
        if(sign) {
            bell?.visibility = View.GONE
            bellFlag=false
        }else{
            bell?.visibility = View.VISIBLE
            bellFlag =true
        }
    }

    fun getGalleryData(){
        resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult())
        {
            if(it.resultCode == RESULT_OK){
                if(picture_flag == 1){
                    it.data?.data?.let { uri ->
                        val imageUri: Uri? = it.data?.data
                        if(imageUri != null){
                            activity?.applicationContext?.let { it1 ->
                                Glide.with(it1).load(imageUri).override(500,500)
                                    .into(binding.selectedPhoto)
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onClick(v: View?) {
        when(v?.id){
            binding.galleryBtn.id -> { settingPermission(1)}

        }
    }

    fun settingPermission(permis_num: Int){
        val permis = object : PermissionListener {

            override fun onPermissionGranted(){
                if(permis_num == 1) {
                    move_gallery()
                }
            }

            override fun onPermissionDenied(deniedPermission: MutableList<String>?){}
        }
        if(permis_num == 1){
            checkPer_gallery(permis)
        }
    }

    // 갤러리로 이동
    fun move_gallery(){
        val intent = Intent(Intent.ACTION_PICK)
        intent.data = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        intent.type = "image/*"
        // 이미지 여러장 선택하기
        // 페이지 다시 이동
        resultLauncher.launch(intent)
        // 갤러리로
        picture_flag = 1
    }

    // 갤러리 관련 권한 체크
    fun checkPer_gallery(permis: PermissionListener){
        TedPermission.create()
            .setPermissionListener(permis)
            .setDeniedMessage("[설정] > [권한] 에서 권한을 허용할 수 있습니다.")
            .setPermissions(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
            ).check()
    }

}