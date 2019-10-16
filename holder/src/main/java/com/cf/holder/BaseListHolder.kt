package com.cf.holder

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cf.extensions.getTypeClass
import com.cf.holder.HolderManager.application
import com.cf.holder.list.BaseListManager
import com.cf.holder.list.DataLoader
import com.cf.holder.list.ListConfig
import com.cf.holder.list.ListManager
import com.lau.holder.R

abstract class BaseListHolder<T, DL : DataLoader> : BaseHolder<T>, ListConfig<DL> {
    constructor(parent: ViewGroup?, layoutId: Int) : super(parent, layoutId)
    constructor(view: View) : super(view)

    private var dataLoader: DL? = null
    var config: ListManager? = null
    override fun onContextSet() {
        super.onContextSet()
        config ?: createListManager().apply {
            config = this
            getAdapter().setFooterEnable(false)
        }

    }

    override fun createLayoutManager(): LinearLayoutManager? {
        return LinearLayoutManager(adapterContext?.activity(), LinearLayoutManager.HORIZONTAL, false)
    }

    open fun createListManager(): ListManager = BaseListManager(this)

    override fun createAdapter(): QuickAdapter {
        return adapterContext?.let { QuickAdapter(it) }
                ?: QuickAdapter(application.toAdapterContext())
    }

    override fun getRecyclerView(): RecyclerView? {
        return findViewById(R.id.recyclerView)
    }

    override fun getListDataLoader(): DL {
        return dataLoader
                ?: (javaClass.getTypeClass(DataLoader::class.java)?.newInstance() as DL).apply {
                    dataLoader = this
                }
    }
}