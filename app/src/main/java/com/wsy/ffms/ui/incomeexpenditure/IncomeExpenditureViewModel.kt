package com.wsy.ffms.ui.incomeexpenditure

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.wsy.ffms.core.base.BaseViewModel
import com.wsy.ffms.db.AppDataBase
import com.wsy.ffms.db.banner.Banner
import com.wsy.ffms.db.expenditure.Expenditure
import com.wsy.ffms.db.income.Income

/**
 *  author : wsy
 *  date   : 2023/3/31
 *  desc   : 收入支出模块ViewModel
 */
class IncomeExpenditureViewModel : BaseViewModel() {
    private val _uiState = MutableLiveData<IncomeExpenditureUiModel>()
    val uiState: LiveData<IncomeExpenditureUiModel>
        get() = _uiState


    //获取收入列表
    fun getIncomeList() {
        launchOnUI {
            val incomeList = AppDataBase.instance.getIncomeDao().queryAllIncome()
            emitUiState(showIncomeList = incomeList)
        }
    }

    //获取支出列表
    fun getExpenditureList() {
        launchOnUI {
            val expenditureList = AppDataBase.instance.getExpenditureDao().queryAllExpenditure()
            emitUiState(showExpenditureList = expenditureList)
        }
    }

    //删除收入
    fun deleteIncome(id: Int) {
        launchOnUI {
            AppDataBase.instance.getIncomeDao().delete(Income(id))
            emitUiState(deleteSuccess = true)
        }
    }
    //删除支出
    fun deleteExpenditure(id: Int) {
        launchOnUI {
            AppDataBase.instance.getExpenditureDao().delete(Expenditure(id))
            emitUiState(deleteSuccess = true)
        }
    }

    private fun emitUiState(
        showProcess: Boolean = false,
        showError: String? = null,
        showIncomeList: List<Income>? = null,
        showExpenditureList: List<Expenditure>? = null,
        deleteSuccess: Boolean = false,
    ) {
        val uiState =
            IncomeExpenditureUiModel(showProcess, showError, showIncomeList, showExpenditureList,deleteSuccess)
        _uiState.value = uiState
    }

    data class IncomeExpenditureUiModel(
        val showProcess: Boolean,
        val showError: String?,
        val showIncomeList: List<Income>?,
        val showExpenditureList: List<Expenditure>?,
        val deleteSuccess: Boolean,
    )
}