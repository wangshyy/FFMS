package com.wsy.ffms.db.banner

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

/**
 *  author : wsy
 *  date   : 2023/4/29
 *  desc   :
 */
@Dao
interface BannerDao {
    //新增轮播图
    @Insert
    fun insert(banner: Banner)

    //删除
    @Delete
    fun delete(vararg banner: Banner)

    //获取所有轮播图
    @Query("SELECT * FROM banner")
    fun queryAllBanner(): List<Banner>?
}