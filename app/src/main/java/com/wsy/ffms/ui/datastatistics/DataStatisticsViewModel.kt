package com.wsy.ffms.ui.datastatistics

import androidx.lifecycle.MutableLiveData
import com.wsy.ffms.core.base.BaseViewModel
import com.wsy.ffms.db.AppDataBase
import com.wsy.ffms.db.expenditure.Expenditure
import com.wsy.ffms.db.income.Income
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

        val currentDateYear = Calendar.getInstance().get(Calendar.YEAR)
        val currentDateMonth = Calendar.getInstance().get(Calendar.MONTH) + 1
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
                var amout = 0
                list?.forEach {
                    amout += when (chartType.value) {
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
                listCurrent.add(Pair(i.toString(), amout.toFloat()))
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
                var amout = 0
                list?.forEach {
                    amout += when (chartType.value) {
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
                listCurrent.add(Pair(i.toString(), amout.toFloat()))
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
                var amout = 0
                list?.forEach {
                    amout += when (chartType.value) {
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
                listCurrent.add(Pair(i.toString(), amout.toFloat()))
            }
            return listCurrent
        }
    }

    //查询近六年的收入支出情况
    fun querySixYearData(): List<Pair<String, Float>> {
        val currentDateYear = Calendar.getInstance().get(Calendar.YEAR)
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
            var amout = 0
            list?.forEach {
                amout += when (chartType.value) {
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
            sixYearDataList.add(Pair(i.toString(), amout.toFloat()))
        }
        return sixYearDataList
    }
}