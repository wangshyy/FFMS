package com.wsy.ffms.db.budget

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 *  author : wsy
 *  date   : 2023/4/29
 *  desc   : 预算
 */
@Entity(tableName = "budget")
data class Budget(
    @PrimaryKey(autoGenerate = true) val id: Int?,
    @ColumnInfo(name = "budget_value") val budgetValue: String? = null
)