package com.cf.holder.list

import androidx.lifecycle.LifecycleOwner
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
class ListManager<VM : LoadData>(private var contextConfig: ListAction<VM>) {
    lateinit var adapter: QuickAdapter
    var enableRefresh = true
    var enableLoadMore = true
    private var canLoadMoreFromData = true

    init {
        contextConfig.getRecycleView()?.let { recycleView ->
            adapter = contextConfig.createAdapter()
            contextConfig.createLayoutManager()?.apply {
                recycleView.layoutManager = this
                addLoadMoreListener(recycleView)
            }
            recycleView.adapter = adapter
            contextConfig.bindHolder(adapter)
            addDataListener()
        }

    }

    private fun addDataListener() {
        contextConfig.getListVM().onListSuccess {
            onSuccess(it)
        }
    }

    private fun addLoadMoreListener(recycleView: RecyclerView) {
        contextConfig.getListVM().onListError {
            adapter.mFooterData.isLoading = false
        }

        val scrollListener = object : LoadMoreListener() {
            override fun onLoadMore() {
                if (enableLoadMore && canLoadMoreFromData && !adapter.mFooterData.isLoading) {
                    loadMore()
                }
            }
        }
        recycleView.addOnScrollListener(scrollListener)
    }


    fun loadData() {
        contextConfig.getListVM().load()
    }

    private fun onSuccess(result: ListResult) {
        result.apply {
            if (page == ListDataManager.START) {
                adapter.setData(list)
            } else {
                adapter.addData(list)
            }
            canLoadMoreFromData = result.list?.isEmpty() != true

            checkFullScreenAndLoadMore(contextConfig.getRecycleView())
        }
    }

    private fun loadMore() {
        adapter.mFooterData.isLoading = true
        contextConfig.getListVM().load()
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


interface ListAction<VM : LoadData> : LifecycleOwner {
    fun bindHolder(adapter: QuickAdapter)
    fun createLayoutManager(): LinearLayoutManager?
    fun getRecycleView(): RecyclerView?
    fun createAdapter(): QuickAdapter
    fun getListVM(): VM
}

interface LoadData {
    fun load()
    fun onListSuccess(result: (ListResult) -> Unit)
    fun onListError(exception: (Exception) -> Unit)
}

data class ListResult(var page: Int = ListDataManager.START, var list: List<*>?)
