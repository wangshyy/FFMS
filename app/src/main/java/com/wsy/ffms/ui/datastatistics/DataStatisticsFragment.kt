package com.wsy.ffms.ui.datastatistics

import android.graphics.Color
import android.view.View
import com.google.android.material.tabs.TabLayout
import com.lxj.xpopup.XPopup
import com.lxj.xpopupext.listener.TimePickerListener
import com.lxj.xpopupext.popup.TimePickerPopup
import com.wsy.ffms.R
import com.wsy.ffms.core.base.BaseVMFragment
import com.wsy.ffms.databinding.FgDataStatisticsBinding
import com.wsy.ffms.model.bean.Title
import com.wsy.ffms.ui.MainActivity
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.*

/**
 *  author : wsy
 *  date   : 2023/3/2
 *  desc   : 数据分析模块
 */
class DataStatisticsFragment :
    BaseVMFragment<FgDataStatisticsBinding>(R.layout.fg_data_statistics), View.OnClickListener {

    private val mViewModel by viewModel<DataStatisticsViewModel>()

    override fun initView() {
        MainActivity.setStatusBarHeight(binding.flStatusBar, requireActivity())
        mViewModel.type.value = "0"   //初始为月度
        binding.apply {
            viewModel = mViewModel
            onClickListener = this@DataStatisticsFragment
            title = Title(getString(R.string.data_statistics), false) {}.apply {
                textColor = Color.WHITE
                backgroundColor = requireContext().getColor(R.color.colorPrimary)
            }

            tabLayout.addTab(binding.tabLayout.newTab().setText(getText(R.string.monthly)), 0)
            tabLayout.addTab(binding.tabLayout.newTab().setText(getText(R.string.annual)), 1)
            tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab) {
                    when (tab.position) {
                        0 -> mViewModel.type.value = "0"
                        1 -> mViewModel.type.value = "1"
                    }
                    mViewModel.queryAll()
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {
                }

                override fun onTabReselected(tab: TabLayout.Tab?) {
                }

            })
        }
    }

    override fun initData() {

    }

    override fun startObserve() {

        mViewModel.type.observe(this) {
            when (it) {
                //月度
                "0" -> {
                    binding.isMonthly = true
                    mViewModel.datePattern.value = "yyyy年-MM月"
                }
                //年度
                "1" -> {
                    binding.isMonthly = false
                    mViewModel.datePattern.value = "yyyy年"
                }
            }
        }
        mViewModel.datePattern.observe(this) {
            mViewModel.date.value =
                SimpleDateFormat(mViewModel.datePattern.value).format(
                    Date.from(
                        Calendar.getInstance().toInstant()
                    )
                )
        }
        mViewModel.expenditureList.observe(this) {
            it?.let {
                var amount = 0
                it.forEach { expenditure ->
                    amount += expenditure.amount?.toInt()!!
                }
                mViewModel.expenditureAmount.value = amount.toString()
            }
        }
        mViewModel.incomeList.observe(this) {
            it?.let {
                var amount = 0
                it.forEach { income ->
                    amount += income.amount?.toInt()!!
                }
                mViewModel.incomeAmount.value = amount.toString()
            }
        }
        mViewModel.date.observe(this) {
            it?.let {
                mViewModel.queryAll()
            }
        }

    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.tv_date -> {
                val date = Calendar.getInstance()
                val format = SimpleDateFormat(mViewModel.datePattern.value)
                val popup: TimePickerPopup = TimePickerPopup(requireContext())
                    .setMode(if (binding.isMonthly) TimePickerPopup.Mode.YM else TimePickerPopup.Mode.Y)
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
                XPopup.Builder(requireContext())
                    .asCustom(popup)
                    .show()
            }
        }
    }
}