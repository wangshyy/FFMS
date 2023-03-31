package com.wsy.ffms.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.wsy.ffms.BR
import com.wsy.ffms.R
import com.wsy.ffms.databinding.ItemIncomeListBinding
import com.wsy.ffms.db.income.Income

/**
 *  author : wsy
 *  date   : 2023/3/31
 *  desc   : 收入列表适配器
 */
class IncomeListAdapter :
    BaseQuickAdapter<Income, BaseDataBindingHolder<ItemIncomeListBinding>>(R.layout.item_income_list) {
    override fun convert(holder: BaseDataBindingHolder<ItemIncomeListBinding>, item: Income) {
        holder.dataBinding?.run {
            setVariable(BR.model, item)
            executePendingBindings()
        }
    }
}