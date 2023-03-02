package com.wsy.ffms.widget

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.drawable.AnimationDrawable
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.wsy.ffms.R

/**
 *  author : wsy
 *  date   : 2023/3/2
 *  desc   : 加载进度条dialog
 */
class LoadingProgressDialog(context: Context, theme: Int) : Dialog(context, theme) {
    /**
     * 当窗口焦点改变时调用
     */
    override fun onWindowFocusChanged(hasFocus: Boolean) {
        val imageView = findViewById<View>(R.id.spinnerImageView) as ImageView
        // 获取ImageView上的动画背景
        val spinner = imageView.background as AnimationDrawable
        // 开始动画
        spinner.start()
    }

    /**
     * 给Dialog设置提示信息
     *
     * @param message
     */
    fun setMessage(message: CharSequence?) {
        if (message != null && message.isNotEmpty()) {
            findViewById<View>(R.id.message).visibility = View.VISIBLE
            val txt = findViewById<View>(R.id.message) as TextView
            txt.text = message
            txt.invalidate()
        }
    }

    companion object {
        private var dialog: LoadingProgressDialog? = null

        /**
         * 弹出自定义ProgressDialog
         *
         * @param context
         * 上下文
         * @param message
         * 提示
         * @param cancelable
         * 是否按返回键取消
         * @param cancelListener
         * 按下返回键监听
         * @return
         */
        @JvmOverloads
        fun show(
            context: Context,
            message: CharSequence? = null,
            cancelable: Boolean = true,
            cancelListener: DialogInterface.OnCancelListener? = null
        ): LoadingProgressDialog? {
            dialog = LoadingProgressDialog(context, R.style.APPLoadingProgress)
            dialog?.let {
                it.setTitle("")
                it.setContentView(R.layout.view_loading_progress)
                if (message == null || message.isEmpty()) {
                    it.findViewById<View>(R.id.message).visibility =
                        View.GONE
                } else {
                    val txt = it.findViewById<View>(R.id.message) as TextView
                    txt.text = message
                }
                // 按返回键是否取消
                it.setCancelable(cancelable)
                it.setCanceledOnTouchOutside(false)
                // 监听返回键处理
                it.setOnCancelListener(cancelListener)
                // 设置居中
                it.window!!.attributes.gravity = Gravity.CENTER
                val lp = it.window!!.attributes
                // 设置背景层透明度
                lp.dimAmount = 0.2f
                it.window!!.attributes = lp
                it.show()
            }
            return dialog
        }
    }
}