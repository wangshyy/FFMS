package com.wsy.ffms.ui.datastatistics

import androidx.lifecycle.MutableLiveData
import com.wsy.ffms.core.base.BaseViewModel
import com.wsy.ffms.db.AppDataBase
import com.wsy.ffms.db.expenditure.Expenditure
import com.wsy.ffms.util.TimeUnit
import java.util.*

/**
 *  author : wsy
 *  date   : 2023/4/1
 *  desc   : 数据统计模块ViewModel
 */
class DataStatisticsViewModel : BaseViewModel() {
    val type = MutableLiveData<String>()    //页面类型 0->月度 1->年度
    val date = MutableLiveData<String>()    //日期
    val datePattern = MutableLiveData<String>()  //日期格式

    val expenditureList = MutableLiveData<List<Expenditure>>()  //支出列表
    val expenditureAmount = MutableLiveData<String>()   //支出总额

    //获取支出列表
    fun queryAll() {
        val calendar = TimeUnit.parseDate(date.value!!, datePattern.value!!)
        if(type.value== "0"){
            expenditureList.value = AppDataBase.instance.getExpenditureDao()
                .queryAllByYear(calendar.get(Calendar.YEAR).toString())
        }
    }
}