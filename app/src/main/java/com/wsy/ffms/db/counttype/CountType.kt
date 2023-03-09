package com.wsy.ffms.db.counttype

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 *  author : wsy
 *  date   : 2023/3/9
 *  desc   : 账户类型表
 */
@Entity(tableName = "count_type")
data class CountType(
    @PrimaryKey(autoGenerate = true) val id: Int?,
    @ColumnInfo(name = "type_name") val typeName: String? = null
)
