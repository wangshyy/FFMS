package com.wsy.ffms.ui.incomeexpenditure.expenditure

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.jeremyliao.liveeventbus.LiveEventBus
import com.wsy.ffms.R
import com.wsy.ffms.adapter.IncomeListAdapter
import com.wsy.ffms.core.base.BaseVMFragment
import com.wsy.ffms.databinding.FgExpenditureBinding
import com.wsy.ffms.ui.incomeexpenditure.IncomeExpenditureViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 *  author : wsy
 *  date   : 2023/3/10
 *  desc   : 支出页面
 */
class ExpenditureFragment :BaseVMFragment<FgExpenditureBinding>(R.layout.fg_expenditure){
    private val mViewModel by viewModel<IncomeExpenditureViewModel>()
    private val mExpenditureListAdapter by lazy { ExpenditureAdapter() }
    private lateinit var mEmptyView: View
    override fun initView() {
        binding.apply {
            rvExpenditure.apply {
                adapter = mExpenditureListAdapter
                layoutManager = LinearLayoutManager(context)
            }
        }
        //空页面
        mEmptyView =
            layoutInflater.inflate(
                R.layout.view_empty,
                binding.rvExpenditure.parent as ViewGroup,
                false
            )

        //订阅消息
        LiveEventBus.get<String>("add_type")
            .observe(this){
                when(it){
                    //添加成功，更新列表
                    "expenditure" -> mViewModel.getExpenditureList()
                }
            }
    }

    override fun initData() {
    }

    override fun startObserve() {
    }
}