package com.wsy.ffms.db.expenditure

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

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
}