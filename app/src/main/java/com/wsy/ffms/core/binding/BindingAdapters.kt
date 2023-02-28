package com.wsy.ffms.core.binding

import android.opengl.Visibility
import android.view.View
import androidx.databinding.BindingAdapter

/**
 *  author : wsy
 *  date   : 2023/2/28
 *  desc   :
 */
@BindingAdapter("visibleUnless")
fun bindVisibleUnless(view: View, visible: Boolean) {
    view.visibility = if (visible)
        View.VISIBLE
    else
        View.GONE
}