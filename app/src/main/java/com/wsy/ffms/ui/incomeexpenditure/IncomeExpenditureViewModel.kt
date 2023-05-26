package com.wsy.ffms.ui.incomeexpenditure

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.wsy.ffms.R
import com.wsy.ffms.core.base.BaseViewModel
import com.wsy.ffms.db.AppDataBase
import com.wsy.ffms.db.banner.Banner
import com.wsy.ffms.db.expenditure.Expenditure
import com.wsy.ffms.db.income.Income
import com.wsy.ffms.util.TimeUnit
import java.text.SimpleDateFormat
import java.util.*

/**
 *  author : wsy
 *  date   : 2023/3/31
 *  desc   : 收入支出模块ViewModel
 */
class IncomeExpenditureViewModel(val context: Context) : BaseViewModel() {
    private val _uiState = MutableLiveData<IncomeExpenditureUiModel>()
    val uiState: LiveData<IncomeExpenditureUiModel>
        get() = _uiState

    val date = MutableLiveData<String>()  // 日期
    val type = MutableLiveData<String>("日")  // 日期类型

    // 根据日期类型获取收入列表
    fun getIncomeListByType() {
        launchOnUI {
            var list: List<Income>? = listOf()
            when (type.value) {
                // 日
                context.getString(R.string.day) -> {
                    val calendar = TimeUnit.parseDate(date.value!!, "yyyy-MM-dd")
                    list = AppDataBase.instance.getIncomeDao().queryAllByYearMonthDay(
                        calendar.get(Calendar.YEAR).toString(),
                        (calendar.get(Calendar.MONTH) + 1).toString(),
                        calendar.get(Calendar.DAY_OF_MONTH).toString()
                    )
                }
                // 月
                context.getString(R.string.month) -> {
                    val calendar = TimeUnit.parseDate(date.value!!, "yyyy-MM")
                    list = AppDataBase.instance.getIncomeDao().queryAllByYearMonth(
                        calendar.get(Calendar.YEAR).toString(),
                        (calendar.get(Calendar.MONTH) + 1).toString()
                    )
                }
            }
            emitUiState(showIncomeList = list)
        }
    }

    // 根据日期类型获取支出列表
    fun getExpenditureListByType() {
        launchOnUI {
            var list: List<Expenditure>? = listOf()
            when (type.value) {
                // 日
                context.getString(R.string.day) -> {
                    val calendar = TimeUnit.parseDate(date.value!!, "yyyy-MM-dd")
                    list = AppDataBase.instance.getExpenditureDao().queryAllByYearMonthDay(
                        calendar.get(Calendar.YEAR).toString(),
                        (calendar.get(Calendar.MONTH) + 1).toString(),
                        calendar.get(Calendar.DAY_OF_MONTH).toString()
                    )
                }
                // 月
                context.getString(R.string.month) -> {
                    val calendar = TimeUnit.parseDate(date.value!!, "yyyy-MM")
                    list = AppDataBase.instance.getExpenditureDao().queryAllByYearMonth(
                        calendar.get(Calendar.YEAR).toString(),
                        (calendar.get(Calendar.MONTH) + 1).toString()
                    )
                }
            }
            emitUiState(showExpenditureList = list)
        }
    }


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
            IncomeExpenditureUiModel(
                showProcess,
                showError,
                showIncomeList,
                showExpenditureList,
                deleteSuccess
            )
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