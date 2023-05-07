package com.wsy.ffms.db

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.wsy.ffms.MyApplication
import com.wsy.ffms.db.banner.Banner
import com.wsy.ffms.db.banner.BannerDao
import com.wsy.ffms.db.budget.Budget
import com.wsy.ffms.db.budget.BudgetDao
import com.wsy.ffms.db.consumptiontype.ConsumptionType
import com.wsy.ffms.db.consumptiontype.ConsumptionTypeDao
import com.wsy.ffms.db.counttype.CountType
import com.wsy.ffms.db.counttype.CountTypeDao
import com.wsy.ffms.db.expenditure.Expenditure
import com.wsy.ffms.db.expenditure.ExpenditureDao
import com.wsy.ffms.db.familymember.FamilyMember
import com.wsy.ffms.db.familymember.FamilyMemberDao
import com.wsy.ffms.db.income.Income
import com.wsy.ffms.db.income.IncomeDao
import com.wsy.ffms.db.incometype.IncomeType
import com.wsy.ffms.db.incometype.IncomeTypeDao
import com.wsy.ffms.db.todo.Todo
import com.wsy.ffms.db.todo.TodoDao
import com.wsy.ffms.db.user.User
import com.wsy.ffms.db.user.UserDao

/**
 *  author : wsy
 *  date   : 2023/2/24
 *  desc   :
 */
@Database(
    entities = [User::class, CountType::class, ConsumptionType::class, IncomeType::class, FamilyMember::class, Income::class, Expenditure::class, Banner::class, Budget::class, Todo::class],
    version = 10,
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
    abstract fun getConsumptionTypeDao(): ConsumptionTypeDao
    abstract fun getIncomeTypeDao(): IncomeTypeDao
    abstract fun getFamilyMemberDao(): FamilyMemberDao
    abstract fun getIncomeDao(): IncomeDao
    abstract fun getExpenditureDao(): ExpenditureDao
    abstract fun getBannerDao(): BannerDao
    abstract fun getBudgetDao(): BudgetDao
    abstract fun getTodoDao(): TodoDao
}