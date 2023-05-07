package com.wsy.ffms.db.income

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 *  author : wsy
 *  date   : 2023/3/30
 *  desc   : 收入表
 */
@Entity(tableName = "income")
data class Income(
    @PrimaryKey(autoGenerate = true) val id: Int?,
    @ColumnInfo(name = "date_year") var dateYear: String? = null,
    @ColumnInfo(name = "date_month") var dateMonth: String? = null,
    @ColumnInfo(name = "date_day") var dateDay: String? = null,
    @ColumnInfo(name = "count_type") var countType: String? = null,
    @ColumnInfo(name = "income_type") var incomeType: String? = null,
    @ColumnInfo(name = "family_member") var familyMember: String? = null,
    var amount: String? = null
)
