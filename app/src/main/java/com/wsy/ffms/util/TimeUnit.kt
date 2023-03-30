package com.wsy.ffms.util

import android.net.ParseException
import java.text.Format
import java.text.SimpleDateFormat
import java.util.*

/**
 *  author : wsy
 *  date   : 2023/3/30
 *  desc   : 时间转换工具类
 */
class TimeUnit {

    companion object {
        /**
         * 时间字符串转换为Date
         * @param dateStr String 时间字符串
         * @param format String 格式字符串如："yyyy-MM-dd HH:mm:ss"
         * @return Date
         */
        fun parseDate(dateStr: String, format: String): Calendar {
            val sdf = SimpleDateFormat(format)
            var date = Date()
            try {
                date = sdf.parse(dateStr)
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            val calendar = Calendar.getInstance()
            calendar.time = date
            return calendar
        }
    }
}