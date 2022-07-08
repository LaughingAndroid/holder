package com.cf.holder.list


import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cf.extensions.getTypeClass
import com.cf.holder.QuickAdapter
import com.lau.holder.R

abstract class ListFragment<DL : DataLoader> : Fragment(), ListConfig<DL> {
    lateinit var listManager: ListManager
    var rootView: View? = null
    private var dataLoader: DL? = null

    fun isListManagerInit(): Boolean = ::listManager.isInitialized

    override fun createLayoutManager(): RecyclerView.LayoutManager? {
        return LinearLayoutManager(this.context)
    }

    override fun createAdapter(): QuickAdapter {
        return QuickAdapter(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rootView = view
        listManager = createListConfig()
    }

    open fun createListConfig(): ListManager = AutoLoadListManager(this)

    override fun getRecyclerView(): RecyclerView? {
        return rootView?.findViewById(R.id.recyclerView)
    }


    override fun getListDataLoader(): DL {
        return dataLoader
                ?: (javaClass.getTypeClass(DataLoader::class.java)?.newInstance() as DL).apply {
                    dataLoader = this
                }
    }

    open fun setLoadMoreEnable(enable: Boolean) {
        if (::listManager.isInitialized) {
            listManager.setLoadMoreEnable(enable)
        }
    }

    open fun setRefreshEnable(enable: Boolean) {
        if (::listManager.isInitialized) {
            listManager.setRefreshEnable(enable)
        }
    }
}