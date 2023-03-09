package com.wsy.ffms.db.counttype

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

/**
 *  author : wsy
 *  date   : 2023/3/9
 *  desc   : 账户类型表操作类
 */
@Dao
interface CountTypeDao {
    //新增账户类型
    @Insert
    fun insert(countType: CountType)

    //删除
    @Delete
    fun delete(vararg countType: CountType)

    //获取所有类型
    @Query("SELECT * FROM count_type")
    fun queryAllCountType(): List<CountType>?
}