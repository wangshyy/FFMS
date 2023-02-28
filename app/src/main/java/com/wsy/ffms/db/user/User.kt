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
    val id: Int?,
    @ColumnInfo(name = "user_name") val userName: String? = null,
    @ColumnInfo(name = "password") var password: String? = null,
    @ColumnInfo(name = "is_rem_pwd") var isRemPwd: Boolean? = false, //是否记住密码
    @ColumnInfo(name = "is_activate") var isActivate: Int? = 0,  //当前登录用户
)