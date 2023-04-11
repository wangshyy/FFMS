package com.wsy.ffms.ui.datastatistics

import android.graphics.Color
import android.view.View
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.google.android.material.tabs.TabLayout
import com.lxj.xpopup.XPopup
import com.lxj.xpopupext.listener.TimePickerListener
import com.lxj.xpopupext.popup.TimePickerPopup
import com.wsy.ffms.R
import com.wsy.ffms.core.base.BaseVMFragment
import com.wsy.ffms.databinding.FgDataStatisticsBinding
import com.wsy.ffms.db.AppDataBase
import com.wsy.ffms.model.bean.Title
import com.wsy.ffms.ui.MainActivity
import lecho.lib.hellocharts.model.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.*
import lecho.lib.hellocharts.model.SubcolumnValue





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
        mViewModel.chartType.value = "0"   //初始为支出图表
        binding.apply {
            viewModel = mViewModel
            onClickListener = this@DataStatisticsFragment
            title = Title(getString(R.string.data_statistics), false) {}.apply {
                textColor = Color.WHITE
                backgroundColor = requireContext().getColor(R.color.colorPrimary)
            }

            tlDate.addTab(binding.tlDate.newTab().setText(getText(R.string.monthly)), 0)
            tlDate.addTab(binding.tlDate.newTab().setText(getText(R.string.annual)), 1)
            tlDate.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
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

            tlType.addTab(binding.tlType.newTab().setText(getText(R.string.expenditure_analyse)), 0)
            tlType.addTab(binding.tlType.newTab().setText(getText(R.string.income_analyse)), 1)
            tlType.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab) {
                    when (tab.position) {
                        0 -> mViewModel.chartType.value = "0"
                        1 -> mViewModel.chartType.value = "1"
                    }
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

        mViewModel.chartType.observe(this) {
            it?.let {
                if (mViewModel.type.value == "0") {
                    //月度展示折线图
                    showLineChart()
                } else {
                    //年度展示柱状图
                    showColumnChart()
                }
            }
        }

    }

    //显示折线图
    private fun showLineChart() {
        val dataIncomeList = mViewModel.queryHalfData()
        val axisXValues: MutableList<AxisValue> = mutableListOf()
        val pointValues: MutableList<PointValue> = mutableListOf()
        dataIncomeList.forEachIndexed { index, pair ->
            axisXValues.add(AxisValue(index.toFloat()).setLabel(pair.first + getString(R.string.month)))
            pointValues.add(PointValue(index.toFloat(), pair.second))
        }

        val line =
            Line(pointValues).setColor(requireContext().getColor(R.color.colorPrimary)) //折线的颜色
        line.setHasLabelsOnlyForSelected(true);//点击数据坐标提示数据（设置了这个line.setHasLabels(true);就无效）
        val lines: MutableList<Line> = ArrayList()
        lines.add(line);
        val data = LineChartData()
        data.lines = lines

        //坐标轴
        val axisX = Axis() //X轴
        axisX.values = axisXValues;  //填充X轴的坐标名称
        axisX.textColor = requireContext().getColor(R.color.color_grey_515151)  //设置字体颜色
        data.axisXBottom = axisX; //x 轴在底部

        val axisY = Axis() //Y轴
        axisY.textColor = requireContext().getColor(R.color.color_grey_515151)  //设置字体颜色
        data.axisYLeft = axisY;  //Y轴设置在左边

        binding.lcMonthly.isInteractive = false  //设置行为属性，支持缩放、滑动以及平移
        binding.lcMonthly.lineChartData = data;
        binding.lcMonthly.visibility = View.VISIBLE;

    }

    //显示柱状图
    private fun showColumnChart() {
        val sixYearDataList = mViewModel.querySixYearData()

        val numColumns = sixYearDataList.size   //柱的个数
        val numSubColumns = 1   //每个柱的子柱个数

        //定义一个圆柱对象集合
        var columns: List<Column> = listOf()
        //子柱数据集合
        var values: List<SubcolumnValue>? = null


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