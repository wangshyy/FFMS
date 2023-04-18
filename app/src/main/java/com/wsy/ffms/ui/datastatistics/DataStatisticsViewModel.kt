package com.wsy.ffms.ui.datastatistics

import androidx.lifecycle.MutableLiveData
import com.github.mikephil.charting.data.BarEntry
import com.wsy.ffms.core.base.BaseViewModel
import com.wsy.ffms.db.AppDataBase
import com.wsy.ffms.db.consumptiontype.ConsumptionType
import com.wsy.ffms.db.counttype.CountType
import com.wsy.ffms.db.expenditure.Expenditure
import com.wsy.ffms.db.income.Income
import com.wsy.ffms.db.incometype.IncomeType
import com.wsy.ffms.util.TimeUnit
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

/**
 *  author : wsy
 *  date   : 2023/4/1
 *  desc   : 数据统计模块ViewModel
 */
class DataStatisticsViewModel : BaseViewModel() {
    val type = MutableLiveData<String>()    //页面类型 0->月度 1->年度
    val chartType = MutableLiveData<String>()   //图表类型 0->支出 1->收入
    val date = MutableLiveData<String>()    //日期
    val datePattern = MutableLiveData<String>()  //日期格式

    val expenditureList = MutableLiveData<List<Expenditure>>()  //支出列表
    val expenditureAmount = MutableLiveData<String>()   //支出总额

    val incomeList = MutableLiveData<List<Income>>()  //收入列表
    val incomeAmount = MutableLiveData<String>()   //收入总额

    val centerType = MutableLiveData<String>()  //饼状图中心位置文本
    val centerTypeValue = MutableLiveData<String>()  //饼状图中心位置类型占比

    //获取支出列表
    fun queryAll() {
        val calendar = TimeUnit.parseDate(date.value!!, datePattern.value!!)
        when (type.value) {
            "0" -> {
                val x = calendar.get(Calendar.YEAR).toString()
                val y = (calendar.get(Calendar.MONTH) + 1).toString()
                expenditureList.value = AppDataBase.instance.getExpenditureDao()
                    .queryAllByYearMonth(
                        calendar.get(Calendar.YEAR).toString(),
                        (calendar.get(Calendar.MONTH) + 1).toString()
                    )
                incomeList.value = AppDataBase.instance.getIncomeDao()
                    .queryAllByYearMonth(
                        calendar.get(Calendar.YEAR).toString(),
                        (calendar.get(Calendar.MONTH) + 1).toString()
                    )
            }
            "1" -> {
                expenditureList.value = AppDataBase.instance.getExpenditureDao()
                    .queryAllByYear(calendar.get(Calendar.YEAR).toString())
                incomeList.value = AppDataBase.instance.getIncomeDao()
                    .queryAllByYear(calendar.get(Calendar.YEAR).toString())
            }
        }
    }


    //查询半年内的收入支出情况
    fun queryHalfData(): List<Pair<String, Float>> {

        val currentDateYear =
            TimeUnit.parseDate(date.value!!, datePattern.value!!).get(Calendar.YEAR)
        val currentDateMonth =
            TimeUnit.parseDate(date.value!!, datePattern.value!!).get(Calendar.MONTH) + 1
        //当前年查询数据个数会存在小于6的情况，此时需要查询前一年的数据
        if (currentDateMonth < 6) {
            val listCurrent: MutableList<Pair<String, Float>> = mutableListOf()

            for (i in (12 - (6 - currentDateMonth) + 1)..12) {
                val list = when (chartType.value) {
                    //支出
                    "0" -> {
                        AppDataBase.instance.getExpenditureDao()
                            .queryAllByYearMonth((currentDateYear - 1).toString(), i.toString())
                    }
                    //收入
                    else -> {
                        AppDataBase.instance.getIncomeDao()
                            .queryAllByYearMonth((currentDateYear - 1).toString(), i.toString())
                    }
                }
                var amount = 0
                list?.forEach {
                    amount += when (chartType.value) {
                        //支出
                        "0" -> {
                            (it as Expenditure).amount?.toInt()!!
                        }
                        //收入
                        else -> {
                            (it as Income).amount?.toInt()!!
                        }
                    }
                }
                listCurrent.add(Pair(i.toString(), amount.toFloat()))
            }
            for (i in 1..currentDateMonth) {
                val list = when (chartType.value) {
                    //支出
                    "0" -> {
                        AppDataBase.instance.getExpenditureDao()
                            .queryAllByYearMonth(currentDateYear.toString(), i.toString())
                    }
                    //收入
                    else -> {
                        AppDataBase.instance.getIncomeDao()
                            .queryAllByYearMonth(currentDateYear.toString(), i.toString())
                    }
                }
                var amount = 0
                list?.forEach {
                    amount += when (chartType.value) {
                        //支出
                        "0" -> {
                            (it as Expenditure).amount?.toInt()!!
                        }
                        //收入
                        else -> {
                            (it as Income).amount?.toInt()!!
                        }
                    }
                }
                listCurrent.add(Pair(i.toString(), amount.toFloat()))
            }
            return listCurrent
        } else {
            val listCurrent: MutableList<Pair<String, Float>> = mutableListOf()
            for (i in (currentDateMonth - 6 + 1)..currentDateMonth) {
                val list = when (chartType.value) {
                    //支出
                    "0" -> {
                        AppDataBase.instance.getExpenditureDao()
                            .queryAllByYearMonth(currentDateYear.toString(), i.toString())
                    }
                    //收入
                    else -> {
                        AppDataBase.instance.getIncomeDao()
                            .queryAllByYearMonth(currentDateYear.toString(), i.toString())
                    }
                }
                var amount = 0
                list?.forEach {
                    amount += when (chartType.value) {
                        //支出
                        "0" -> {
                            (it as Expenditure).amount?.toInt()!!
                        }
                        //收入
                        else -> {
                            (it as Income).amount?.toInt()!!
                        }
                    }
                }
                listCurrent.add(Pair(i.toString(), amount.toFloat()))
            }
            return listCurrent
        }
    }

