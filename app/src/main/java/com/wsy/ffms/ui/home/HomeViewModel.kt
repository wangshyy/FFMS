package com.wsy.ffms.ui.home

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.wsy.ffms.R
import com.wsy.ffms.core.base.BaseViewModel
import com.wsy.ffms.db.AppDataBase
import com.wsy.ffms.db.banner.Banner
import com.wsy.ffms.db.income.Income
import com.wsy.ffms.db.todo.Todo
import com.wsy.ffms.util.TimeUnit
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.*

/**
 *  author : wsy
 *  date   : 2023/4/29
 *  desc   : 首页viewModel
 */
class HomeViewModel(private val context: Context) : BaseViewModel() {
    private var _uiState = MutableLiveData<HomeUiModel>()
    val uiState: LiveData<HomeUiModel>
        get() = _uiState

    val budgetPercent = MutableLiveData<String>()   // 预算百分比
    val remainingBudget = MutableLiveData<String>() // 剩余预算
    val budget = MutableLiveData<String>() // 总预算
    val expenditure = MutableLiveData<String>() //支出

    val budgetList = mutableListOf<String>()   // 预算列表

    val getResult = MutableLiveData(Pair(first = false, second = false))    // 预算，支出获取结果

    val isExcess = MutableLiveData<Boolean>(false) // 是否超额

    val todoNum = MutableLiveData<Int>() //  待办事项数量
    val todoTitle = MutableLiveData<String>() //  待办事项名称
    val todoContent = MutableLiveData<String>() //  待办事项时间
    val todoDate = MutableLiveData<String>() //  待办事项日期

    //获取轮播图列表
    fun getBannerList() {
        launchOnUI {
            val bannerList = AppDataBase.instance.getBannerDao().queryAllBanner()
            emitUiState(showBannerList = bannerList)
        }
    }

    // 初始化预算列表
    fun intBudgetList() {
        for (i in 500..100000 step 500) {
            budgetList.add("$i")
        }
    }

    //获取预算
    fun getBudgetAmount() {
        launchOnUI {
            val budget = AppDataBase.instance.getBudgetDao().queryBudget()
            if (budget?.isNotEmpty() == true) {
                this@HomeViewModel.budget.value = budget[0].budgetValue
            }
        }
        getResult.value = Pair(true, getResult.value?.second!!)
    }

    //获取当月支出金额
    fun getExpenditureAmount() {
        val year = Calendar.getInstance().get(Calendar.YEAR).toString()
        val month = (Calendar.getInstance().get(Calendar.MONTH) + 1).toString()
        var amount = 0
        launchOnUI {
            val list = AppDataBase.instance.getExpenditureDao().queryAllByYearMonth(year, month)
            list?.forEach {
                amount += it.amount?.toInt()!!
            }
            expenditure.value = amount.toString()
        }
        getResult.value = Pair(getResult.value?.first!!, true)
    }

    //新增待办事项
    fun addTodo() {
        if (todoTitle.value.isNullOrEmpty() || todoDate.value.isNullOrEmpty() || todoContent.value.isNullOrEmpty()) {
            emitUiState(showError = context.getString(R.string.add_hint))
            return
        }

        launchOnUI {
            val todo = Todo(null)
            todo.apply {
                title = todoTitle.value
                date = todoDate.value
                content = todoContent.value
            }
            AppDataBase.instance.getTodoDao().insert(todo)
            emitUiState(addTodoSuccess = true)
        }
    }

    // 获取待办事项
    fun getTodoList() {
        launchOnUI {
            val todoList = AppDataBase.instance.getTodoDao().queryAllTodo()
            val list = mutableListOf<Todo>()
            todoList?.forEach {
                list.add(it)
            }
            // 根据日期前后升序
            list.sortBy {
                LocalDate.parse(
                    it.date,
                    DateTimeFormatter.ofPattern("yyyy-MM-dd")
                )
            }
            emitUiState(showTodoList = list)
        }
    }

    //删除待办事项
    fun deleteTodo(id: Int) {
        launchOnUI {
            AppDataBase.instance.getTodoDao().delete(Todo(id))
            emitUiState(deleteSuccess = true)
        }
    }

    private fun emitUiState(
        showProgress: Boolean = false,
        showError: String? = null,
        addTodoSuccess: Boolean = false,
        showBannerList: List<Banner>? = null,
        showTodoList: List<Todo>? = null,
        deleteSuccess: Boolean = false
    ) {
        val uiState =
            HomeUiModel(
                showProgress,
                showError,
                addTodoSuccess,
                showBannerList,
                showTodoList,
                deleteSuccess
            )
        _uiState.value = uiState
    }

    data class HomeUiModel(
        val showProgress: Boolean,
        val showError: String?,
        val addTodoSuccess: Boolean,
        val showBannerList: List<Banner>?,
        val showTodoList: List<Todo>?,
        val deleteSuccess: Boolean
    )
}