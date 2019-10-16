package com.cf.holder.list

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

interface PullRefreshListener {
    fun setOnRefresh(onRefresh: () -> Unit)
    fun complete()
}

fun SwipeRefreshLayout.toListener(): PullRefreshListener {
    return object : PullRefreshListener {
        override fun setOnRefresh(onRefresh: () -> Unit) {
            setOnRefreshListener(onRefresh)
        }

        override fun complete() {
            isRefreshing = false
        }
    }
}