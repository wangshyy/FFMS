package com.wsy.ffms.db.income

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

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
}