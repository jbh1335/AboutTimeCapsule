package com.aboutcapsule.android.views.login

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.aboutcapsule.android.R
import com.aboutcapsule.android.databinding.FragmentNicknameSettingBinding
import com.aboutcapsule.android.factory.MyPageViewModelFactory
import com.aboutcapsule.android.model.MyPageViewModel
import com.aboutcapsule.android.repository.MypageRepo


class NicknameSettingFragment : Fragment() {
    lateinit var binding:FragmentNicknameSettingBinding
    lateinit var myPageViewModel:MyPageViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getDataFromBack()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_nickname_setting, container, false)
        return binding.root
    }

    fun getDataFromBack() {
        val repository = MypageRepo()
        val myPageViewModelFactory = MyPageViewModelFactory(repository)
        myPageViewModel = ViewModelProvider  (this, myPageViewModelFactory).get(MyPageViewModel::class.java)
        checkNickname()

    }
    @SuppressLint("ResourceAsColor")
    fun checkNickname() {
        binding.nicknameCheckBtn.setOnClickListener {
            myPageViewModel.checkNickname(binding.nicknameEditText.text.toString())
            myPageViewModel.checkNickname.observe(viewLifecycleOwner) {
                if (it == true) {
                    binding.checkNicknameResult.text = "사용 가능한 닉네임입니다."
                    binding.checkNicknameResult.setTextColor(R.color.btnColor)
                } else {
                    binding.checkNicknameResult.text = "이미 존재하는 닉네임 입니다."
                    binding.checkNicknameResult.setTextColor(R.color.falseColor)
                }
        }
    }

//    fun checkNickname() {
//        myPageViewModel.checkNickname.observe(viewLifecycleOwner) {
//            binding.nicknameEditText.addTextChangedListener(object : TextWatcher {
//                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
//                }
//
//                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//                    Log.d("ontext", "${s}")
//                }
//
//                @SuppressLint("ResourceAsColor")
//                override fun afterTextChanged(s: Editable?) {
//                    if (s != null && !s.toString().equals("")) {
//                        Log.d("닉네임변경1", "${s}")
//                        myPageViewModel.checkNickname(s.toString())
//                        myPageViewModel.checkNickname.observe(viewLifecycleOwner) {
//                            if (it == true) {
//                                binding.checkNicknameResult.text = "사용 가능한 닉네임입니다."
//                                binding.checkNicknameResult.setTextColor(R.color.btnColor)
//                            } else {
//                                binding.checkNicknameResult.text = "이미 존재하는 닉네임 입니다."
//                                binding.checkNicknameResult.setTextColor(R.color.falseColor)
//                            }
//                        }
//
//
//
//                    } else {
//                        binding.checkNicknameResult.text = ""
//                    }
//
//                }
//            })
//        }

        }


}