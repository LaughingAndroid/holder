package com.cf.holder.list


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cf.extensions.getTypeClass
import com.cf.holder.QuickAdapter
import com.lau.holder.R

abstract class ListActivity<DL : DataLoader> : AppCompatActivity(), ListConfig<DL> {
    lateinit var listManager: ListManager
    abstract fun layoutId(): Int
    private var dataLoader: DL? = null

    fun isListManagerInit(): Boolean = ::listManager.isInitialized

    override fun createLayoutManager(): RecyclerView.LayoutManager? {
        return LinearLayoutManager(this)
    }

    override fun createAdapter(): QuickAdapter {
        return QuickAdapter(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutId())
        listManager = createListConfig()
    }

    open fun createListConfig(): ListManager = AutoLoadListManager(this)

    override fun getRecyclerView(): RecyclerView? {
        return findViewById(R.id.recyclerView)
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