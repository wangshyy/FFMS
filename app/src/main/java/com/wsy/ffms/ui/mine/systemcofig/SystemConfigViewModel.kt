package com.wsy.ffms.ui.mine.systemcofig

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.wsy.ffms.core.base.BaseViewModel
import com.wsy.ffms.db.AppDataBase
import com.wsy.ffms.db.banner.Banner
import com.wsy.ffms.db.consumptiontype.ConsumptionType
import com.wsy.ffms.db.counttype.CountType
import com.wsy.ffms.db.familymember.FamilyMember
import com.wsy.ffms.db.incometype.IncomeType
import com.wsy.ffms.model.bean.SystemConfigCommonBean

/**
 *  author : wsy
 *  date   : 2023/3/8
 *  desc   : 系统配置viewModel
 */
class SystemConfigViewModel : BaseViewModel() {
    private var _uiState = MutableLiveData<SystemConfigUiModel>()
    val uiState: LiveData<SystemConfigUiModel>
        get() = _uiState
    var pageType: String? = null //pageType：0->家庭成员 1->账户类型 2->消费类型 3->收入类型

    val type = MutableLiveData<String>() //类型/姓名

    //获取类型列表
    fun getAllType() {
        emitUiState(showProgress = true)
        launchOnUI {
            val typeList = mutableListOf<SystemConfigCommonBean>()
            when (pageType) {
                //获取家庭成员
                "0" -> {
                    (AppDataBase.instance.getFamilyMemberDao().queryAllFamilyMember())?.forEach {
                        typeList.add(SystemConfigCommonBean(it.id, it.name))
                    }
                }
                //获取账户类型
                "1" -> {
                    (AppDataBase.instance.getCountTypeDao().queryAllCountType())?.forEach {
                        typeList.add(SystemConfigCommonBean(it.id, it.typeName))
                    }
                }
                //获取消费类型
                "2" -> {
                    (AppDataBase.instance.getConsumptionTypeDao()
                        .queryAllConsumptionType())?.forEach {
                            typeList.add(SystemConfigCommonBean(it.id, it.typeName))
                        }
                }
                //获取收入类型
                "3" -> {
                    (AppDataBase.instance.getIncomeTypeDao().queryAllIncomeType())?.forEach {
                        typeList.add(SystemConfigCommonBean(it.id, it.typeName))
                    }
                }
            }

            emitUiState(showSuccess = typeList)
        }
    }

    //插入类型数据到数据库
    fun insertType() {
        emitUiState(showProgress = true)
        launchOnUI {
            when (pageType) {
                //插入到家庭成员数据表
                "0" -> {
                    AppDataBase.instance.getFamilyMemberDao().insert(FamilyMember(null, type.value))
                    emitUiState(addSuccess = true)
                }
                //插入到账户类型数据表
                "1" -> {
                    AppDataBase.instance.getCountTypeDao().insert(CountType(null, type.value))
                    emitUiState(addSuccess = true)
                }
                //插入到消费类型数据表获取
                "2" -> {
                    AppDataBase.instance.getConsumptionTypeDao()
                        .insert(ConsumptionType(null, type.value))
                    emitUiState(addSuccess = true)
                }
                //插入到收入类型数据表获取
                "3" -> {
                    AppDataBase.instance.getIncomeTypeDao().insert(IncomeType(null, type.value))
                    emitUiState(addSuccess = true)
                }
            }
        }
    }

    //从数据库删除数据
    fun deleteType(id: Int) {
        emitUiState(showProgress = true)
        launchOnUI {
            when (pageType) {
                //家庭成员
                "0" -> {
                    AppDataBase.instance.getFamilyMemberDao().delete(FamilyMember(id))
                    emitUiState(deleteSuccess = true)
                }
                //账户类型
                "1" -> {
                    AppDataBase.instance.getCountTypeDao().delete(CountType(id))
                    emitUiState(deleteSuccess = true)
                }
                //消费类型
                "2" -> {
                    AppDataBase.instance.getConsumptionTypeDao().delete(ConsumptionType(id))
                    emitUiState(deleteSuccess = true)
                }
                //收入类型
                "3" -> {
                    AppDataBase.instance.getIncomeTypeDao().delete(IncomeType(id))
                    emitUiState(deleteSuccess = true)
                }
            }
        }
    }

    //获取轮播图列表
    fun getBannerList() {
        emitUiState(showProgress = true)
        launchOnUI {
            val bannerList = AppDataBase.instance.getBannerDao().queryAllBanner()
            emitUiState(showBannerList = bannerList)
        }
    }

    //添加轮播图
    fun insertBanner(uri: String) {
        emitUiState(showProgress = true)
        launchOnUI {
            AppDataBase.instance.getBannerDao().insert(Banner(null, uri))
            emitUiState(addSuccess = true)
        }
    }

    //删除轮播图
    fun deleteBanner(id: Int) {
        emitUiState(showProgress = true)
        launchOnUI {
            AppDataBase.instance.getBannerDao().delete(Banner(id))
            emitUiState(deleteSuccess = true)
        }
    }

    private fun emitUiState(
        showProgress: Boolean = false,
        showError: String? = null,
        addSuccess: Boolean = false,
        deleteSuccess: Boolean = false,
        showSuccess: List<SystemConfigCommonBean>? = null,
        showBannerList: List<Banner>? = null
    ) {
        val uiState =
            SystemConfigUiModel(
                showProgress,
                showError,
                addSuccess,
                deleteSuccess,
                showSuccess,
                showBannerList
            )
        _uiState.value = uiState
    }

    data class SystemConfigUiModel(
        val showProgress: Boolean,
        val showError: String?,
        val addSuccess: Boolean,
        val deleteSuccess: Boolean,
        val showSuccess: List<SystemConfigCommonBean>?,
        val showBannerList: List<Banner>?
    )
}