package com.wsy.ffms.db.income

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 *  author : wsy
 *  date   : 2023/3/9
 *  desc   : 收入类型表
 */
@Entity(tableName = "income_type")
data class Income(
    @PrimaryKey(autoGenerate = true) val id: Int?,
    @ColumnInfo(name = "type_name") val typeName: String? = null
)
