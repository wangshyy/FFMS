package com.wsy.ffms.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.wsy.ffms.ui.incomeexpenditure.expenditure.ExpenditureFragment
import com.wsy.ffms.ui.incomeexpenditure.income.IncomeFragment

/**
 *  author : wsy
 *  date   : 2023/3/10
 *  desc   : 收入支出viewPager适配器
 */
class IncExpPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount() = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> ExpenditureFragment()
            1 -> IncomeFragment()
            else -> ExpenditureFragment()
        }
    }
}