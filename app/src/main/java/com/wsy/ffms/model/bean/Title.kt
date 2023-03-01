package com.wsy.ffms.model.bean

/**
 *  author : wsy
 *  date   : 2023/2/28
 *  desc   : titleBean
 */
class Title(
    val title: String?,
    val backIconVisible: Boolean? = true,
    val action: () -> Unit
)