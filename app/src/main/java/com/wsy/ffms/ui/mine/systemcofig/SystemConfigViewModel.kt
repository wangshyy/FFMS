package com.wsy.ffms.ui.mine.systemcofig

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.wsy.ffms.core.base.BaseViewModel
import com.wsy.ffms.db.AppDataBase
import com.wsy.ffms.db.consumption.Consumption
import com.wsy.ffms.db.counttype.CountType
import com.wsy.ffms.db.income.Income
import com.wsy.ffms.model.bean.SystemConfigCommonBean

/**
 *  author : wsy
 *  date   : 2023/3/8
 *  desc   : 系统配置viewModel
 */
class SystemConfigViewModel : BaseViewModel() {
    private var _uiState = MutableLiveData<SystemConfigUiModel>()
    val uiState: LiveData<SystemConfigUiModel>
        get() = _uiState
    var pageType: String? = null //pageType：1->账户类型 2->消费类型 3->收入类型

    val type = MutableLiveData<String>() //类型

    //获取类型列表
    fun getAllType() {
        emitUiState(showProgress = true)
        launchOnUI {
            val typeList = mutableListOf<SystemConfigCommonBean>()
            when (pageType) {
                //获取账户类型
                "1" -> {
                    (AppDataBase.instance.getCountTypeDao().queryAllCountType())?.forEach {
                        typeList.add(SystemConfigCommonBean(it.id, it.typeName))
                    }
                }
                //获取消费类型
                "2" -> {
                    (AppDataBase.instance.getConsumptionDao().queryAllConsumptionType())?.forEach {
                        typeList.add(SystemConfigCommonBean(it.id, it.typeName))
                    }
                }
                //获取收入类型
                "3" -> {
                    (AppDataBase.instance.getIncomeDao().queryAllIncomeType())?.forEach {
                        typeList.add(SystemConfigCommonBean(it.id, it.typeName))
                    }
                }
            }

            emitUiState(showSuccess = typeList)
        }
    }

    //插入类型数据到数据库
    fun insertType() {
        emitUiState(showProgress = true)
        launchOnUI {
            when (pageType) {
                //插入到账户类型数据表
                "1" -> {
                    AppDataBase.instance.getCountTypeDao().insert(CountType(null, type.value))
                    emitUiState(addSuccess = true)
                }
                //插入到消费类型数据表获取
                "2" -> {
                    AppDataBase.instance.getConsumptionDao().insert(Consumption(null, type.value))
                    emitUiState(addSuccess = true)
                }
                //插入到收入类型数据表获取
                "3" -> {
                    AppDataBase.instance.getIncomeDao().insert(Income(null, type.value))
                    emitUiState(addSuccess = true)
                }
            }
        }
    }


    private fun emitUiState(
        showProgress: Boolean = false,
        showError: String? = null,
        addSuccess: Boolean = false,
        deleteSuccess: Boolean = false,
        showSuccess: List<SystemConfigCommonBean>? = null
    ) {
        val uiState =
            SystemConfigUiModel(showProgress, showError, addSuccess, deleteSuccess, showSuccess)
        _uiState.value = uiState
    }

    data class SystemConfigUiModel(
        val showProgress: Boolean,
        val showError: String?,
        val addSuccess: Boolean,
        val deleteSuccess: Boolean,
        val showSuccess: List<SystemConfigCommonBean>?
    )
}