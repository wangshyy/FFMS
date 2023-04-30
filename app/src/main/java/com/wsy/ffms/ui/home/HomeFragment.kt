package com.wsy.ffms.ui.home

import android.view.View
import androidx.appcompat.widget.AppCompatTextView
import com.bumptech.glide.Glide
import com.lxj.xpopup.XPopup
import com.wsy.ffms.R
import com.wsy.ffms.core.base.BaseVMFragment
import com.wsy.ffms.databinding.FgHomeBinding
import com.wsy.ffms.db.AppDataBase
import com.wsy.ffms.db.banner.Banner
import com.wsy.ffms.db.budget.Budget
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
        mViewModel.intBudgetList()
        mViewModel.getBudgetAmount()
        mViewModel.getExpenditureAmount()
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
                    if (mViewModel.isExcess.value == true) {
                        paintColor = context.getColor(R.color.color_red_FF3333)
                        endPercent = 100
                        percent = 0
                    } else {
                        paintColor = context.getColor(R.color.colorPrimary)
                        endPercent = it.toInt()
                        percent = 0
                    }

                    invalidate()
                }
            }
        }

        //观察获取结果,都获取完成时
        mViewModel.getResult.observe(this) {
            if (it.first && it.second) {
                if (!mViewModel.budget.value.isNullOrEmpty()) {
                    if (mViewModel.budget.value?.toInt()!! < mViewModel.expenditure.value?.toInt()!!) {
                        mViewModel.isExcess.value = true
                        mViewModel.remainingBudget.value = "0"
                        mViewModel.budgetPercent.value =
                            ((mViewModel.expenditure.value!!.toInt() - mViewModel.budget.value!!.toInt()).toFloat() / mViewModel.budget.value!!.toFloat() * 100).toInt()
                                .toString()
                    } else {
                        mViewModel.isExcess.value = false
                        mViewModel.remainingBudget.value =
                            (mViewModel.budget.value?.toInt()
                                ?.minus(mViewModel.expenditure.value?.toInt()!!)).toString()
                        mViewModel.budgetPercent.value =
                            ((mViewModel.budget.value!!.toInt() - mViewModel.expenditure.value!!.toInt()).toFloat() / mViewModel.budget.value!!.toFloat() * 100).toInt()
                                .toString()
                    }
                } else {
                    mViewModel.budgetPercent.value = "0"
                }
                //重置
                mViewModel.getResult.value = Pair(first = false, second = false)
            }
        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.tv_plan_title -> {
                var checkPosition = mViewModel.budget.value?.let { it.toInt().div(500) - 1 }?:let { 0 }
                val view = XPopup.Builder(context)
                    .isViewMode(true)
                    .popupHeight(getPeekHeight())
                    .isDestroyOnDismiss(true) //对于只使用一次的弹窗，推荐设置这个
                    .asBottomList(
                        getString(R.string.budget_setting),
                        mViewModel.budgetList.toTypedArray(),
                        null,
                        checkPosition,
                        true,
                        { position, text ->
                            mViewModel.budget.value?.let {
                                AppDataBase.instance.getBudgetDao().update(Budget(1, text))
                            } ?: let {
                                AppDataBase.instance.getBudgetDao().insert(Budget(null, text))
                            }
                            mViewModel.getBudgetAmount()
                            mViewModel.getExpenditureAmount()
                        },
                        R.layout.dialog_budget_setting,
                        0
                    )
                view.show()
            }
        }
    }

    //弹窗高度
    private fun getPeekHeight(): Int {
        val peekHeight = resources.displayMetrics.heightPixels
        return peekHeight - peekHeight * 3 / 5
    }
}