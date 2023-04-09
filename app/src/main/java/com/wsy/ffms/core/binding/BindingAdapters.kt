package com.wsy.ffms.core.binding

import android.view.View
import androidx.appcompat.widget.AppCompatTextView
import androidx.databinding.BindingAdapter
import com.wsy.ffms.R

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

@BindingAdapter("typeStatus")
fun AppCompatTextView.initTextByType(status: String?) {
    text = when (status) {
        "0" -> context.getString(R.string.consumption_type)
        "1" -> context.getString(R.string.income_type)
        else -> context.getString(R.string.consumption_type)
    }
}

@BindingAdapter("lineChartType")
fun AppCompatTextView.initTextByLineChartType(status: String?) {
    text = when (status) {
        "0" -> context.getString(R.string.expenditure_trend)
        "1" -> context.getString(R.string.income_trend)
        else -> context.getString(R.string.expenditure_trend)
    }
}
