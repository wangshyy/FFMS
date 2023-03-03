package com.wsy.ffms.ui.mine

import androidx.lifecycle.MutableLiveData
import com.wsy.ffms.core.base.BaseViewModel

/**
 *  author : wsy
 *  date   : 2023/3/3
 *  desc   : 我的模块viewModel
 */
class MineViewModel: BaseViewModel() {
    val familyName = MutableLiveData<String>()  //家庭名称
}