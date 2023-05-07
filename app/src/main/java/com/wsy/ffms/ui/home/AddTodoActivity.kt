package com.wsy.ffms.ui.home

import android.content.Intent
import android.view.View
import com.gyf.immersionbar.ktx.immersionBar
import com.lxj.xpopup.XPopup
import com.lxj.xpopupext.listener.TimePickerListener
import com.lxj.xpopupext.popup.TimePickerPopup
import com.wsy.ffms.R
import com.wsy.ffms.core.base.BaseVMActivity
import com.wsy.ffms.core.etx.toast
import com.wsy.ffms.databinding.AcAddTodoBinding
import com.wsy.ffms.model.bean.Title
import com.wsy.ffms.util.RESULT_CODE
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.*

/**
 *  author : wsy
 *  date   : 2023/5/7
 *  desc   : 新增待办事项页面
 */
class AddTodoActivity : BaseVMActivity(), View.OnClickListener {
    private val mBinding by binding<AcAddTodoBinding>(R.layout.ac_add_todo)
    private val mViewModel by viewModel<HomeViewModel>()
    override fun initView() {
        immersionBar {
            fitsSystemWindows(true)
            statusBarColor(R.color.white)
            statusBarDarkFont(true)
        }
        mBinding.apply {
            title = Title(getString(R.string.add_todo), true) { onBackPressed() }
            onClickListener = this@AddTodoActivity
            viewModel = mViewModel
        }
    }

    override fun initData() {
    }

    override fun startObserve() {
        mViewModel.uiState.observe(this) {
            if (it.addTodoSuccess) {
                toast(getString(R.string.add_success))
                val intent = Intent()
                intent.putExtra("addTodoSuccess", true)
                setResult(RESULT_CODE, intent)
                finish()
            }
            it.showError?.let { error ->
                toast(error)
            }
        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.tv_date -> {
                val format = SimpleDateFormat("yyyy-MM-dd")
                val date = Calendar.getInstance()
                val popup: TimePickerPopup = TimePickerPopup(this)
                    .setMode(TimePickerPopup.Mode.YMD)
                    .setDefaultDate(date)
                    .setDateRange(date, null)
                    .setTimePickerListener(object : TimePickerListener {
                        override fun onTimeChanged(date: Date) {
                            //时间改变
                        }

                        override fun onTimeConfirm(date: Date, view: View) {
                            //点击确认时间
                            mViewModel.todoDate.value = format.format(date)
                        }

                        override fun onCancel() {

                        }
                    })
                XPopup.Builder(this)
                    .asCustom(popup)
                    .show()
            }
        }
    }
}