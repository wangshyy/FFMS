package com.wsy.ffms.db.user

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 *  author : wsy
 *  date   : 2023/2/24
 *  desc   :
 */
@Entity(tableName = "user")
data class User(
    @PrimaryKey
    var id: String,
    @ColumnInfo(name = "user_name") val userName: String? = null,
    @ColumnInfo(name = "password") val password: String? = null,
)