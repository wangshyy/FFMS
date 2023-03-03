package com.wsy.ffms.ui.incomeexpenditure

import com.wsy.ffms.R
import com.wsy.ffms.core.base.BaseVMFragment
import com.wsy.ffms.databinding.FgIncomeExpenditureBinding
import com.wsy.ffms.ui.MainActivity

/**
 *  author : wsy
 *  date   : 2023/3/2
 *  desc   : 收入支出模块
 */
class IncomeExpenditureFragment: BaseVMFragment<FgIncomeExpenditureBinding>(R.layout.fg_income_expenditure) {
    override fun initView() {
        MainActivity.setStatusBarHeight(binding.flStatusBar,requireActivity())
        binding.apply {

        }
    }

    override fun initData() {
    }

    override fun startObserve() {
    }
}