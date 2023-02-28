package com.wsy.ffms.core.binding

import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.databinding.BindingAdapter

/**
 *  author : wsy
 *  date   : 2023/2/28
 *  desc   :
 */
@BindingAdapter("navigationClick", requireAll = false)
fun AppCompatImageView.init(action: () -> Unit) {
    setOnClickListener { action() }
}

@BindingAdapter("title", requireAll = false)
fun AppCompatTextView.init(title: String) {
    text = title
}