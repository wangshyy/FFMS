package com.wsy.ffms.ui

import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.gyf.immersionbar.ktx.immersionBar
import com.wsy.ffms.R
import com.wsy.ffms.core.base.BaseVMActivity
import com.wsy.ffms.core.etx.toast
import com.wsy.ffms.databinding.AcMainBinding

class MainActivity : BaseVMActivity(), View.OnClickListener {
    private val mBinding by binding<AcMainBinding>(R.layout.ac_main)
    override fun initView() {

        mBinding.apply {
            onClickListener = this@MainActivity
            btmNavMain.setOnNavigationItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.nav_home, R.id.nav_data_statistics, R.id.nav_income_expenditure -> immersionBar {
                        fitsSystemWindows(true)  //使用该属性,必须指定状态栏颜色
                        statusBarColor(R.color.white)
                        statusBarDarkFont(true)
                    }
                    R.id.nav_mine -> immersionBar {
                        fitsSystemWindows(true)  //使用该属性,必须指定状态栏颜色
                        statusBarColor(R.color.colorPrimary)
                        statusBarDarkFont(false)
                    }
                }
                true
            }
        }
        //绑定navigation与btmNavigationView
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        NavigationUI.setupWithNavController(mBinding.btmNavMain, navHostFragment.navController)


    }

    override fun initData() {
    }

    override fun startObserve() {
    }

    override fun onClick(p0: View?) {

    }
}