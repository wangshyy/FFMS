package com.wsy.ffms.db.familymember

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

/**
 *  author : wsy
 *  date   : 2023/3/30
 *  desc   : 家庭成员表操作类
 */
@Dao
interface FamilyMemberDao {
    //新增家庭成员
    @Insert
    fun insert(familyMember: FamilyMember)

    //删除
    @Delete
    fun delete(vararg familyMember: FamilyMember)

    //获取所有家庭成员
    @Query("SELECT * FROM family_member")
    fun queryAllFamilyMember(): List<FamilyMember>?
}