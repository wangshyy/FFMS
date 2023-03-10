package com.wsy.ffms.ui.mine.systemcofig

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.gyf.immersionbar.ktx.immersionBar
import com.wsy.ffms.R
import com.wsy.ffms.adapter.SystemConfigCommonAdapter
import com.wsy.ffms.core.base.BaseVMActivity
import com.wsy.ffms.core.etx.toast
import com.wsy.ffms.databinding.AcSystemConfigCommonBinding
import com.wsy.ffms.db.AppDataBase
import com.wsy.ffms.model.bean.SystemConfigCommonBean
import com.wsy.ffms.model.bean.Title
import com.wsy.ffms.widget.LoadingProgressDialog
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 *  author : wsy
 *  date   : 2023/3/8
 *  desc   : 系统配置公用页面 1、账户类型 2、消费类型 3、收入类型
 */
class SystemConfigCommonActivity : BaseVMActivity(), View.OnClickListener {
    private val binding by binding<AcSystemConfigCommonBinding>(R.layout.ac_system_config_common)
    private val mViewModel by viewModel<SystemConfigViewModel>()
    private val adapter by lazy { SystemConfigCommonAdapter() }
    private lateinit var mEmptyView: View
    private var progressDialog: LoadingProgressDialog? = null
    override fun initView() {
        immersionBar {
            fitsSystemWindows(true)
            statusBarColor(R.color.white)
            statusBarDarkFont(true)
        }
        mViewModel.pageType = intent.getStringExtra("pageType")
        binding.apply {
            onClickListener = this@SystemConfigCommonActivity
            viewModel = mViewModel
            title = Title(
                when (mViewModel.pageType) {
                    "1" -> applicationContext.getString(R.string.count_type)
                    "2" -> applicationContext.getString(R.string.consumption_type)
                    else -> applicationContext.getString(R.string.income_type)
                }
            ) { onBackPressed() }
            recyclerView.apply {
                layoutManager = LinearLayoutManager(this@SystemConfigCommonActivity)
                adapter = this@SystemConfigCommonActivity.adapter
            }
            isAdding = false //初始化为false 点击添加后为true
        }

        //空页面
        mEmptyView = layoutInflater.inflate(
            R.layout.view_empty,
            binding.recyclerView.parent as ViewGroup,
            false
        )
    }

    override fun initData() {
        mViewModel.getAllType()
    }

    override fun startObserve() {

        mViewModel.type.observe(this) {
            if (!it.isNullOrEmpty()) {
                binding.tvOperate.text = resources.getString(R.string.common_confirm)
            } else {
                binding.tvOperate.text = resources.getString(R.string.common_cancel)
            }
        }

        mViewModel.uiState.observe(this) {
            it.showSuccess?.let { list ->
                progressDialog?.dismiss()
                adapter.setList(list)
            }
            if (it.showSuccess?.size ?: 0 == 0) adapter.setEmptyView(mEmptyView)

            if (it.showProgress) {
                progressDialog?.show() ?: let {
                    progressDialog = LoadingProgressDialog.show(this, cancelable = true)
                }
            }

            if (it.addSuccess) {
                progressDialog?.dismiss()
                mViewModel.getAllType()
                toast(applicationContext.getString(R.string.add_success))
            }

            if (it.deleteSuccess) {
                progressDialog?.dismiss()
                mViewModel.getAllType()
                toast(applicationContext.getString(R.string.delete_success))
            }
        }

        adapter.deleteId.observe(this) {
            it?.let {
                mViewModel.deleteType(it)
                adapter.clearDeleteId()
            }
        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.tv_add -> {
                binding.isAdding = true
                if (adapter.data.size == 0) binding.recyclerView.visibility =
                    View.GONE //没有数据是点击添加，隐藏recyclerview
            }

            R.id.tv_operate -> {
                if (!mViewModel.type.value.isNullOrEmpty()) {
                    //添加类型到数据库
                    mViewModel.insertType()

                    //清空输入框数据
                    mViewModel.type.value = null
                }

                binding.isAdding = false
                if (binding.recyclerView.visibility == View.GONE)
                    binding.recyclerView.visibility = View.VISIBLE
            }

        }
    }
}