    //查询近六年的收入支出情况
    fun querySixYearData(): List<Pair<String, Float>> {
        val currentDateYear =
            TimeUnit.parseDate(date.value!!, datePattern.value!!).get(Calendar.YEAR)
        val sixYearDataList: MutableList<Pair<String, Float>> = mutableListOf()

        for (i in (currentDateYear - 5)..currentDateYear) {
            val list = when (chartType.value) {
                //支出
                "0" -> {
                    AppDataBase.instance.getExpenditureDao()
                        .queryAllByYear(i.toString())
                }
                //收入
                else -> {
                    AppDataBase.instance.getIncomeDao()
                        .queryAllByYear(i.toString())
                }
            }
            var amount = 0
            list?.forEach {
                amount += when (chartType.value) {
                    //支出
                    "0" -> {
                        (it as Expenditure).amount?.toInt()!!
                    }
                    //收入
                    else -> {
                        (it as Income).amount?.toInt()!!
                    }
                }
            }
            sixYearDataList.add(Pair(i.toString(), amount.toFloat()))
        }
        return sixYearDataList
    }

    //查询本月度支出收入排行
    fun queryRankMonthly(): List<Pair<String, Float>> {
        val currentDateYear =
            TimeUnit.parseDate(date.value!!, datePattern.value!!).get(Calendar.YEAR).toString()
        val currentDateMonth =
            (TimeUnit.parseDate(date.value!!, datePattern.value!!)
                .get(Calendar.MONTH) + 1).toString()

        val rankList: MutableList<Pair<String, Float>> = mutableListOf()

        val typeList =
            if (chartType.value == "0")
            //支出
                AppDataBase.instance.getConsumptionTypeDao().queryAllConsumptionType()
            else
            //收入
                AppDataBase.instance.getIncomeTypeDao().queryAllIncomeType()
        val list: MutableList<Pair<String, Int>> = mutableListOf()
        typeList?.let {
            it.forEachIndexed { _, type ->
                val amountList =
                    if (chartType.value == "0")
                        AppDataBase.instance.getExpenditureDao().queryAllByMonthAndType(
                            currentDateYear,
                            currentDateMonth,
                            (type as ConsumptionType).typeName.toString()
                        )
                    else
                        AppDataBase.instance.getIncomeDao().queryAllByMonthAndType(
                            currentDateYear,
                            currentDateMonth,
                            (type as IncomeType).typeName.toString()
                        )
                var amount = 0
                amountList?.forEach { any ->
                    amount += if (chartType.value == "0") (any as Expenditure).amount?.toInt()!!
                    else (any as Income).amount?.toInt()!!
                }
                list.add(
                    Pair(
                        if (chartType.value == "0") (type as ConsumptionType).typeName!! else (type as IncomeType).typeName!!,
                        amount
                    )
                )
            }
        }
        list.sortByDescending { pair -> pair.second }
        list.forEach { pair ->
            rankList.add(Pair(pair.first, pair.second.toFloat()))
        }
        return rankList
    }

    //查询本年度支出收入排行
    fun queryRankAnnual(): List<Pair<String, Float>> {
        val currentDateYear =
            TimeUnit.parseDate(date.value!!, datePattern.value!!).get(Calendar.YEAR).toString()

        val rankList: MutableList<Pair<String, Float>> = mutableListOf()

        val typeList =
            if (chartType.value == "0")
            //支出
                AppDataBase.instance.getConsumptionTypeDao().queryAllConsumptionType()
            else
            //收入
                AppDataBase.instance.getIncomeTypeDao().queryAllIncomeType()
        val list: MutableList<Pair<String, Int>> = mutableListOf()
        typeList?.let {
            it.forEachIndexed { index, type ->
                val amountList =
                    if (chartType.value == "0")
                        AppDataBase.instance.getExpenditureDao().queryAllByYearAndType(
                            currentDateYear,
                            (type as ConsumptionType).typeName.toString()
                        )
                    else
                        AppDataBase.instance.getIncomeDao().queryAllByYearAndType(
                            currentDateYear,
                            (type as IncomeType).typeName.toString()
                        )
                var amount = 0
                amountList?.forEach { any ->
                    amount += if (chartType.value == "0") (any as Expenditure).amount?.toInt()!!
                    else (any as Income).amount?.toInt()!!
                }
                if (amount != 0) {
                    if (index > 5) {
                        amount += list[5].second
                        list.removeAt(5)
                    }
                    list.add(
                        Pair(
                            if (chartType.value == "0") (type as ConsumptionType).typeName!! else (type as IncomeType).typeName!!,
                            amount
                        )
                    )
                }
            }
        }
        list.sortByDescending { pair -> pair.second }
        list.forEach { pair ->
            rankList.add(Pair(pair.first, pair.second.toFloat()))
        }
        return rankList
    }
}