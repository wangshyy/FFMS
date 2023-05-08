package com.wsy.ffms.db.todo

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

/**
 *  author : wsy
 *  date   : 2023/5/7
 *  desc   : 待办事项表
 */
@Entity(tableName = "todo")
data class Todo(
    @PrimaryKey(autoGenerate = true) val id: Int?,
    @ColumnInfo(name = "date") var date: String? = null,
    @ColumnInfo(name = "title") var title: String? = null,
    @ColumnInfo(name = "content") var content: String? = null,
)


data class LocalDateTodo(
    val id: Int?,
    var date: LocalDate? = null,
    var title: String? = null,
    var content: String? = null,
)
