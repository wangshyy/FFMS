package com.wsy.ffms.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.databinding.DataBindingUtil
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.wsy.ffms.R
import com.wsy.ffms.databinding.DialogBudgetSettingBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 *  author : wsy
 *  date   : 2023/4/30
 *  desc   : 预算设置底部弹窗
 */
class BudgetSettingDialog : BottomSheetDialogFragment() {
    private lateinit var binding: DialogBudgetSettingBinding
    private var mBottomSheetBehavior: BottomSheetBehavior<View>? = null
    private val viewModel by viewModel<HomeViewModel>()


    override fun onStart() {
        super.onStart()
        setStyle(STYLE_NORMAL, R.style.BottomSheetDialog)
        dialog?.let {
            val bottomSheet = it.findViewById<View>(R.id.design_bottom_sheet)
            // 设置全部展开时距离顶部的偏移量
            bottomSheet.layoutParams.height = getPeekHeight()
            // 设置背影透明
            bottomSheet.setBackgroundResource(android.R.color.transparent)
        }

        view?.post {
            val parent = requireView().parent as View
            val params: CoordinatorLayout.LayoutParams =
                parent.layoutParams as CoordinatorLayout.LayoutParams
            val behavior = params.behavior
            mBottomSheetBehavior = behavior as BottomSheetBehavior
            mBottomSheetBehavior?.addBottomSheetCallback(mBottomSheetBehaviorCallback)
            mBottomSheetBehavior?.peekHeight = getPeekHeight()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.BottomSheetDialog)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding =
            DataBindingUtil.inflate(inflater, R.layout.dialog_budget_setting, container, false)
        binding.lifecycleOwner = this//双向绑定
        binding.viewModel = this.viewModel


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        initObserve()
    }

    private fun initObserve() {

    }

    private fun getPeekHeight(): Int {
        val peekHeight = resources.displayMetrics.heightPixels
        return peekHeight - peekHeight * 3 / 5
    }

    private var mBottomSheetBehaviorCallback = object : BottomSheetBehavior.BottomSheetCallback() {
        override fun onStateChanged(bottomSheet: View, newState: Int) {
            if (newState == BottomSheetBehavior.STATE_DRAGGING) {
                mBottomSheetBehavior?.state = BottomSheetBehavior.STATE_EXPANDED
            }
        }

        override fun onSlide(bottomSheet: View, slideOffset: Float) {

        }
    }
}