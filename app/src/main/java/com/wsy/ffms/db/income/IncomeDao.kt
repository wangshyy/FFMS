package com.wsy.ffms.db.income

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.wsy.ffms.db.banner.Banner
import com.wsy.ffms.db.expenditure.Expenditure

/**
 *  author : wsy
 *  date   : 2023/3/30
 *  desc   : 收入表操作类
 */
@Dao
interface IncomeDao {
    //新增收入
    @Insert
    fun insert(income: Income)

    //删除
    @Delete
    fun delete(vararg income: Income)

    //获取所有收入信息
    @Query("SELECT * FROM income")
    fun queryAllIncome(): List<Income>?

    //根据年份获取所有收入信息
    @Query("SELECT * FROM income WHERE date_year == :year")
    fun queryAllByYear(year: String): List<Income>?

    //根据年-月份获取所有收入信息
    @Query("SELECT * FROM income WHERE date_year == :year AND date_month == :month")
    fun queryAllByYearMonth(year: String,month: String): List<Income>?

    //根据年份、支出类型获取所有收入信息
    @Query("SELECT * FROM income WHERE date_year == :year  AND income_type==:type")
    fun queryAllByYearAndType(year: String,  type: String): List<Income>?

    //根据月份、支出类型获取所有收入信息
    @Query("SELECT * FROM income WHERE date_year == :year AND date_month == :month AND income_type==:type")
    fun queryAllByMonthAndType(year: String, month: String, type: String): List<Income>?
}