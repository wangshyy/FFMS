package com.wsy.ffms.ui.incomeexpenditure.add

import android.graphics.Color
import com.gyf.immersionbar.ktx.immersionBar
import com.wsy.ffms.R
import com.wsy.ffms.core.base.BaseVMActivity
import com.wsy.ffms.databinding.AcAddIncomeExpenditureBinding
import com.wsy.ffms.model.bean.Title
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 *  author : wsy
 *  date   : 2023/3/28
 *  desc   : 新增收入支出页面
 */
class AddIncomeExpenditureActivity : BaseVMActivity() {
    private val mBinding by binding<AcAddIncomeExpenditureBinding>(R.layout.ac_add_income_expenditure)
    private val mViewModel by viewModel<AddIncomeExpenditureViewModel>()
    override fun initView() {
        immersionBar {
            fitsSystemWindows(true)
            statusBarColor(R.color.white)
            statusBarDarkFont(true)
        }
        mBinding.apply {
            title = Title(getString(R.string.common_add), true) { onBackPressed() }
        }
    }

    override fun initData() {
    }

    override fun startObserve() {
    }
}