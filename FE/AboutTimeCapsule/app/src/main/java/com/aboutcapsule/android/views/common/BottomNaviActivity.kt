package com.aboutcapsule.android.views.common

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.aboutcapsule.android.R
import com.aboutcapsule.android.databinding.ActivityBottomNaviBinding
import com.aboutcapsule.android.views.chat.ChatMainFragment
import com.aboutcapsule.android.views.mainpage.MainPageMainFragment
import com.aboutcapsule.android.views.map.MapMainFragment
import com.aboutcapsule.android.views.mypage.MyPageMainFragment

private const val TAG_HOME="main_page_fragment"
private const val TAG_MAP ="map_fragment"
private const val TAG_CHAT="chat_fragment"
private const val TAG_MYPAGE="my_page_fragment"

class BottomNaviActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBottomNaviBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBottomNaviBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 최초렌더링 시 , 메인페이지로 세팅
        setFragment(TAG_MYPAGE,MainPageMainFragment())

        binding.navigationView.setOnItemSelectedListener { item ->
            when(item.itemId){
                R.id.mainPageFragment -> setFragment(TAG_HOME,MainPageMainFragment())
                R.id.mapFragment -> setFragment(TAG_MAP,MapMainFragment())
                R.id.chatFragment -> setFragment(TAG_CHAT,ChatMainFragment())
                R.id.myPageFragment -> setFragment(TAG_MYPAGE,MyPageMainFragment())
            }
            true
        }

    }

    private fun setFragment(tag: String, fragment: Fragment){
        val manager: FragmentManager = supportFragmentManager
        val fragTransaction = manager.beginTransaction()

        if(manager.findFragmentByTag(tag)==null){
            fragTransaction.add(R.id.mainFrameLayout,fragment, tag)
        }

        val home = manager.findFragmentByTag(TAG_HOME)
        val map =manager.findFragmentByTag(TAG_MAP)
        val chat = manager.findFragmentByTag(TAG_CHAT)
        val myPage=manager.findFragmentByTag(TAG_MYPAGE)

        if(home!=null){
            fragTransaction.hide(home)
        }
        if(map!=null){
            fragTransaction.hide(map)
        }
        if(chat!=null){
            fragTransaction.hide(chat)
        }
        if(myPage!=null){
            fragTransaction.hide(myPage)
        }
        if(tag== TAG_HOME){
            if(home!=null){
                fragTransaction.show(home)
            }
        }
        else if(tag == TAG_MAP){
            if(map!=null){
                fragTransaction.show(map)
            }
        }
        else if(tag == TAG_CHAT){
            if(chat !=null){
                fragTransaction.show(chat)
            }
        }
        else if(tag == TAG_MYPAGE){
            if(myPage !=null ){
                fragTransaction.show(myPage)
            }
        }

        fragTransaction.commitAllowingStateLoss()
    }
}