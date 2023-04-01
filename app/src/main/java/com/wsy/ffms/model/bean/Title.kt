package com.wsy.ffms.model.bean

import android.graphics.Color

/**
 *  author : wsy
 *  date   : 2023/2/28
 *  desc   : titleBean
 */
class Title(
    val title: String?,
    val backIconVisible: Boolean? = true,
    val action: () -> Unit
){
    var textColor: Int = Color.BLACK
    var backgroundColor: Int = Color.WHITE
}