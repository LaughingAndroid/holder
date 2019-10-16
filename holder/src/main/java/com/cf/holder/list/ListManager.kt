package com.cf.holder.list

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.cf.holder.QuickAdapter

/**
 *
 * @ClassName: ListManager
 * @Description:
 * @Author: Laughing
 * @CreateDate: 2019/4/17 12:42
 * @Version: 1.1.0
 */
interface ListManager {
    fun onRefresh()
    fun loadMore()
    fun setRefreshEnable(enable: Boolean)
    fun setLoadMoreEnable(enable: Boolean)
    fun setRefreshListener(listener: PullRefreshListener?)
    fun getAdapter(): QuickAdapter
}

open class BaseListManager<Loader : DataLoader>(var contextConfig: ListConfig<Loader>) : ListManager {
    var enableRefresh = true
    var enableLoadMore = true
    lateinit var _adapter: QuickAdapter
    var pullRefreshListener: PullRefreshListener? = null

    init {
        contextConfig.getRecyclerView()?.let { recycleView ->
            _adapter = contextConfig.createAdapter()
            contextConfig.createLayoutManager()?.apply {
                recycleView.layoutManager = this
            }
            recycleView.adapter = _adapter
            contextConfig.bindHolder(_adapter)
        }

        onRefresh()
    }

    override fun setRefreshEnable(enable: Boolean) {
        enableRefresh = enable
    }

    override fun setLoadMoreEnable(enable: Boolean) {
        enableLoadMore = enable
    }

    override fun setRefreshListener(listener: PullRefreshListener?) {
        this.pullRefreshListener = listener
        this.pullRefreshListener?.setOnRefresh { onRefresh() }
    }

    override fun onRefresh() {
        contextConfig.getListDataLoader().load(true, {
            onSuccess(it)
        }, {
            _adapter.mFooterData.isLoading = false
        })
    }


    override fun loadMore() {
        _adapter.mFooterData.isLoading = true
        contextConfig.getListDataLoader().load(false, {
            onSuccess(it)
        }, {
            onFailed(it)
        })
    }

    open fun onSuccess(result: PageData) {
        pullRefreshListener?.complete()
        result.apply {
            if (page == ListDataImpl.START) {
                _adapter.setData(list)
            } else {
                _adapter.addData(list)
            }
        }
    }

    open fun onFailed(exception: Exception) {
        pullRefreshListener?.complete()
        _adapter.mFooterData.isLoading = false
    }

    override fun getAdapter(): QuickAdapter {
        return _adapter
    }

}

class AutoLoadListManager<VM : DataLoader>(config: ListConfig<VM>) : BaseListManager<VM>(config) {
    private var canLoadMoreFromData = true

    init {
        config.getRecyclerView()?.let { addLoadMoreListener(it) }
    }

    private fun addLoadMoreListener(recycleView: RecyclerView) {
        val scrollListener = object : LoadMoreListener() {
            override fun onLoadMore() {
                if (enableLoadMore && canLoadMoreFromData && !_adapter.mFooterData.isLoading) {
                    loadMore()
                }
            }
        }
        recycleView.addOnScrollListener(scrollListener)
    }

    override fun onSuccess(result: PageData) {
        super.onSuccess(result)
        canLoadMoreFromData = result.list?.isEmpty() != true
        checkFullScreenAndLoadMore(contextConfig.getRecyclerView())
    }

    private fun checkFullScreenAndLoadMore(recyclerView: RecyclerView?) {
        if (recyclerView == null || !enableLoadMore || !canLoadMoreFromData) return

        val layoutManager = recyclerView.layoutManager ?: return
        if (layoutManager is LinearLayoutManager) {
            recyclerView.postDelayed({
                if (layoutManager.findLastCompletelyVisibleItemPosition() + 1 == layoutManager.itemCount) {
                    loadMore()
                }
            }, 50)
        } else if (layoutManager is StaggeredGridLayoutManager) {
            val staggeredGridLayoutManager = layoutManager as StaggeredGridLayoutManager
            recyclerView.postDelayed({
                val positions = IntArray(staggeredGridLayoutManager.spanCount)
                staggeredGridLayoutManager.findLastCompletelyVisibleItemPositions(positions)
                val pos = getTheBiggestNumber(positions) + 1
                if (pos == staggeredGridLayoutManager.itemCount) {
                    loadMore()
                }
            }, 50)
        }
    }

    private fun getTheBiggestNumber(numbers: IntArray?): Int {
        var tmp = -1
        if (numbers == null || numbers.isEmpty()) {
            return tmp
        }
        for (num in numbers) {
            if (num > tmp) {
                tmp = num
            }
        }
        return tmp
    }
}

/**
 * 生成一个列表页的基础配置
 */
interface ListConfig<DL : DataLoader> {
    /**
     * 把holder和adapter关联
     */
    fun bindHolder(adapter: QuickAdapter)

    fun createLayoutManager(): LinearLayoutManager?
    fun getRecyclerView(): RecyclerView?
    fun createAdapter(): QuickAdapter
    /**
     * 数据加载器
     */
    fun getListDataLoader(): DL
}

/**
 * 数据加载器
 */
interface DataLoader {
    fun load(refresh: Boolean = false, result: (PageData) -> Unit, exception: (Exception) -> Unit)
}

/**
 * 基于page的数据加载器
 */
abstract class BaseDataLoader : DataLoader {
    val dataManager = ListDataImpl<Any>()
    override fun load(refresh: Boolean, result: (PageData) -> Unit, exception: (Exception) -> Unit) {
        if (refresh) {
            dataManager.resetPage()
        }
        loadData({
            val data = PageData(dataManager.page, it)
            result(data)
            dataManager.putCache(data)
            dataManager.pageAdd()
        }, exception)
    }

    abstract fun loadData(result: (List<*>) -> Unit, exception: (Exception) -> Unit)
}


data class PageData(var page: Int = ListDataImpl.START, var list: List<*>?)
