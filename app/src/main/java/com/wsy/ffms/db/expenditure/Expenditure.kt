package com.wsy.ffms.db.expenditure

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 *  author : wsy
 *  date   : 2023/3/31
 *  desc   : 支出表
 */
@Entity(tableName = "expenditure")
data class Expenditure(
    @PrimaryKey(autoGenerate = true) val id: Int?,
    @ColumnInfo(name = "date_year") var dateYear: String? = null,
    @ColumnInfo(name = "date_month") var dateMonth: String? = null,
    @ColumnInfo(name = "date_day") var dateDay: String? = null,
    @ColumnInfo(name = "income_type") var incomeType: String? = null,
    var amount: String? = null
)
