package com.wsy.ffms.ui.incomeexpenditure.add

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.wsy.ffms.core.base.BaseViewModel
import com.wsy.ffms.db.AppDataBase
import com.wsy.ffms.model.bean.SystemConfigCommonBean

/**
 *  author : wsy
 *  date   : 2023/3/28
 *  desc   : 新增收入支出页面viewModel
 */
class AddIncomeExpenditureViewModel : BaseViewModel() {
    private var _uiState = MutableLiveData<AddIEUiModel>()
    val uiState: LiveData<AddIEUiModel>
        get() = _uiState

    val type = MutableLiveData<String>() //类型 0->支出 1->收入
    val typeLabel = MutableLiveData<String>() //类型文本 支出/收入
    val amount = MutableLiveData<String>()  //金额
    val date = MutableLiveData<String>()    //日期
    val eIType = MutableLiveData<String>()  // 消费/收入类型

    //获取类型列表
    fun getTypeList() {
        launchOnUI {
            val typeList = mutableListOf<SystemConfigCommonBean>()
            when (type.value) {
                //获取消费类型列表
                "0" -> {
                    (AppDataBase.instance.getConsumptionDao().queryAllConsumptionType())?.forEach {
                        typeList.add(SystemConfigCommonBean(it.id, it.typeName))
                    }
                }
                //获取收入类型列表
                "1" -> {
                    (AppDataBase.instance.getIncomeDao().queryAllIncomeType())?.forEach {
                        typeList.add(SystemConfigCommonBean(it.id, it.typeName))
                    }
                }
            }

            emitUiState(showSuccess = typeList)
        }
    }

    private fun emitUiState(
        showProcess: Boolean = false,
        showError: String? = null,
        showSuccess: List<SystemConfigCommonBean>? = null
    ) {
        val uiState = AddIEUiModel(showProcess, showError, showSuccess)
        _uiState.value = uiState
    }

    data class AddIEUiModel(
        val showProcess: Boolean,
        val showError: String?,
        val showSuccess: List<SystemConfigCommonBean>?
    )
}