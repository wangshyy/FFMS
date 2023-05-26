package com.wsy.ffms.ui.incomeexpenditure.expenditure

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.jeremyliao.liveeventbus.LiveEventBus
import com.lxj.xpopup.XPopup
import com.lxj.xpopupext.listener.TimePickerListener
import com.lxj.xpopupext.popup.TimePickerPopup
import com.wsy.ffms.R
import com.wsy.ffms.adapter.ExpenditureAdapter
import com.wsy.ffms.core.base.BaseVMFragment
import com.wsy.ffms.core.etx.toast
import com.wsy.ffms.databinding.FgExpenditureBinding
import com.wsy.ffms.ui.incomeexpenditure.IncomeExpenditureViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.*

/**
 *  author : wsy
 *  date   : 2023/3/10
 *  desc   : 支出页面
 */
class ExpenditureFragment : BaseVMFragment<FgExpenditureBinding>(R.layout.fg_expenditure),
    View.OnClickListener {
    private val mViewModel by viewModel<IncomeExpenditureViewModel>()
    private val mExpenditureListAdapter by lazy { ExpenditureAdapter() }
    private lateinit var mEmptyView: View
    override fun initView() {
        binding.apply {
            viewModel = mViewModel
            onClickListener = this@ExpenditureFragment
            rvExpenditure.apply {
                adapter = mExpenditureListAdapter
                layoutManager = LinearLayoutManager(context)
            }
        }
        //空页面
        mEmptyView =
            layoutInflater.inflate(
                R.layout.view_empty,
                binding.rvExpenditure.parent as ViewGroup,
                false
            )

        //订阅消息
        LiveEventBus.get<String>("add_type")
            .observe(this) {
                when (it) {
                    //添加成功，更新列表
                    "expenditure" -> mViewModel.getExpenditureList()
                }
            }

        mExpenditureListAdapter.deleteId.observe(this) {
            it?.let {
                mViewModel.deleteExpenditure(it)
                mExpenditureListAdapter.clearDeleteId()
            }
        }
    }

    override fun initData() {
        mViewModel.getExpenditureList()
    }

    override fun startObserve() {
        mViewModel.uiState.observe(this) {
            it.showExpenditureList?.let { list ->
                mExpenditureListAdapter.setList(list)
            }
            if (it.showExpenditureList?.size ?: 0 == 0) mExpenditureListAdapter.setEmptyView(
                mEmptyView
            )
            if (it.deleteSuccess) {
                requireActivity().toast(getString(R.string.delete_success))
                if (mViewModel.date.value.isNullOrEmpty())
                    mViewModel.getExpenditureList()
                else mViewModel.getExpenditureListByType()
            }
        }

        // 观察date数据变化
        mViewModel.date.observe(this) {
            if (!it.isNullOrEmpty()) {
                mViewModel.getExpenditureListByType()
            } else {
                mViewModel.getExpenditureList()
            }
        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            // 清空日期
            R.id.tv_del -> {
                mViewModel.date.value = ""
            }
            //选择类型
            R.id.tv_type -> {
                XPopup.Builder(requireContext())
                    .hasShadowBg(false)//去掉半透明背景
                    .atView(binding.tvType)//添加依附的view
                    .asAttachList(
                        arrayOf(
                            getString(R.string.day),
                            getString(R.string.month)
                        ), null
                    ) { position, label ->
                        if (mViewModel.type.value == label)
                        else {
                            mViewModel.type.value = label
                            mViewModel.date.value = ""
                        }
                    }.show()
            }

            R.id.tv_date -> {
                when (mViewModel.type.value) {
                    // 日
                    getString(R.string.day) -> {
                        val format = SimpleDateFormat("yyyy-MM-dd")
                        val date = Calendar.getInstance()
                        val popup: TimePickerPopup = TimePickerPopup(requireContext())
                            .setMode(TimePickerPopup.Mode.YMD)
                            .setDefaultDate(date)
                            .setDateRange(null, date)
                            .setTimePickerListener(object : TimePickerListener {
                                override fun onTimeChanged(date: Date) {
                                    //时间改变
                                }

                                override fun onTimeConfirm(date: Date, view: View) {
                                    //点击确认时间
                                    mViewModel.date.value = format.format(date)
                                }

                                override fun onCancel() {

                                }
                            })
                        XPopup.Builder(requireContext())
                            .asCustom(popup)
                            .show()
                    }
                    // 月
                    getString(R.string.month) -> {
                        val format = SimpleDateFormat("yyyy-MM")
                        val date = Calendar.getInstance()
                        val popup: TimePickerPopup = TimePickerPopup(requireContext())
                            .setMode(TimePickerPopup.Mode.YM)
                            .setDefaultDate(date)
                            .setDateRange(null, date)
                            .setTimePickerListener(object : TimePickerListener {
                                override fun onTimeChanged(date: Date) {
                                    //时间改变
                                }

                                override fun onTimeConfirm(date: Date, view: View) {
                                    //点击确认时间
                                    mViewModel.date.value = format.format(date)
                                }

                                override fun onCancel() {

                                }
                            })
                        XPopup.Builder(requireContext())
                            .asCustom(popup)
                            .show()
                    }
                }

            }
        }
    }
}