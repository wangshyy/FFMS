package com.wsy.ffms.db.income

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

/**
 *  author : wsy
 *  date   : 2023/3/9
 *  desc   : 收入类型操作类
 */
@Dao
interface IncomeDao {
    //新增收入类型
    @Insert
    fun insert(income: Income)

    //删除
    @Delete
    fun delete(vararg income: Income)

    //获取所有类型
    @Query("SELECT * FROM income_type")
    fun queryAllIncomeType(): List<Income>?
}