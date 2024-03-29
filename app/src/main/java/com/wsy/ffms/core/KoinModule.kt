package com.wsy.ffms.core

import com.wsy.ffms.ui.datastatistics.DataStatisticsViewModel
import com.wsy.ffms.ui.home.HomeViewModel
import com.wsy.ffms.ui.incomeexpenditure.IncomeExpenditureViewModel
import com.wsy.ffms.ui.incomeexpenditure.add.AddIncomeExpenditureViewModel
import com.wsy.ffms.ui.login.LoginViewModel
import com.wsy.ffms.ui.mine.MineViewModel
import com.wsy.ffms.ui.mine.basicfunction.modifypassword.ModifyPasswordViewModel
import com.wsy.ffms.ui.mine.systemcofig.SystemConfigViewModel
import com.wsy.ffms.ui.register.RegisterViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 *  author : wsy
 *  date   : 2023/2/27
 *  desc   : koin依赖注入框架module
 */

val viewModelModule = module {
    viewModel { LoginViewModel(androidContext()) }
    viewModel { RegisterViewModel(androidContext()) }
    viewModel { MineViewModel() }
    viewModel { ModifyPasswordViewModel() }
    viewModel { SystemConfigViewModel() }
    viewModel { AddIncomeExpenditureViewModel(androidContext()) }
    viewModel { IncomeExpenditureViewModel(androidContext()) }
    viewModel { DataStatisticsViewModel() }
    viewModel { HomeViewModel(androidContext()) }
}