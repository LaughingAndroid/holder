package com.cf.holder.list

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

abstract class LoadMoreListener : RecyclerView.OnScrollListener() {
    /**
     * 标记是否正在向上滑动
     */
    private var isSlidingUpward = false

    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        super.onScrollStateChanged(recyclerView, newState)


        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
            //当状态是不滑动的时候
            var itemCount = 0
            var lastItemPosition = 0
            var scrollToPositionWithOffset = {}
            if (recyclerView.layoutManager is LinearLayoutManager) {
                val manager = recyclerView.layoutManager as LinearLayoutManager
                lastItemPosition = manager.findLastCompletelyVisibleItemPosition()
                itemCount = manager.itemCount
                scrollToPositionWithOffset = {
                    manager.scrollToPositionWithOffset(itemCount - 1, 1000)
                }
            } else if (recyclerView.layoutManager is StaggeredGridLayoutManager) {
                val manager = recyclerView.layoutManager as StaggeredGridLayoutManager
                val aa = IntArray(5)
                manager.findLastCompletelyVisibleItemPositions(aa)
                var index = 4
                while (index > 0) {
                    if (aa[index] > 0) {
                        lastItemPosition = aa[index]
                        break
                    }
                }
                itemCount = manager.itemCount
            }


            if (lastItemPosition == itemCount - 1 && isSlidingUpward) {
                onLoadMore()
                scrollToPositionWithOffset()
            }
        }
    }


    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        // 大于0表示正在向上滑动，小于等于0表示停止或向下滑动
        isSlidingUpward = dy > 0
    }

    /**
     * 加载更多数据的方法
     */
    abstract fun onLoadMore()
}
