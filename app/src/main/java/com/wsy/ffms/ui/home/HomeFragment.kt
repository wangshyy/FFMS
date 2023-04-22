package com.wsy.ffms.ui.home

import android.view.View
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.listener.OnResultCallbackListener
import com.lxj.xpopup.XPopup
import com.wsy.ffms.R
import com.wsy.ffms.core.base.BaseVMFragment
import com.wsy.ffms.databinding.FgHomeBinding
import com.wsy.ffms.model.bean.Title
import com.wsy.ffms.ui.MainActivity
import com.wsy.ffms.util.GlideEngine
import com.youth.banner.adapter.BannerImageAdapter
import com.youth.banner.holder.BannerImageHolder
import com.youth.banner.indicator.CircleIndicator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

/**
 *  author : wsy
 *  date   : 2023/3/2
 *  desc   : 首页
 */
class HomeFragment : BaseVMFragment<FgHomeBinding>(R.layout.fg_home), View.OnClickListener {
    private val bannerImageList = arrayListOf<String>()
    override fun initView() {
        MainActivity.setStatusBarHeight(binding.flStatusBar, requireActivity())
        binding.apply {
            title = Title(getString(R.string.home), false) {}
            onClickListener = this@HomeFragment
            //初始化轮播图
            bannerHome.setAdapter(object : BannerImageAdapter<String>(bannerImageList) {
                override fun onBindView(
                    holder: BannerImageHolder,
                    data: String?,
                    position: Int,
                    size: Int
                ) {
                    Glide.with(holder.imageView).load(data).into(holder.imageView)
                }
            })
                .setIndicator(CircleIndicator(context))// 设置圆形指示器
//                .setIndicatorGravity(IndicatorConfig.Direction.RIGHT)// 设置指示器位置
                .setLoopTime(5000)// 轮播时间
                .addBannerLifecycleObserver(this@HomeFragment)// 添加生命周期观察者
        }
    }

    override fun initData() {
    }

    override fun startObserve() {
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.tv_add -> showChooseDialog()
        }

    }

    // 图片来源选择dialog
    private fun showChooseDialog() {
        XPopup.Builder(context)
            .asCenterList(
                "", arrayOf("拍照", "相册")
            ) { position: Int, text: String? ->
                if (position == 0) {
                    PictureSelector.create(this.requireActivity())
                        .openCamera(PictureMimeType.ofImage())
                        .imageEngine(GlideEngine.createGlideEngine())
                        .forResult(onResultCallbackListener)
                } else {
                    PictureSelector.create(this.requireActivity())
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
                            bannerImageList.add(result[0].realPath)
                            binding.bannerHome.setDatas(bannerImageList)
                            Glide.with(this@HomeFragment).load(result[0].realPath).into(binding.tvImg)
                        }
                    }
                }


            } else {
            }
        }

        override fun onCancel() {

        }
    }
}