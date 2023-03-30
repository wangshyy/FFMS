package com.wsy.ffms.db.familymember

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 *  author : wsy
 *  date   : 2023/3/30
 *  desc   : 家庭成员表
 */
@Entity(tableName = "family_member")
data class FamilyMember(
    @PrimaryKey(autoGenerate = true) val id: Int?,
    val name: String? = null
)
