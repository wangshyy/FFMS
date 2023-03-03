package com.wsy.ffms.ui.home

import com.wsy.ffms.R
import com.wsy.ffms.core.base.BaseVMFragment
import com.wsy.ffms.databinding.FgHomeBinding
import com.wsy.ffms.model.bean.Title
import com.wsy.ffms.ui.MainActivity

/**
 *  author : wsy
 *  date   : 2023/3/2
 *  desc   : 首页
 */
class HomeFragment : BaseVMFragment<FgHomeBinding>(R.layout.fg_home) {
    override fun initView() {
        MainActivity.setStatusBarHeight(binding.flStatusBar, requireActivity())
        binding.apply {
            title = Title(getString(R.string.home), false) {}
        }
    }

    override fun initData() {
    }

    override fun startObserve() {
    }
}