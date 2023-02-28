package com.wsy.ffms.ui.login

import android.content.Context
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.wsy.ffms.R
import com.wsy.ffms.core.base.BaseViewModel
import com.wsy.ffms.db.AppDataBase
import com.wsy.ffms.db.user.User

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

    fun login() {
        launchOnUI {
            if (familyName.get().isNullOrEmpty() or password.get().isNullOrEmpty()) {
                emitUiState(showError = context.getString(R.string.login_hint))
                return@launchOnUI
            }
//            AppDataBase.instance.getUserDao().insert(User(null,familyName.get(), password.get(),false,0))
            val user = AppDataBase.instance.getUserDao().getUserByName(familyName.get()!!)
            user?.let {
                if (it.password == password.get()) emitUiState(loginSuccess = true)
                else emitUiState(showError = context.getString(R.string.password_error))
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