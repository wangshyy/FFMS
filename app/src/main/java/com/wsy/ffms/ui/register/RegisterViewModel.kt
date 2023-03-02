package com.wsy.ffms.ui.register

import android.content.Context
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.wsy.ffms.R
import com.wsy.ffms.core.base.BaseViewModel
import com.wsy.ffms.db.AppDataBase
import com.wsy.ffms.db.user.User
import com.wsy.ffms.ui.login.LoginViewModel
import com.wsy.ffms.util.RegexUtils
import java.lang.Exception

/**
 *  author : wsy
 *  date   : 2023/3/1
 *  desc   : 注册页面viewModel
 */
class RegisterViewModel(private val context: Context) : BaseViewModel() {
    private val _uiState = MutableLiveData<RegisterUiModel>()
    val uiState: LiveData<RegisterUiModel>
        get() = _uiState

    val familyName = ObservableField("")
    val password = ObservableField("")

    fun register() {
        launchOnUI {
            emitUiState(showProgress = true)
            if (familyName.get().isNullOrEmpty() or password.get().isNullOrEmpty()) {
                emitUiState(showError = context.getString(R.string.register_hint))
                return@launchOnUI
            }
            if (AppDataBase.instance.getUserDao().getUserByName(familyName.get()!!) != null) {
                emitUiState(showError = context.getString(R.string.user_already_exists))
                return@launchOnUI
            }
            if (!RegexUtils.validatePassword(password.get())) {
                emitUiState(showError = context.getString(R.string.password_format_rem))
                return@launchOnUI
            }

            try {
                AppDataBase.instance.getUserDao()
                    .insert(User(null, familyName.get(), password.get()))
                emitUiState(registerSuccess = true)
            } catch (e: Exception) {
                emitUiState(showError = e.message)
            }
        }
    }

    private fun emitUiState(
        showProgress: Boolean = false,
        showError: String? = null,
        registerSuccess: Boolean = false,
        enableLoginButton: Boolean = false
    ) {
        val uiModel = RegisterUiModel(showProgress, showError, registerSuccess, enableLoginButton)
        _uiState.value = uiModel
    }

    data class RegisterUiModel(
        val showProgress: Boolean,
        val showError: String?,
        val registerSuccess: Boolean,
        val enableLoginButton: Boolean
    )
}