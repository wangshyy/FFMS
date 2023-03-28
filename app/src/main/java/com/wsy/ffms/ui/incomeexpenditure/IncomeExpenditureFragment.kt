package com.wsy.ffms.ui.incomeexpenditure

import com.google.android.material.tabs.TabLayoutMediator
import com.wsy.ffms.R
import com.wsy.ffms.adapter.IncExpPagerAdapter
import com.wsy.ffms.core.base.BaseVMFragment
import com.wsy.ffms.core.etx.startKtxActivity
import com.wsy.ffms.databinding.FgIncomeExpenditureBinding
import com.wsy.ffms.model.bean.Title
import com.wsy.ffms.ui.MainActivity
import com.wsy.ffms.ui.incomeexpenditure.add.AddIncomeExpenditureActivity

/**
 *  author : wsy
 *  date   : 2023/3/2
 *  desc   : 收入支出模块
 */
class IncomeExpenditureFragment :
    BaseVMFragment<FgIncomeExpenditureBinding>(R.layout.fg_income_expenditure) {
    override fun initView() {
        MainActivity.setStatusBarHeight(binding.flStatusBar, requireActivity())
        binding.apply {
            title = Title(getString(R.string.income_expenditure), false) {}
            viewpager.adapter = IncExpPagerAdapter(this@IncomeExpenditureFragment)
            viewpager.offscreenPageLimit = 2
            viewpager.isUserInputEnabled = false//true滑动 false禁止滑动

            //tabLayout与viewPager联动
            TabLayoutMediator(tabLayout, viewpager) { tab, position ->
                when (position) {
                    0 -> tab.text = getString(R.string.expenditure)
                    1 -> tab.text = getString(R.string.income)
                    else -> tab.text = getString(R.string.expenditure)
                }
            }.attach()

            fabAdd.setOnClickListener {
                //新增
                startKtxActivity<AddIncomeExpenditureActivity>()
            }
        }
    }

    override fun initData() {
    }

    override fun startObserve() {
    }
}