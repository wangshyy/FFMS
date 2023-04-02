package com.wsy.ffms.db.income

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
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

    //获取所有收入信息
    @Query("SELECT * FROM income")
    fun queryAllIncome(): List<Income>?

    //根据年份获取所有收入信息
    @Query("SELECT * FROM income WHERE date_year == :year")
    fun queryAllByYear(year: String): List<Income>?

    //根据年-月份获取所有收入信息
    @Query("SELECT * FROM income WHERE date_year == :year AND date_month == :month")
    fun queryAllByYearMonth(year: String,month: String): List<Income>?
}