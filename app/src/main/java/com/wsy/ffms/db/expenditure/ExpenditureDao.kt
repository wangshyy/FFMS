package com.wsy.ffms.db.expenditure

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import java.time.Month

/**
 *  author : wsy
 *  date   : 2023/3/31
 *  desc   : 支出表操作类
 */
@Dao
interface ExpenditureDao {
    //新增支出
    @Insert
    fun insert(expenditure: Expenditure)

    //获取所有支出信息
    @Query("SELECT * FROM expenditure")
    fun queryAllExpenditure(): List<Expenditure>?

    //根据年份获取所有支出信息
    @Query("SELECT * FROM expenditure WHERE date_year == :year")
    fun queryAllByYear(year: String): List<Expenditure>?

    //根据年-月份获取所有支出信息
    @Query("SELECT * FROM expenditure WHERE date_year == :year AND date_month == :month")
    fun queryAllByYearMonth(year: String, month: String): List<Expenditure>?

    //根据年份、支出类型获取所有支出信息
    @Query("SELECT * FROM expenditure WHERE date_year == :year  AND expenditure_type==:type")
    fun queryAllByYearAndType(year: String,  type: String): List<Expenditure>?

    //根据月份、支出类型获取所有支出信息
    @Query("SELECT * FROM expenditure WHERE date_year == :year AND date_month == :month AND expenditure_type==:type")
    fun queryAllByMonthAndType(year: String, month: String, type: String): List<Expenditure>?
}