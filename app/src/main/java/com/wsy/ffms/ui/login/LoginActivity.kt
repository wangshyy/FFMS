package com.wsy.ffms.ui.login

import android.view.View
import com.gyf.immersionbar.ktx.immersionBar
import com.wsy.ffms.R
import com.wsy.ffms.core.base.BaseVMActivity
import com.wsy.ffms.core.etx.startKtxActivity
import com.wsy.ffms.core.etx.startKtxActivityForResult
import com.wsy.ffms.core.etx.toast
import com.wsy.ffms.databinding.AcLoginBinding
import com.wsy.ffms.db.AppDataBase
import com.wsy.ffms.helper.UserHelper
import com.wsy.ffms.ui.MainActivity
import com.wsy.ffms.ui.register.RegisterActivity
import com.wsy.ffms.widget.LoadingProgressDialog
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 *  author : wsy
 *  date   : 2023/2/27
 *  desc   : 登录页
 */
class LoginActivity : BaseVMActivity(), View.OnClickListener {
    private val mBinding by binding<AcLoginBinding>(R.layout.ac_login)
    private val mLoginViewModel by viewModel<LoginViewModel>()
    private var progressDialog: LoadingProgressDialog? = null
    override fun initView() {
        immersionBar {
            transparentBar()    //透明状态栏，不写默认透明色
        }
        mBinding.apply {
            viewModel = mLoginViewModel
            onClickListener = this@LoginActivity
        }
        val user = AppDataBase.instance.getUserDao().getActivateUser()
        user?.let {
            mLoginViewModel.familyName.set(it.userName)
            mLoginViewModel.isRemPwd.value = it.isRemPwd
            if (it.isRemPwd == true) mLoginViewModel.password.set(it.password)
        }
    }

    override fun initData() {

    }

    override fun startObserve() {
        mLoginViewModel.uiState.observe(this) {
            if (it.showProgress) {
                progressDialog?.show() ?: let {
                    progressDialog = LoadingProgressDialog.show(this, cancelable = true)
                }
            }
            it.showError?.let { error ->
                progressDialog?.dismiss()
                toast(this, error)
            }
            if (it.loginSuccess) {
                progressDialog?.dismiss()
                startKtxActivity<MainActivity>()
            }
        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            //注册
            R.id.tv_register -> startKtxActivity<RegisterActivity>(
                values = arrayListOf(
                    "extra_title" to resources.getString(R.string.register_title)
                )
            )

        }
    }
}