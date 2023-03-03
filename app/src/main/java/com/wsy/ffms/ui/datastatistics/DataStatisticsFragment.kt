package com.wsy.ffms.ui.datastatistics

import com.wsy.ffms.R
import com.wsy.ffms.core.base.BaseVMFragment
import com.wsy.ffms.databinding.FgDataStatisticsBinding
import com.wsy.ffms.ui.MainActivity

/**
 *  author : wsy
 *  date   : 2023/3/2
 *  desc   : 数据分析模块
 */
class DataStatisticsFragment :
    BaseVMFragment<FgDataStatisticsBinding>(R.layout.fg_data_statistics) {
    override fun initView() {
        MainActivity.setStatusBarHeight(binding.flStatusBar, requireActivity())
        binding.apply {

        }
    }

    override fun initData() {
    }

    override fun startObserve() {
    }
}