package com.wsy.ffms.ui.register

import android.view.View
import com.gyf.immersionbar.ktx.immersionBar
import com.wsy.ffms.R
import com.wsy.ffms.core.base.BaseVMActivity
import com.wsy.ffms.core.etx.toast
import com.wsy.ffms.databinding.AcRegisterBinding
import com.wsy.ffms.model.bean.Title
import com.wsy.ffms.widget.LoadingProgressDialog
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 *  author : wsy
 *  date   : 2023/3/1
 *  desc   : 注册页面
 */
class RegisterActivity : BaseVMActivity(), View.OnClickListener {
    private val mRegisterViewModel by viewModel<RegisterViewModel>()
    private val mBinding by binding<AcRegisterBinding>(R.layout.ac_register)
    private var progressDialog: LoadingProgressDialog? = null
    override fun initView() {
        immersionBar {
            fitsSystemWindows(true)  //使用该属性,必须指定状态栏颜色
            statusBarColor(R.color.white)
            statusBarDarkFont(true)
        }
        mBinding.apply {
            title = Title(intent.getStringExtra("extra_title")) { onBackPressed() }
            onClickListener = this@RegisterActivity
            viewModel = mRegisterViewModel
        }
    }

    override fun initData() {

    }

    override fun startObserve() {
        mRegisterViewModel.uiState.observe(this) {
            if (it.showProgress) {
                progressDialog?.show() ?: let {
                    progressDialog = LoadingProgressDialog.show(this, cancelable = true)
                }
            }
            it.showError?.let { error ->
                progressDialog?.dismiss()
                toast(this, error)
            }
            if (it.registerSuccess) {
                progressDialog?.dismiss()
                toast(resources.getString(R.string.register_success))
                finish()
            }
        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.iv_back -> onBackPressed()
        }
    }
}