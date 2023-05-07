package com.wsy.ffms.db.todo

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.wsy.ffms.db.counttype.CountType
import com.wsy.ffms.db.expenditure.Expenditure

/**
 *  author : wsy
 *  date   : 2023/5/7
 *  desc   : 待办事项表操作类
 */
@Dao
interface TodoDao {
    //新增待办事项
    @Insert
    fun insert(todo: Todo)

    //删除
    @Delete
    fun delete(vararg todo: Todo)

    //获取所有待办事项
    @Query("SELECT * FROM todo")
    fun queryAllTodo(): List<Todo>?

}