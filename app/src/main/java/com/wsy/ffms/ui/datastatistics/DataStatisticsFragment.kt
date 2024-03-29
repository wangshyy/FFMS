package com.wsy.ffms.ui.datastatistics

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.View
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.ValueFormatter
import com.google.android.material.tabs.TabLayout
import com.lxj.xpopup.XPopup
import com.lxj.xpopupext.listener.TimePickerListener
import com.lxj.xpopupext.popup.TimePickerPopup
import com.wsy.ffms.R
import com.wsy.ffms.core.base.BaseVMFragment
import com.wsy.ffms.databinding.FgDataStatisticsBinding
import com.wsy.ffms.model.bean.Title
import com.wsy.ffms.ui.MainActivity
import lecho.lib.hellocharts.formatter.SimpleColumnChartValueFormatter
import lecho.lib.hellocharts.listener.ColumnChartOnValueSelectListener
import lecho.lib.hellocharts.listener.PieChartOnValueSelectListener
import lecho.lib.hellocharts.model.*
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

    @SuppressLint("SimpleDateFormat")
    override fun startObserve() {

        mViewModel.type.observe(this) {
            when (it) {
                //月度
                "0" -> {
                    binding.isMonthly = true
                    mViewModel.datePattern.value = "yyyy年-MM月"
                    mViewModel.chartType.value = mViewModel.chartType.value
                }
                //年度
                "1" -> {
                    binding.isMonthly = false
                    mViewModel.datePattern.value = "yyyy年"
                    mViewModel.chartType.value = mViewModel.chartType.value
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
                //更新图表
                mViewModel.chartType.value = mViewModel.chartType.value
            }
        }

        mViewModel.chartType.observe(this) {
            it?.let {
                if (mViewModel.type.value == "0") {
                    //月度展示趋势折线图、排行柱状图
                    showLineChart()
                    showRankColumnChart()
                } else {
                    //年度展示趋势柱状图、占比饼状图
                    showColumnChart()
                    showRankPieChart()
                }
            }
        }

    }

    //显示趋势折线图
    private fun showLineChart() {
        val dataIncomeList = mViewModel.queryHalfData()
        //x轴描述
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

        binding.lcMonthly.apply {
            lineChartData = data
            isZoomEnabled = false     //是否可以缩放
        }
    }

    //显示趋势柱状图
    private fun showColumnChart() {
        val sixYearDataList = mViewModel.querySixYearData()
        //x轴描述
        val axisValues: MutableList<AxisValue> = mutableListOf()
        sixYearDataList.forEachIndexed { index, pair ->
            axisValues.add(AxisValue(index.toFloat()).setLabel(pair.first))   //设置x轴描述
        }

        val numColumns = sixYearDataList.size   //柱的个数
        val numSubColumns = 1   //每个柱的子柱个数

        //定义一个圆柱对象集合
        val columns: MutableList<Column> = mutableListOf()

        for (i in 0 until numColumns) {
            //子柱数据集合
            val values: MutableList<SubcolumnValue> = mutableListOf()
            for (j in 0 until numSubColumns) {
                //为每一柱图添加颜色和数值
                values.add(
                    SubcolumnValue(
                        sixYearDataList[i].second,
                        requireContext().getColor(R.color.colorPrimary)
                    )
                )
            }

            //创建column对象
            val column = Column(values)
            //设置标注可以显示小数
            val chartValueFormatter = SimpleColumnChartValueFormatter(1)    //小数位数
            column.apply {
                formatter = chartValueFormatter
                setHasLabels(false)//是否有数据标注
                setHasLabelsOnlyForSelected(true)//是否点击时显示标注
            }
            columns.add(column)
        }

        //坐标轴
        val axisX = Axis() //X轴
        axisX.apply {
            setHasLines(false)  //是否显示网格线
            textColor = requireContext().getColor(R.color.color_grey_515151)  //设置字体颜色
            values = axisValues
        }
        val axisY = Axis() //Y轴
        axisY.apply {
            setHasLines(false)  //是否显示网格线
            textColor = requireContext().getColor(R.color.color_grey_515151)  //设置字体颜色
        }

        val data = ColumnChartData(columns)
        data.apply {
            axisXBottom = axisX
            axisYLeft = axisY
        }

        //给柱状图填充数据
        binding.lcAnnual.apply {
            isZoomEnabled = false     //是否可以缩放
            columnChartData = data
        }
    }

    //显示排行柱状图
    private fun showRankColumnChart() {

        val list = mViewModel.queryRankMonthly()
        //x轴描述
        val axisValues: MutableList<AxisValue> = mutableListOf()
        list.forEachIndexed { index, pair ->
            axisValues.add(AxisValue(index.toFloat()).setLabel(pair.first))   //设置x轴描述
        }

        val numColumns = list.size   //柱的个数
        val numSubColumns = 1   //每个柱的子柱个数

        //定义一个圆柱对象集合
        val columns: MutableList<Column> = mutableListOf()

        for (i in 0 until numColumns) {
            //子柱数据集合
            val values: MutableList<SubcolumnValue> = mutableListOf()
            for (j in 0 until numSubColumns) {
                //为每一柱图添加颜色和数值
                values.add(
                    SubcolumnValue(
                        list[i].second,
                        requireContext().getColor(R.color.colorPrimary)
                    )
                )
            }

            //创建column对象
            val column = Column(values)
            //设置标注可以显示小数
            val chartValueFormatter = SimpleColumnChartValueFormatter(1)    //小数位数
            column.apply {
                formatter = chartValueFormatter
                setHasLabels(false)//是否有数据标注
                setHasLabelsOnlyForSelected(true)//是否点击时显示标注
            }
            columns.add(column)
        }

        //坐标轴
        val axisX = Axis() //X轴
        axisX.apply {
            setHasLines(false)  //是否显示网格线
            textColor = requireContext().getColor(R.color.color_grey_515151)  //设置字体颜色
            values = axisValues
        }
        val axisY = Axis() //Y轴
        axisY.apply {
            setHasLines(false)  //是否显示网格线
            textColor = requireContext().getColor(R.color.color_grey_515151)  //设置字体颜色
        }

        val data = ColumnChartData(columns)
        data.apply {
            axisXBottom = axisX
            axisYLeft = axisY
            fillRatio = .5F
        }

        //给柱状图填充数据
        binding.ccRank.apply {
            isZoomEnabled = false     //是否可以缩放
            columnChartData = data
        }
    }

    //显示排行饼状图
    private fun showRankPieChart() {
        val list = mViewModel.queryRankAnnual()
        var allAmount = 0F  //总金额
        //图例颜色列表
        val colorList = arrayListOf(
            requireActivity().getColor(R.color.color_red_FF3333),
            requireActivity().getColor(R.color.colorPrimary),
            requireActivity().getColor(R.color.color_green_00CC66),
            requireActivity().getColor(R.color.color_yellow_FFFF33),
            requireActivity().getColor(R.color.color_grey_A0A0A0)
        )
        val sliceValues = mutableListOf<SliceValue>()
        list.forEachIndexed { index, pair ->
            sliceValues.add(SliceValue(pair.second, colorList[index]))
            allAmount += pair.second
        }

        val pieCharData = PieChartData()
        pieCharData.apply {
            setHasLabels(true)  //是否显示文本
            setHasLabelsOnlyForSelected(false)  //不用点击显示占的百分比
            setHasLabelsOutside(false)  //占的百分比是否显示在饼图外面
            setHasCenterCircle(true)    //是否是环形显示
            values = sliceValues    //填充数据
            centerCircleColor = Color.WHITE //设置环形中间的颜色
            centerCircleScale = 0.5f    //设置环形的大小级别
        }
        binding.pcRank.apply {
            pieChartData = pieCharData
            isValueSelectionEnabled = true    //选择饼图某一块变大
            alpha = 0.9f   //设置透明度
            circleFillRatio = 1f    //设置饼图大小
            onValueTouchListener = object : PieChartOnValueSelectListener {
                override fun onValueDeselected() {
                    mViewModel.centerType.value = ""
                    mViewModel.centerTypeValue.value = ""
                }

                override fun onValueSelected(arcIndex: Int, value: SliceValue?) {
                    mViewModel.centerType.value = list[arcIndex].first
                    mViewModel.centerTypeValue.value =
                        "${"%.1f".format((list[arcIndex].second / allAmount) * 100)}%"
                }

            }
        }
    }


    @SuppressLint("SimpleDateFormat")
    override fun onClick(v: View) {
        when (v.id) {
            R.id.tv_date -> {
                val date = Calendar.getInstance()
                val format = SimpleDateFormat(mViewModel.datePattern.value)
                val popup: TimePickerPopup = TimePickerPopup(requireContext())
                    .setMode(if (binding.isMonthly) TimePickerPopup.Mode.YM else TimePickerPopup.Mode.Y)
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