package com.wsy.ffms.ui.incomeexpenditure.income

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.jeremyliao.liveeventbus.LiveEventBus
import com.jeremyliao.liveeventbus.core.LiveEvent
import com.wsy.ffms.R
import com.wsy.ffms.adapter.IncomeListAdapter
import com.wsy.ffms.core.base.BaseVMFragment
import com.wsy.ffms.databinding.FgIncomeBinding
import com.wsy.ffms.ui.incomeexpenditure.IncomeExpenditureViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 *  author : wsy
 *  date   : 2023/3/10
 *  desc   : 收入页面
 */
class IncomeFragment : BaseVMFragment<FgIncomeBinding>(R.layout.fg_income) {
    private val mViewModel by viewModel<IncomeExpenditureViewModel>()
    private val mIncomeListAdapter by lazy { IncomeListAdapter() }
    private lateinit var mEmptyView: View
    override fun initView() {
        binding.apply {
            rvIncome.apply {
                adapter = mIncomeListAdapter
                layoutManager = LinearLayoutManager(context)
            }
        }
        //空页面
        mEmptyView =
            layoutInflater.inflate(
                R.layout.view_empty,
                binding.rvIncome.parent as ViewGroup,
                false
            )

        //订阅消息
        LiveEventBus.get<String>("add_type")
            .observe(this){
                when(it){
                    //添加成功，更新列表
                    "income" -> mViewModel.getIncomeList()
                }
            }
    }

    override fun initData() {
        mViewModel.getIncomeList()
    }

    override fun startObserve() {

        mViewModel.uiState.observe(this) {
            it.showIncomeList?.let { list ->
                mIncomeListAdapter.setList(list)
            }
            if (it.showIncomeList?.size ?: 0 == 0) mIncomeListAdapter.setEmptyView(mEmptyView)
        }
    }
}