package com.wsy.ffms.helper

import android.content.Context
import com.wsy.ffms.db.AppDataBase
import com.wsy.ffms.db.user.User
import java.lang.Appendable

/**
 *  author : wsy
 *  date   : 2023/2/28
 *  desc   : User帮助类
 */
class UserHelper private constructor() {
    companion object {
        val instance: UserHelper by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            UserHelper()
        }
    }

    private var context: Context? = null
    private var user: User? = null

    fun setUser(user: User) {
        this.user = user
    }

    fun getUser(): User {
        user?.let {
            return it
        }
        user = AppDataBase.instance.getUserDao().getActivateUser()
        if (user == null) {
            user = User(null)
        }
        return user as User
    }

    fun logout(){
        AppDataBase.instance.getUserDao().userReset()
    }

}