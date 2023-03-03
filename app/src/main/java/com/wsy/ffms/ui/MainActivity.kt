package com.wsy.ffms.ui

import android.app.Activity
import android.os.Build
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
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

    companion object {
        //将view的高度设为状态栏高度
        fun setStatusBarHeight(view: View, activity: Activity) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                // 获得状态栏高度
                val resourceId: Int =
                    activity.resources.getIdentifier("status_bar_height", "dimen", "android")
                val statusBarHeight = activity.resources.getDimensionPixelSize(resourceId)
                val layoutParams = view.layoutParams
                layoutParams.height = statusBarHeight
                view.layoutParams = layoutParams
            }
        }
    }

    override fun initView() {

        mBinding.apply {
            onClickListener = this@MainActivity
        }
        //绑定navigation与btmNavigationView
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        NavigationUI.setupWithNavController(mBinding.btmNavMain, navHostFragment.navController)
        navHostFragment.navController.addOnDestinationChangedListener { _, destination, _ ->
            initStatusBar(destination.id)
        }

    }

    //初始化状态栏
    private fun initStatusBar(ids: Int) {
        when (ids) {
            R.id.nav_home, R.id.nav_data_statistics, R.id.nav_income_expenditure ->
                immersionBar {
                    statusBarDarkFont(true)
                }
            R.id.nav_mine ->
                immersionBar {
                    statusBarDarkFont(false)
                }
        }
    }

    override fun initData() {
    }

    override fun startObserve() {
    }

    override fun onClick(p0: View?) {

    }


}