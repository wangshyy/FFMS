package com.wsy.ffms.ui.login

import android.view.View
import com.gyf.immersionbar.ktx.immersionBar
import com.wsy.ffms.R
import com.wsy.ffms.core.base.BaseVMActivity
import com.wsy.ffms.core.etx.toast
import com.wsy.ffms.databinding.AcLoginBinding
import com.wsy.ffms.db.AppDataBase
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 *  author : wsy
 *  date   : 2023/2/27
 *  desc   : 登录页
 */
class LoginActivity : BaseVMActivity(), View.OnClickListener {
    private val mBinding by binding<AcLoginBinding>(R.layout.ac_login)
    private val mLoginViewModel by viewModel<LoginViewModel>()
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
            mLoginViewModel.password.set(it.password)
        }
    }

    override fun initData() {

    }

    override fun startObserve() {
        mLoginViewModel.uiState.observe(this) {
            it.showError?.let { error ->
                toast(this, error)
            }
            if (it.loginSuccess) {
                toast(resources.getString(R.string.login_success))
            }
        }
    }

    override fun onClick(p0: View?) {

    }
}