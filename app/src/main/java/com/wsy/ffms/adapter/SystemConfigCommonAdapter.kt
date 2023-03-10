package com.wsy.ffms.adapter

import androidx.lifecycle.MutableLiveData
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.lxj.xpopup.XPopup
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

    //删除id 根据id删除对应item
    var deleteId = MutableLiveData<Int?>()

    //清空删除id
    fun clearDeleteId() {
        deleteId.value = null
    }

    override fun convert(
        holder: BaseDataBindingHolder<ItemSystemConigCommonBinding>,
        item: SystemConfigCommonBean
    ) {
        holder.dataBinding?.run {
            //删除按钮
            smMenuViewRight.setOnClickListener {
                XPopup.Builder(context).asConfirm(
                    context.getString(R.string.delete_reminder),
                    null,
                    context.getString(R.string.common_cancel),
                    context.getString(R.string.common_confirm),
                    {
                        deleteId.value = item.id
                        sml.smoothCloseMenu() //关闭侧滑栏
                    },
                    {
                        sml.smoothCloseMenu() //关闭侧滑栏
                    },
                    false,
                    R.layout.dialog_my_confim_popup
                ).show()
            }
            setVariable(BR.model, item)
            executePendingBindings()
        }
    }
}