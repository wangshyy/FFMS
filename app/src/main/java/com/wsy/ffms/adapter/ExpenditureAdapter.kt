package com.wsy.ffms.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.wsy.ffms.BR
import com.wsy.ffms.R
import com.wsy.ffms.databinding.ItemExpenditureListBinding
import com.wsy.ffms.db.expenditure.Expenditure

/**
 *  author : wsy
 *  date   : 2023/4/1
 *  desc   : 支出列表适配器
 */
class ExpenditureAdapter :
    BaseQuickAdapter<Expenditure, BaseDataBindingHolder<ItemExpenditureListBinding>>(R.layout.item_expenditure_list) {
    override fun convert(
        holder: BaseDataBindingHolder<ItemExpenditureListBinding>,
        item: Expenditure
    ) {
        holder.dataBinding?.run {
            setVariable(BR.model, item)
            executePendingBindings()
        }
    }
}