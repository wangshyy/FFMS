package com.wsy.ffms.db

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.wsy.ffms.MyApplication
import com.wsy.ffms.db.consumption.Consumption
import com.wsy.ffms.db.consumption.ConsumptionDao
import com.wsy.ffms.db.counttype.CountType
import com.wsy.ffms.db.counttype.CountTypeDao
import com.wsy.ffms.db.income.Income
import com.wsy.ffms.db.income.IncomeDao
import com.wsy.ffms.db.user.User
import com.wsy.ffms.db.user.UserDao

/**
 *  author : wsy
 *  date   : 2023/2/24
 *  desc   :
 */
@Database(
    entities = [User::class, CountType::class, Consumption::class, Income::class],
    version = 3,
    exportSchema = false
)
abstract class AppDataBase : RoomDatabase() {
    companion object {
        private const val DB_NAME = "ffms.db"
        val instance: AppDataBase by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            Room.databaseBuilder(MyApplication.CONTEXT, AppDataBase::class.java, DB_NAME)
                .fallbackToDestructiveMigration()   // 设置迁移数据库如果发生错误，将会重新创建数据库，而不是发生崩溃
                .allowMainThreadQueries()   //允许在主线程执行
                .build()
        }
    }

    abstract fun getUserDao(): UserDao
    abstract fun getCountTypeDao(): CountTypeDao
    abstract fun getConsumptionDao(): ConsumptionDao
    abstract fun getIncomeDao(): IncomeDao
}