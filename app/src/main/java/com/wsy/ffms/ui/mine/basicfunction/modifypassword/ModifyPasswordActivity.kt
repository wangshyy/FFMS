package com.wsy.ffms.ui.mine.basicfunction.modifypassword

import android.view.View
import com.gyf.immersionbar.ktx.immersionBar
import com.wsy.ffms.R
import com.wsy.ffms.core.base.BaseVMActivity
import com.wsy.ffms.core.etx.toast
import com.wsy.ffms.databinding.AcModifyPasswordBinding
import com.wsy.ffms.widget.LoadingProgressDialog
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 *  author : wsy
 *  date   : 2023/3/3
 *  desc   : 修改密码页面
 */
class ModifyPasswordActivity : BaseVMActivity(), View.OnClickListener {
    private val binding by binding<AcModifyPasswordBinding>(R.layout.ac_modify_password)
    private val mModifyPasswordViewModel by viewModel<ModifyPasswordViewModel>()
    private var progressDialog: LoadingProgressDialog? = null
    override fun initView() {
        immersionBar {
            fitsSystemWindows(true)
            statusBarColor(R.color.white)
            statusBarDarkFont(true)
        }
        binding.apply {
            onClickListener = this@ModifyPasswordActivity
            viewModel = mModifyPasswordViewModel
        }
    }

    override fun initData() {
    }

    override fun startObserve() {
        mModifyPasswordViewModel.uiState.observe(this) {
            if (it.showProcess) {
                progressDialog?.show() ?: let {
                    progressDialog = LoadingProgressDialog.show(this, cancelable = true)
                }
            }
            it.showError?.let { error ->
                progressDialog?.dismiss()
                toast(error)
            }
            if (it.modifySuccess) {
                progressDialog?.dismiss()
                toast(resources.getString(R.string.modify_success))
                finish()
            }
            if (it.oldPwdError) {
                progressDialog?.dismiss()
                toast(resources.getString(R.string.old_pwd_error))
            }
            if (it.newPwdFormatError) {
                progressDialog?.dismiss()
                toast(resources.getString(R.string.password_format_rem))
            }
        }
    }

    override fun onClick(v: View) {
        when(v.id){
            R.id.iv_back -> finish()
        }
    }
}