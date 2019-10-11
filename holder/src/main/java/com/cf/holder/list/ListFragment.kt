package com.cf.holder.list


import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.cf.holder.QuickAdapter

abstract class ListFragment<VM : LoadData> : Fragment(), ListAction<VM> {
    lateinit var listConfig: ListManager<VM>

    override fun createLayoutManager(): LinearLayoutManager? {
        return LinearLayoutManager(this.context)
    }

    override fun createAdapter(): QuickAdapter {
        return QuickAdapter(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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