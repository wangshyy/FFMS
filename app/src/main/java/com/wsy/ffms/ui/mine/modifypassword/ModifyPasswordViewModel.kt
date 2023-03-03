package com.wsy.ffms.ui.mine.modifypassword

import android.content.Context
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.wsy.ffms.core.base.BaseViewModel
import com.wsy.ffms.db.AppDataBase
import com.wsy.ffms.helper.UserHelper
import com.wsy.ffms.util.RegexUtils

/**
 *  author : wsy
 *  date   : 2023/3/3
 *  desc   : 修改密码页面viewModel
 */
class ModifyPasswordViewModel : BaseViewModel() {

    private val _uiState = MutableLiveData<ModifyPwdUiModel>()
    val uiState: MutableLiveData<ModifyPwdUiModel>
        get() = _uiState
    val oldPwd = ObservableField("")    //旧密码
    val newPwd = ObservableField("")    //新密码

    fun modify() {
        launchOnUI {
            emitUiState(showProcess = true)
            val user = UserHelper.instance.getUser()
            if (oldPwd.get() != user.password) {
                emitUiState(oldPwdError = true)
                return@launchOnUI
            }
            if (!RegexUtils.validatePassword(newPwd.get())) {
                emitUiState(newPwdFormatError = true)
                return@launchOnUI
            }

            //密码重置
            user.password = newPwd.get()
            //userHelper密码更新
            UserHelper.instance.setUser(user)
            //数据库用户密码更新
            try {
                AppDataBase.instance.getUserDao().update(user)
                emitUiState(modifySuccess = true)
            } catch (e: Exception) {
                emitUiState(showError = e.message)
            }
        }
    }

    private fun emitUiState(
        showProcess: Boolean = false,
        showError: String? = null,
        modifySuccess: Boolean = false,
        oldPwdError: Boolean = false,
        newPwdFormatError: Boolean = false
    ) {
        val uiModel =
            ModifyPwdUiModel(showProcess, showError, modifySuccess, oldPwdError, newPwdFormatError)
        _uiState.value = uiModel
    }

    data class ModifyPwdUiModel(
        val showProcess: Boolean,
        val showError: String?,
        val modifySuccess: Boolean,
        val oldPwdError: Boolean,
        val newPwdFormatError: Boolean
    )
}