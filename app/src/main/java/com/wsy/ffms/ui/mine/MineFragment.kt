package com.wsy.ffms.ui.mine

import android.view.View
import com.lxj.xpopup.XPopup
import com.wsy.ffms.R
import com.wsy.ffms.core.base.BaseVMFragment
import com.wsy.ffms.core.etx.startKtxActivity
import com.wsy.ffms.databinding.FgMineBinding
import com.wsy.ffms.db.AppDataBase
import com.wsy.ffms.helper.UserHelper
import com.wsy.ffms.ui.mine.modifypassword.ModifyPasswordActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 *  author : wsy
 *  date   : 2023/3/2
 *  desc   : 我的模块
 */
class MineFragment : BaseVMFragment<FgMineBinding>(R.layout.fg_mine), View.OnClickListener {
    private val mMineViewModel by viewModel<MineViewModel>()
    override fun initView() {
        binding.apply {
            onClickListener = this@MineFragment
            viewModel = mMineViewModel
        }
    }

    override fun initData() {
        //获取当前登录的user
        val user = UserHelper.instance.getUser()
        mMineViewModel.familyName.value = user.userName
    }

    override fun startObserve() {
    }

    override fun onClick(v: View) {
        when (v.id) {
            //修改密码
            R.id.tv_modify_pwd -> startKtxActivity<ModifyPasswordActivity>()
            //登出
            R.id.tv_logout -> {
                XPopup.Builder(requireContext())
                    .isDestroyOnDismiss(true)
                    .asConfirm(
                        resources.getString(R.string.logout_reminder),
                        "",
                        resources.getString(R.string.common_cancel),
                        resources.getString(R.string.common_confirm),
                        {
                            AppDataBase.instance.getUserDao().userReset()
                            requireActivity().finish()
                        },
                        {}, false, R.layout.dialog_my_confim_popup
                    ).show()
            }
        }
    }
}