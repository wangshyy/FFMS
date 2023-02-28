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
    @PrimaryKey(autoGenerate = true)
    val id: Int? = 0,
    @ColumnInfo(name = "user_name") val userName: String?,
    @ColumnInfo(name = "password") var password: String?,
    @ColumnInfo(name = "is_rem_pwd") var isRemPwd: Boolean?, //是否记住密码
    @ColumnInfo(name = "is_activate") var isActivate: Int?,  //当前登录用户
)