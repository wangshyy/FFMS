package com.wsy.ffms.core.binding

import android.graphics.Color
import android.graphics.drawable.Drawable
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout
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

@BindingAdapter("textColor", requireAll = false)
fun AppCompatTextView.init(color: Int) {
    setTextColor(color)
}

@BindingAdapter("backgroundColor", requireAll = false)
fun ConstraintLayout.init(color: Drawable) {
    background = color
}