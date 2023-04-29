package com.wsy.ffms.ui.mine.systemcofig

import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.gyf.immersionbar.ktx.immersionBar
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.listener.OnResultCallbackListener
import com.lxj.xpopup.XPopup
import com.wsy.ffms.R
import com.wsy.ffms.adapter.BannerListAdapter
import com.wsy.ffms.core.base.BaseVMActivity
import com.wsy.ffms.core.etx.toast
import com.wsy.ffms.databinding.AcBannerConfigBinding
import com.wsy.ffms.model.bean.Title
import com.wsy.ffms.util.GlideEngine
import com.wsy.ffms.widget.LoadingProgressDialog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File

/**
 *  author : wsy
 *  date   : 2023/4/29
 *  desc   : 轮播图配置页面
 */
class BannerConfigActivity : BaseVMActivity(), View.OnClickListener {
    private val mViewModel by viewModel<SystemConfigViewModel>()
    private val binding by binding<AcBannerConfigBinding>(R.layout.ac_banner_config)
    private lateinit var mEmptyView: View
    private var progressDialog: LoadingProgressDialog? = null
    private val mAdapter by lazy { BannerListAdapter() }
    override fun initView() {
        immersionBar {
            fitsSystemWindows(true)
            statusBarColor(R.color.white)
            statusBarDarkFont(true)
        }
        binding.apply {
            onClickListener = this@BannerConfigActivity
            viewModel = mViewModel
            title = Title(getString(R.string.banner)) { onBackPressed() }
            rvBanner.apply {
                layoutManager = LinearLayoutManager(this@BannerConfigActivity)
                adapter = mAdapter
            }

            fabAdd.setOnClickListener {
                //新增
                showChooseDialog()
            }
        }

        //空页面
        mEmptyView = layoutInflater.inflate(
            R.layout.view_empty,
            binding.rvBanner.parent as ViewGroup,
            false
        )

    }

    override fun initData() {
        mViewModel.getBannerList()
    }

    override fun startObserve() {
        mViewModel.uiState.observe(this) {
            it.showBannerList?.let { list ->
                progressDialog?.dismiss()
                mAdapter.setList(list)
            }
            if (it.showBannerList?.size ?: 0 == 0) mAdapter.setEmptyView(mEmptyView)

            if (it.showProgress) {
                progressDialog?.show() ?: let {
                    progressDialog = LoadingProgressDialog.show(this, cancelable = true)
                }
            }

            if (it.addSuccess) {
                progressDialog?.dismiss()
                mViewModel.getBannerList()
                toast(getString(R.string.add_success))
            }

            if (it.deleteSuccess) {
                progressDialog?.dismiss()
                mViewModel.getBannerList()
                toast(getString(R.string.delete_success))
            }
        }

        mAdapter.deleteId.observe(this) {
            it?.let {
                mViewModel.deleteBanner(it)
                mAdapter.clearDeleteId()
            }
        }
    }

    // 图片来源选择dialog
    private fun showChooseDialog() {
        XPopup.Builder(this)
            .asCenterList(
                "", arrayOf("拍照", "相册")
            ) { position: Int, text: String? ->
                if (position == 0) {
                    PictureSelector.create(this)
                        .openCamera(PictureMimeType.ofImage())
                        .imageEngine(GlideEngine.createGlideEngine())
                        .forResult(onResultCallbackListener)
                } else {
                    PictureSelector.create(this)
                        .openGallery(PictureMimeType.ofImage())
                        .imageEngine(GlideEngine.createGlideEngine())
                        .selectionMode(PictureConfig.SINGLE)
                        .isPreviewVideo(false)
                        .isCamera(false)
                        .isSingleDirectReturn(true)
                        .forResult(onResultCallbackListener)
                }
            }
            .show()
    }

    private val onResultCallbackListener = object : OnResultCallbackListener<LocalMedia> {
        override fun onResult(result: MutableList<LocalMedia>?) {
            if (result != null && result.size > 0) {
                lifecycleScope.launch {
                    withContext(Dispatchers.IO) {
                        val file = File(result[0].realPath)

                        //回主线程更新
                        withContext(Dispatchers.Main) {
//                            bannerImageList.add(result[0].realPath)
//                            binding.bannerHome.setDatas(bannerImageList)
//                            Glide.with(this@HomeFragment).load(result[0].realPath)
//                                .into(binding.tvImg)
                            mViewModel.insertBanner(result[0].realPath)
                        }
                    }
                }


            } else {
            }
        }

        override fun onCancel() {

        }
    }

    override fun onClick(v: View) {

    }
}