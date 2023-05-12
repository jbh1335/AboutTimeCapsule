package com.aboutcapsule.android.views

import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.aboutcapsule.android.R
import com.aboutcapsule.android.databinding.ActivityMainBinding
import com.aboutcapsule.android.util.GlobalAplication
import com.aboutcapsule.android.util.PreferenceUtil
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    companion object{
        lateinit var preferences: PreferenceUtil
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setCurrentUser()
        initBinding()
        initNavigation()

        initFirebase()

        bottomNav()

        // sharedPreference
        preferences = PreferenceUtil(applicationContext)

    }

    private fun bottomNav() {
        binding.navBottom.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.mainPageMainFragment -> {
                    navController.navigate(R.id.mainPageMainFragment)
                    true
                }
                R.id.mapMainFragment -> {
                    navController.navigate(R.id.mapMainFragment)
                    true
                }
                R.id.chatMainFragment -> {
                    navController.navigate(R.id.chatMainFragment)
                    true
                }
                R.id.myPageMainFragment -> {
                    CoroutineScope(Dispatchers.Main).launch{
                        val currentUser = GlobalAplication.getInstance().getDataStore().getcurrentMemberId.first()
                        navController.navigate(R.id.myPageMainFragment)
                    }
                    true
                }
                else -> false
            }
        }
    }

    private fun initFirebase() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d("firebaseToken", "${task.result}")
            }
        }

    }

    private fun initNavigation() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        binding.navBottom.setupWithNavController(navController)
    }

    private fun initBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this
    }

    // TODO : 다른 프래그먼트에서 바텀 네비 숨기고 싶을 경우 메서드 가져가서 사용
    fun hideBottomNavi(state : Boolean) {
        if(state) binding.navBottom.visibility = View.GONE
        else binding.navBottom.visibility = View.VISIBLE
    }

    // 화면 터치 시 키보드 내리기
    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        val imm: InputMethodManager =
            getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
        return super.dispatchTouchEvent(ev)
    }

    fun setCurrentUser() {

    }

}

