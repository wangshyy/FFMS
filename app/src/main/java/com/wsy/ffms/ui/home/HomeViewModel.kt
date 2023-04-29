package com.wsy.ffms.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.wsy.ffms.core.base.BaseViewModel
import com.wsy.ffms.db.AppDataBase
import com.wsy.ffms.db.banner.Banner

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

    //获取轮播图列表
    fun getBannerList() {
        emitUiState(showProgress = true)
        launchOnUI {
            val bannerList = AppDataBase.instance.getBannerDao().queryAllBanner()
            emitUiState(showBannerList = bannerList)
        }
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