package com.wsy.ffms.db.budget

import androidx.room.*

/**
 *  author : wsy
 *  date   : 2023/4/29
 *  desc   :
 */
@Dao
interface BudgetDao {
    // 新增预算
    @Insert
    fun insert(budget: Budget)
    // 修改
    @Update
    fun update(budget: Budget)

    // 删除
    @Delete
    fun delete(vararg budget: Budget)

    // 获取预算
    @Query("SELECT * FROM budget")
    fun queryBudget(): List<Budget>?
}