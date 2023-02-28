package com.wsy.ffms.db

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.wsy.ffms.MyApplication
import com.wsy.ffms.db.user.User
import com.wsy.ffms.db.user.UserDao

/**
 *  author : wsy
 *  date   : 2023/2/24
 *  desc   :
 */
@Database(entities = [User::class], version = 1, exportSchema = false)
abstract class AppDataBase : RoomDatabase() {
    companion object {
        private const val DB_NAME = "ffms.db"
        val instance: AppDataBase by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            Room.databaseBuilder(MyApplication.CONTEXT, AppDataBase::class.java, DB_NAME).build()
        }
    }

    abstract fun getUserDao(): UserDao
}