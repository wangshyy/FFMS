package com.wsy.ffms.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.wsy.ffms.core.base.BaseViewModel
import com.wsy.ffms.db.AppDataBase
import com.wsy.ffms.db.banner.Banner
import com.wsy.ffms.util.TimeUnit
import java.util.*

/**
 *  author : wsy
 *  date   : 2023/4/29
 *  desc   : 首页viewModel
 */
class HomeViewModel : BaseViewModel() {
    private var _uiState = MutableLiveData<HomeUiModel>()
    val uiState: LiveData<HomeUiModel>
        get() = _uiState

    val budgetPercent = MutableLiveData<String>()   // 预算百分比
    val remainingBudget = MutableLiveData<String>() // 剩余预算
    val budget = MutableLiveData<String>() // 总预算
    val expenditure = MutableLiveData<String>() //支出

    val budgetList = mutableListOf<String>()   // 预算列表

    val getResult = MutableLiveData(Pair(first = false, second = false))    // 预算，支出获取结果

    val isExcess  = MutableLiveData<Boolean>(false) // 是否超额


    //获取轮播图列表
    fun getBannerList() {
        emitUiState(showProgress = true)
        launchOnUI {
            val bannerList = AppDataBase.instance.getBannerDao().queryAllBanner()
            emitUiState(showBannerList = bannerList)
        }
    }

    // 初始化预算列表
    fun intBudgetList() {
        for (i in 500..100000 step 500) {
            budgetList.add("$i")
        }
    }

    //获取预算
    fun getBudgetAmount() {
        launchOnUI {
            val budget = AppDataBase.instance.getBudgetDao().queryBudget()
            if(budget?.isNotEmpty() == true){
                this@HomeViewModel.budget.value = budget[0].budgetValue
            }
        }
        getResult.value = Pair(true, getResult.value?.second!!)
    }

    //获取当月支出金额
    fun getExpenditureAmount() {
        val year = Calendar.getInstance().get(Calendar.YEAR).toString()
        val month = (Calendar.getInstance().get(Calendar.MONTH) + 1).toString()
        var amount = 0
        launchOnUI {
            val list = AppDataBase.instance.getExpenditureDao().queryAllByYearMonth(year, month)
            list?.forEach {
                amount += it.amount?.toInt()!!
            }
            expenditure.value = amount.toString()
        }
        getResult.value = Pair(getResult.value?.first!!, true)
    }

    private fun emitUiState(
        showProgress: Boolean = false,
        showError: String? = null,
        showBannerList: List<Banner>? = null
    ) {
        val uiState =
            HomeUiModel(
                showProgress,
                showError,
                showBannerList
            )
        _uiState.value = uiState
    }

    data class HomeUiModel(
        val showProgress: Boolean,
        val showError: String?,
        val showBannerList: List<Banner>?
    )
}