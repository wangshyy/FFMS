package com.wsy.ffms.model.bean

import com.wsy.ffms.R

/**
 *  author : wsy
 *  date   : 2023/2/28
 *  desc   : titleBean
 */
class Title(
    val title: String?,
    val backIconVisible: Boolean? = true,
    val backgroundColor: Int? = R.color.white,
    val action: () -> Unit
) {
}