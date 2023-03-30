package com.wsy.ffms.ui.incomeexpenditure.add

import android.graphics.Color
import android.view.View
import android.widget.TimePicker
import com.gyf.immersionbar.ktx.immersionBar
import com.lxj.xpopup.XPopup
import com.lxj.xpopupext.listener.TimePickerListener
import com.lxj.xpopupext.popup.TimePickerPopup
import com.wsy.ffms.R
import com.wsy.ffms.core.base.BaseVMActivity
import com.wsy.ffms.core.etx.toast
import com.wsy.ffms.databinding.AcAddIncomeExpenditureBinding
import com.wsy.ffms.model.bean.Title
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.*

/**
 *  author : wsy
 *  date   : 2023/3/28
 *  desc   : 新增收入支出页面
 */
class AddIncomeExpenditureActivity : BaseVMActivity(), View.OnClickListener {
    private val mBinding by binding<AcAddIncomeExpenditureBinding>(R.layout.ac_add_income_expenditure)
    private val mViewModel by viewModel<AddIncomeExpenditureViewModel>()
    override fun initView() {
        immersionBar {
            fitsSystemWindows(true)
            statusBarColor(R.color.white)
            statusBarDarkFont(true)
        }
        mBinding.apply {
            onClickListener = this@AddIncomeExpenditureActivity
            title = Title(getString(R.string.common_add), true) { onBackPressed() }
            viewModel = mViewModel
            isIncome = false //初始为支出
        }
    }

    override fun initData() {
        mViewModel.type.value = "0"
        mViewModel.typeLabel.value = getString(R.string.expenditure)
    }

    override fun startObserve() {

        mViewModel.uiState.observe(this) {
            it.showSuccess?.let { list ->
                val typeList: MutableList<String> = mutableListOf()
                list.forEach { bean ->
                    typeList.add(bean.content!!)
                }
                XPopup.Builder(this)
                    .hasShadowBg(false)//去掉半透明背景
                    .atView(mBinding.tvIETypeType)//添加依附的view
                    .asAttachList(
                        typeList.toTypedArray(), null
                    ) { _, label ->
                        if (it.isFamilyMember) mViewModel.familyMember.value = label
                        else mViewModel.eIType.value = label
                    }.show()
            }
            it.showError?.let { error ->
                toast(error)
            }
            if (it.addSuccess){
                toast(getString(R.string.add_success))
                finish()
            }
        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            //选择类型
            R.id.tv_type -> {
                XPopup.Builder(this)
                    .hasShadowBg(false)//去掉半透明背景
                    .atView(mBinding.tvType)//添加依附的view
                    .asAttachList(
                        arrayOf(
                            getString(R.string.expenditure),
                            getString(R.string.income)
                        ), null
                    ) { position, label ->
                        if (position.toString() == mViewModel.type.value) return@asAttachList   //相同是不做任何操作
                        mBinding.isIncome = position == 1
                        mViewModel.eIType.value = null  //清空消费收入类型
                        mViewModel.familyMember.value = null  //清空家庭成员
                        mViewModel.type.value = position.toString()
                        mViewModel.typeLabel.value = label
                    }.show()
            }

            R.id.tv_i_e_type_type -> mViewModel.getTypeList()

            R.id.tv_date -> {
                val format = SimpleDateFormat("yyyy-MM-dd")
                val date = Calendar.getInstance()
                val popup: TimePickerPopup = TimePickerPopup(this)
                    .setMode(TimePickerPopup.Mode.YMD)
                    .setDefaultDate(date)
                    .setDateRang(null, date)
                    .setTimePickerListener(object : TimePickerListener {
                        override fun onTimeChanged(date: Date) {
                            //时间改变
                        }

                        override fun onTimeConfirm(date: Date, view: View) {
                            //点击确认时间
                            mViewModel.date.value = format.format(date)
                        }
                    })
                XPopup.Builder(this)
                    .asCustom(popup)
                    .show()
            }
        }
    }
}