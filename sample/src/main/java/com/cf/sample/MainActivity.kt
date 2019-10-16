package com.cf.sample

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import com.cf.annotation.Holder
import com.cf.holder.BaseHolder
import com.cf.holder.BaseListHolder
import com.cf.holder.FooterHolder
import com.cf.holder.QuickAdapter
import com.cf.holder.divider.DefaultItemDecoration
import com.cf.holder.list.BaseDataLoader
import com.cf.holder.list.ListActivity
import com.cf.holder.list.toListener
import com.cf.utils.rx.ILoadingProgress
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item_banner.view.*
import kotlinx.android.synthetic.main.item_test.view.*

class MainActivity : ListActivity<MainLoadData>(), TestHolder.TestHolderCallback, ILoadingProgress {
    override fun hideLoading() {
        refreshLayout.isRefreshing = false
    }

    override fun showLoading() {
        if (isListManagerInit() && !listManager.getAdapter().mFooterData.isLoading) {
            refreshLayout.isRefreshing = true
        }
    }

    override fun layoutId(): Int = R.layout.activity_main

    override fun bindHolder(adapter: QuickAdapter) {
        adapter.apply {
            addHolder(TestHolderBuilder())
            addHolder(MainFooterBuilder())
            addHolder(BannerHolderBuilder())
        }
        getRecyclerView()?.addItemDecoration(DefaultItemDecoration.create())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        listManager.setRefreshListener(refreshLayout.toListener())
    }

    override fun getListDataLoader(): MainLoadData {
        return super.getListDataLoader().apply { loading = this@MainActivity }
    }

    override fun callback(h: TestHolder): Boolean {
        return h.adapterPosition % 2 == 0
    }
}

class MainLoadData : BaseDataLoader() {
    var loading: ILoadingProgress? = null
    override fun loadData(result: (List<*>) -> Unit, exception: (Exception) -> Unit) {
        loading?.showLoading()
        mutableListOf<Any>().apply {
            for (i in 0..10) {
                add("index ${dataManager.data?.size ?: i}")
                if (i == 5) {
                    add(BannerHolder.BannerData())
                }
            }
            android.os.Handler().postDelayed({
                result(this)
                loading?.hideLoading()
            }, 1000)
        }
    }
}

/**
 * 列表的item
 */
@Holder
class TestHolder @JvmOverloads constructor(parent: ViewGroup?, layoutId: Int = R.layout.item_test)
    : BaseHolder<String>(parent, layoutId) {
    @SuppressLint("SetTextI18n")
    override fun convert(data: String) {
        val c: TestHolderCallback? = adapterContext?.as2()
        itemView.tv.text = data + " callback:" + c?.callback(this)
    }

    /**
     * 测试和activity交互
     */
    interface TestHolderCallback {
        fun callback(h: TestHolder): Boolean
    }
}

@Holder
class BannerHolder @JvmOverloads constructor(parent: ViewGroup?, layoutId: Int = R.layout.item_banner)
    : BaseListHolder<BannerHolder.BannerData, MainLoadData>(parent, layoutId), ILoadingProgress {
    override fun hideLoading() {
        itemView.progressBar.visibility = View.GONE
    }

    override fun showLoading() {
        itemView.progressBar.visibility = View.VISIBLE
    }

    override fun bindHolder(adapter: QuickAdapter) {
        adapter.addHolder(ItemBannerHolderBuilder())
    }

    override fun convert(data: BannerData) {
        config?.onRefresh()
    }

    override fun getListDataLoader(): MainLoadData {
        return super.getListDataLoader().apply { loading = this@BannerHolder }
    }

    data class BannerData(var url: String = "")
}

@Holder
class ItemBannerHolder @JvmOverloads constructor(parent: ViewGroup?, layoutId: Int = R.layout.item_sub_banner)
    : BaseHolder<String>(parent, layoutId) {
    override fun convert(data: String) {
    }
}

/**
 * 自定义footer
 */
@Holder
class MainFooter @JvmOverloads constructor(parent: ViewGroup?, layoutId: Int = R.layout.item_main_footer)
    : FooterHolder(parent, layoutId)