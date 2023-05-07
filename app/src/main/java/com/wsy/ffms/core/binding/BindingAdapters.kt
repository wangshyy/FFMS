package com.wsy.ffms.core.binding

import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.databinding.BindingAdapter
import com.wsy.ffms.R
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Period
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

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

@BindingAdapter("type", "lineChartType")
fun AppCompatTextView.initTextByHorBarChartType(type: String?, lineChartType: String?) {
    text = when (type) {
        "0" -> {
            when (lineChartType) {
                "0" -> context.getString(R.string.expenditure_rank)
                "1" -> context.getString(R.string.income_rank)
                else -> context.getString(R.string.expenditure_rank)
            }
        }
        "1" -> {
            when (lineChartType) {
                "0" -> context.getString(R.string.expenditure_percentage)
                "1" -> context.getString(R.string.income_percentage)
                else -> context.getString(R.string.expenditure_percentage)
            }
        }
        else -> ""
    }
}

@BindingAdapter("isExcess", "percent")
fun AppCompatTextView.initTextByIsExcess(isExcess: Boolean, percent: String?) {
    text = if (!isExcess) "剩余：$percent%" else "超额：$percent%"
}

@BindingAdapter("date")
fun AppCompatImageView.initSrcByDate(date: String) {
    val now = LocalDate.now()
    val day = ChronoUnit.DAYS.between(
        now,
        LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
    )
    when  {
        day < 0L -> setImageResource(R.drawable.shape_todo_item_bg_grey)
        day == 0L -> setImageResource(R.drawable.shape_todo_item_bg_red)
        day == 1L -> setBackgroundResource(R.drawable.shape_todo_item_bg_orange)
        else -> setBackgroundResource(R.drawable.shape_todo_item_bg_normal)
    }
}

@BindingAdapter("date")
fun AppCompatTextView.initTextByDate(date: String) {
    val now = LocalDate.now()
    val day = ChronoUnit.DAYS.between(
        now,
        LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
    )
    text = when {
        day < 0L -> context.getText(R.string.late)
        day == 0L -> context.getText(R.string.today)
        day == 1L -> context.getText(R.string.tomorrow)
        else -> date
    }
}
