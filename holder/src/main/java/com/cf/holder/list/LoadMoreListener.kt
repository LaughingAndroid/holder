package com.cf.holder.list

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

abstract class LoadMoreListener : RecyclerView.OnScrollListener() {
    /**
     * 标记是否正在向上滑动
     */
    private var isSlidingUpward = false

    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        super.onScrollStateChanged(recyclerView, newState)

        val manager = recyclerView.layoutManager as RecyclerView.LayoutManager?

        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
            //当状态是不滑动的时候

            val lastItemPosition = manager!!.findLastCompletelyVisibleItemPosition()
            val itemCount = manager.itemCount

            if (lastItemPosition == itemCount - 1 && isSlidingUpward) {
                onLoadMore()
                manager?.scrollToPositionWithOffset(itemCount - 1, 1000)
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
