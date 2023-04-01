package com.wsy.ffms.ui.incomeexpenditure.add

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.wsy.ffms.R
import com.wsy.ffms.core.base.BaseViewModel
import com.wsy.ffms.db.AppDataBase
import com.wsy.ffms.db.expenditure.Expenditure
import com.wsy.ffms.db.income.Income
import com.wsy.ffms.model.bean.SystemConfigCommonBean
import com.wsy.ffms.util.TimeUnit
import java.util.*

/**
 *  author : wsy
 *  date   : 2023/3/28
 *  desc   : 新增收入支出页面viewModel
 */
class AddIncomeExpenditureViewModel(val context: Context) : BaseViewModel() {
    private var _uiState = MutableLiveData<AddIEUiModel>()
    val uiState: LiveData<AddIEUiModel>
        get() = _uiState

    val type = MutableLiveData<String>() //类型 0->支出 1->收入
    val typeLabel = MutableLiveData<String>() //类型文本 支出/收入
    val amount = MutableLiveData<String>()  //金额
    val date = MutableLiveData<String>()    //日期
    val eIType = MutableLiveData<String>()  // 消费/收入类型
    val familyMember = MutableLiveData<String>() //家庭成员

    //获取类型列表
    fun getTypeList() {
        launchOnUI {
            val typeList = mutableListOf<SystemConfigCommonBean>()
            when (type.value) {
                //获取消费类型列表
                "0" -> {
                    (AppDataBase.instance.getConsumptionTypeDao()
                        .queryAllConsumptionType())?.forEach {
                            typeList.add(SystemConfigCommonBean(it.id, it.typeName))
                        }
                }
                //获取收入类型列表
                "1" -> {
                    (AppDataBase.instance.getIncomeTypeDao().queryAllIncomeType())?.forEach {
                        typeList.add(SystemConfigCommonBean(it.id, it.typeName))
                    }
                }

            }
            emitUiState(showSuccess = typeList)
        }
    }

    //获取家庭成员
    fun getFamilyMemberList() {
        val list = mutableListOf<SystemConfigCommonBean>()
        (AppDataBase.instance.getFamilyMemberDao().queryAllFamilyMember())?.forEach {
            list.add(SystemConfigCommonBean(it.id, it.name))
        }
        emitUiState(showSuccess = list, isFamilyMember = true)
    }

    //新增收入/支出数据
    fun add() {
        if (date.value.isNullOrEmpty() || amount.value.isNullOrEmpty() || typeLabel.value.isNullOrEmpty()) {
            emitUiState(showError = context.getString(R.string.add_hint))
            return
        }
        if(type.value == "1" && familyMember.value.isNullOrEmpty()){
            emitUiState(showError = context.getString(R.string.add_hint))
            return
        }
        when (type.value) {
            //支出
            "0" -> {
                val expenditure = Expenditure(null)
                val calendar = TimeUnit.parseDate(date.value!!, "yyyy-MM-dd")
                expenditure.dateYear = calendar.get(Calendar.YEAR).toString()
                //显示月份 (从0开始, 实际显示要加一)
                expenditure.dateMonth = (calendar.get(Calendar.MONTH) + 1).toString()
                expenditure.dateDay = calendar.get(Calendar.DAY_OF_MONTH).toString()
                expenditure.amount = amount.value
                expenditure.expenditureType = eIType.value
                AppDataBase.instance.getExpenditureDao().insert(expenditure)
                emitUiState(addSuccess = "expenditure")
            }

            //收入
            "1" -> {
                val income = Income(null)
                val calendar = TimeUnit.parseDate(date.value!!, "yyyy-MM-dd")
                income.dateYear = calendar.get(Calendar.YEAR).toString()
                //显示月份 (从0开始, 实际显示要加一)
                income.dateMonth = (calendar.get(Calendar.MONTH) + 1).toString()
                income.dateDay = calendar.get(Calendar.DAY_OF_MONTH).toString()
                income.amount = amount.value
                income.familyMember = familyMember.value
                income.incomeType = eIType.value
                AppDataBase.instance.getIncomeDao().insert(income)
                emitUiState(addSuccess = "income")
            }
        }
    }

    private fun emitUiState(
        showProcess: Boolean = false,
        showError: String? = null,
        showSuccess: List<SystemConfigCommonBean>? = null,
        isFamilyMember: Boolean = false,
        addSuccess: String? = null,
    ) {
        val uiState = AddIEUiModel(
            showProcess,
            showError,
            showSuccess,
            isFamilyMember,
            addSuccess,
        )
        _uiState.value = uiState
    }

    data class AddIEUiModel(
        val showProcess: Boolean,
        val showError: String?,
        val showSuccess: List<SystemConfigCommonBean>?,
        val isFamilyMember: Boolean,
        val addSuccess: String?,
    )
}