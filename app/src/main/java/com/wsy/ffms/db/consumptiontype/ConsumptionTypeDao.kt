package com.wsy.ffms.db.consumptiontype

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

/**
 *  author : wsy
 *  date   : 2023/3/9
 *  desc   : 消费类型操作类
 */
@Dao
interface ConsumptionTypeDao {
    //新增消费类型
    @Insert
    fun insert(consumptionType: ConsumptionType)

    //删除
    @Delete
    fun delete(vararg consumptionType: ConsumptionType)

    //获取所有类型
    @Query("SELECT * FROM consumption_type")
    fun queryAllConsumptionType(): List<ConsumptionType>?
}