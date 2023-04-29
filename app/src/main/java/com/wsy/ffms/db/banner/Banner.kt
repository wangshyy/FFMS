package com.wsy.ffms.db.banner

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 *  author : wsy
 *  date   : 2023/4/29
 *  desc   : 轮播图
 */
@Entity(tableName = "banner")
data class Banner(
    @PrimaryKey(autoGenerate = true) val id: Int?,
    @ColumnInfo(name = "uri") val uri: String? = null
)