package com.wsy.ffms.db.consumptiontype

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 *  author : wsy
 *  date   : 2023/3/9
 *  desc   : 消费类型表
 */
@Entity(tableName = "consumption_type")
data class ConsumptionType(
    @PrimaryKey(autoGenerate = true) val id: Int?,
    @ColumnInfo(name = "type_name")val typeName: String? = null
)
