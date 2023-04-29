package com.aboutcapsule.android.views

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.aboutcapsule.android.R
import com.aboutcapsule.android.databinding.ActivityMainBinding

private const val TAG_HOME = "main_page_fragment"
private const val TAG_MAP = "map_fragment"
private const val TAG_CHAT = "chat_fragment"
private const val TAG_MYPAGE = "my_page_fragment"


class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initBinding()
        initNavigation()

        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        val ab = supportActionBar!!
        ab.setDisplayShowTitleEnabled(false)

    }

    private fun initNavigation() {
        val navController = findNavController(R.id.nav_host_fragment)
        binding.navBottom.setupWithNavController(navController)
    }

    private fun initBinding(){
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main)
        binding.lifecycleOwner = this
    }

}