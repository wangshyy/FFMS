package com.wsy.ffms.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.wsy.ffms.BR
import com.wsy.ffms.R
import com.wsy.ffms.databinding.ItemSystemConigCommonBinding
import com.wsy.ffms.model.bean.SystemConfigCommonBean

/**
 *  author : wsy
 *  date   : 2023/3/9
 *  desc   :
 */
class SystemConfigCommonAdapter :
    BaseQuickAdapter<SystemConfigCommonBean, BaseDataBindingHolder<ItemSystemConigCommonBinding>>(R.layout.item_system_conig_common) {
    override fun convert(
        holder: BaseDataBindingHolder<ItemSystemConigCommonBinding>,
        item: SystemConfigCommonBean
    ) {
        holder.dataBinding?.run {
            setVariable(BR.model,item)
            executePendingBindings()
        }
    }
}