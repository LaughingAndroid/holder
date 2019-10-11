package com.cf.holder.list


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.cf.holder.QuickAdapter

abstract class ListActivity<VM : LoadData> : AppCompatActivity(), ListAction<VM> {
    lateinit var listConfig: ListManager<VM>

    override fun createLayoutManager(): LinearLayoutManager? {
        return LinearLayoutManager(this)
    }

    override fun createAdapter(): QuickAdapter {
        return QuickAdapter(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        listConfig = ListManager(this)
        listConfig.loadData()
    }

    open fun setLoadMoreEnable(enable: Boolean) {
        if (::listConfig.isInitialized) {
            listConfig.enableLoadMore = enable
        }
    }

    open fun setRefreshEnable(enable: Boolean) {
        if (::listConfig.isInitialized) {
            listConfig.enableRefresh = enable
        }
    }
}