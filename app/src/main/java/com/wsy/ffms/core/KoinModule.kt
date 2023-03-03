package com.wsy.ffms.core

import com.wsy.ffms.ui.login.LoginViewModel
import com.wsy.ffms.ui.mine.MineViewModel
import com.wsy.ffms.ui.mine.modifypassword.ModifyPasswordViewModel
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
}