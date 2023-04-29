package com.wsy.ffms.widget

import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.wsy.ffms.R

class CustomViewBudget(context: Context, attrs: AttributeSet?, defStyleAttr: Int) :
    View(context, attrs, defStyleAttr) {
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context) : this(context, null, 0)

    private val paintWidth: Float
        get() = width.toFloat() / 8
    var endPercent: Int = 0
    private var percent: Int = 0
        set(value) {
            field = value
            invalidate()
        }    //结束百分比
    var paintColor = context.getColor(R.color.colorPrimary)
    var paintColorShade = context.getColor(R.color.color_shade)
    var paintColorAlert = context.getColor(R.color.color_red_FF3333)


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (percent == 0) {
            startAnimation()
            drawProgress(canvas)
        } else {
            drawProgress(canvas)
        }

    }

    //画进度
    private fun drawProgress(canvas: Canvas?) {
//        canvas?.translate(0F, paintWidth / 2)
        canvas?.save()//把当前的画布的状态进行保存，然后放入Canvas状态栈中

        //画阴影弧线
        val arcPainShade = Paint()
        arcPainShade.apply {
            color = paintColorShade  //弧线画笔颜色
            style = Paint.Style.STROKE //弧线画笔样式，stroke:填充
            strokeWidth = paintWidth    //画笔宽度
            isAntiAlias = true    //抗锯齿
        }
        val rectFArc = RectF(
            paintWidth / 2,
            paintWidth / 2,
            width.toFloat() - paintWidth / 2,
            height.toFloat() - paintWidth / 2
        )    //画布矩形框大小
        canvas?.drawArc(
            rectFArc,
            0F,
            360F,
            false,
            arcPainShade
        )    //画阴影弧线

        if (endPercent == 0) return
        //画弧线
        val arcPain = Paint()
        arcPain.apply {
            color = if (endPercent < 20) paintColorAlert else paintColor //弧线画笔颜色
            style = Paint.Style.STROKE //弧线画笔样式，stroke:填充
            strokeWidth = paintWidth    //画笔宽度
            isAntiAlias = true    //抗锯齿
        }
        canvas?.drawArc(
            rectFArc,
            269F,
            360F * (percent.toFloat() / 100F) + 1F,
            false,
            arcPain
        )    //画弧线
    }

    private fun startAnimation() {
        ObjectAnimator.ofInt(this, "percent", 1, endPercent).apply {
            duration = 500
            start()
            /*addUpdateListener {
                invalidate()
            }*/
        }
    }

}