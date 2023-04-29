package com.wsy.ffms.ui.home

import android.view.View
import com.bumptech.glide.Glide
import com.wsy.ffms.R
import com.wsy.ffms.core.base.BaseVMFragment
import com.wsy.ffms.core.etx.toast
import com.wsy.ffms.databinding.FgHomeBinding
import com.wsy.ffms.db.banner.Banner
import com.wsy.ffms.model.bean.Title
import com.wsy.ffms.ui.MainActivity
import com.wsy.ffms.widget.LoadingProgressDialog
import com.youth.banner.adapter.BannerImageAdapter
import com.youth.banner.holder.BannerImageHolder
import com.youth.banner.indicator.CircleIndicator
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 *  author : wsy
 *  date   : 2023/3/2
 *  desc   : 首页
 */
class HomeFragment : BaseVMFragment<FgHomeBinding>(R.layout.fg_home), View.OnClickListener {
    private val bannerImageList = arrayListOf<Banner>()
    private val mViewModel by viewModel<HomeViewModel>()
    private var progressDialog: LoadingProgressDialog? = null
    override fun initView() {
        MainActivity.setStatusBarHeight(binding.flStatusBar, requireActivity())
        binding.apply {
            title = Title(getString(R.string.home), false) {}
            viewModel = mViewModel
            onClickListener = this@HomeFragment
            //初始化轮播图
            bannerHome.setAdapter(object : BannerImageAdapter<Banner>(bannerImageList) {
                override fun onBindView(
                    holder: BannerImageHolder,
                    data: Banner?,
                    position: Int,
                    size: Int
                ) {
                    Glide.with(holder.imageView).load(data?.uri).into(holder.imageView)
                }
            })
                .setIndicator(CircleIndicator(context))// 设置圆形指示器
//                .setIndicatorGravity(IndicatorConfig.Direction.RIGHT)// 设置指示器位置
                .setLoopTime(5000)// 轮播时间
                .addBannerLifecycleObserver(this@HomeFragment)// 添加生命周期观察者

        }
    }

    override fun initData() {
        mViewModel.getBannerList()

        mViewModel.budgetPercent.value = "80"
    }

    override fun startObserve() {
        mViewModel.uiState.observe(this) {
            it.showBannerList?.let { list ->
                progressDialog?.dismiss()
                binding.bannerHome.setDatas(list)
            }

            if (it.showProgress) {
                progressDialog?.show() ?: let {
                    progressDialog = LoadingProgressDialog.show(requireContext(), cancelable = true)
                }
            }
        }

        //观察剩余预算
        mViewModel.budgetPercent.observe(this) {
            it?.let {
                binding.customViewBudget.apply {
                    endPercent = it.toInt()
                    invalidate()
                }
            }
        }
    }

    override fun onClick(v: View) {
        when (v.id) {

        }
    }
}