package com.wsy.ffms.db.user

import androidx.room.*

/**
 *  author : wsy
 *  date   : 2023/2/24
 *  desc   : User表操作类
 */
@Dao
interface UserDao {
    //新增用户
    @Insert
    fun insert(user: User)

    //删除用户
    @Delete
    fun delete(vararg user: User)

    //删除全部用户
    @Query("DELETE FROM user")
    fun deleteAllUser()

    //更新用户
    @Update
    fun update(vararg user: User)

    //根据用户名查询用户,不区分大小写
    @Query("SELECT * FROM user WHERE user_name= :userName COLLATE NOCASE")
    fun getUserByName(userName: String?): User?

    //获取最近一次登录的用户
    @Query("SELECT * FROM user WHERE is_activate= 1 LIMIT 1")
    fun getActivateUser(): User?

    //清空最近登录记录
    @Query("UPDATE user SET is_activate = 0")
    fun userReset()

    //获取所有用户
    @Query("SELECT * FROM user")
    fun queryUserAll(): List<User?>?

}