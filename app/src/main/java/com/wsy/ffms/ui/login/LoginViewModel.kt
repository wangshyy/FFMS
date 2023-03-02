package com.wsy.ffms.ui.login

import android.content.Context
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.wsy.ffms.R
import com.wsy.ffms.core.base.BaseViewModel
import com.wsy.ffms.db.AppDataBase
import com.wsy.ffms.db.user.User
import com.wsy.ffms.helper.UserHelper

/**
 *  author : wsy
 *  date   : 2023/2/27
 *  desc   : 登录页面ViewModel
 */
class LoginViewModel(private val context: Context) : BaseViewModel() {
    private val _uiState = MutableLiveData<LoginUiModel>()
    val uiState: LiveData<LoginUiModel>
        get() = _uiState

    val familyName = ObservableField("")  //用户名
    val password = ObservableField("")  //密码
    val isRemPwd = MutableLiveData(false)  //是否记住密码

    fun login() {
        launchOnUI {
            emitUiState(showProgress = true)
            if (familyName.get().isNullOrEmpty() or password.get().isNullOrEmpty()) {
                emitUiState(showError = context.getString(R.string.login_hint))
                return@launchOnUI
            }
            val user = AppDataBase.instance.getUserDao().getUserByName(familyName.get())
            user?.let {
                if (it.password == password.get()) {
                    AppDataBase.instance.getUserDao().userReset()   //重置最近登录用户
                    it.isActivate = 1   //将该用户设为最近登录用户
                    if (it.isRemPwd != isRemPwd.value) {
                        it.isRemPwd = isRemPwd.value  //记住密码字段改变时重新赋值
                    }
                    AppDataBase.instance.getUserDao().update(it)  //更新用户信息
                    UserHelper.instance.setUser(it)   //UserHelper设置当前登录用户
                    emitUiState(loginSuccess = true)
                } else emitUiState(showError = context.getString(R.string.password_error))
            } ?: emitUiState(showError = context.getString(R.string.no_find_user_hint))
        }
    }


    private fun emitUiState(
        showProgress: Boolean = false,
        showError: String? = null,
        loginSuccess: Boolean = false,
        enableLoginButton: Boolean = false
    ) {
        val uiModel = LoginUiModel(showProgress, showError, loginSuccess, enableLoginButton)
        _uiState.value = uiModel
    }

    data class LoginUiModel(
        val showProgress: Boolean,
        val showError: String?,
        val loginSuccess: Boolean,
        val enableLoginButton: Boolean
    )
}