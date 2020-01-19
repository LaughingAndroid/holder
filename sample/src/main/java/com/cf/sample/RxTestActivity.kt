package com.cf.sample

import android.content.res.Resources
import android.os.Bundle
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import click
import com.cf.annotation.Holder
import com.cf.extensions.delayJob
import com.cf.holder.BaseHolder
import com.cf.holder.QuickAdapter
import com.cf.holder.divider.DefaultItemDecoration
import com.cf.holder.list.BaseDataLoader
import com.cf.holder.list.ListActivity
import io.reactivex.Observable
import kotlinx.android.synthetic.main.item_test.view.*

class RxTestActivity : ListActivity<RxDl>(), IAddLog {
    override fun bindHolder(adapter: QuickAdapter) {
        adapter.addHolder(ItemOptBuilder())
        getRecyclerView()?.addItemDecoration(DefaultItemDecoration.create())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setLoadMoreEnable(false)
    }

    override fun layoutId(): Int = R.layout.test_list_activity

    override fun addLog(data: Any) {
        listManager.getAdapter().addData(ArrayList<Any>().apply { add(data) })
    }
}

@Holder
class ItemOpt @JvmOverloads constructor(parent: ViewGroup?, layoutId: Int = R.layout.item_rx_opration)
    : BaseHolder<String>(parent, layoutId) {
    init {
        itemView.click {
            val d = mData ?: return@click
            when (d) {
                "concat" -> {
                    val list = createTestList()
                    Observable.concat<Any>(list).subscribe {
                        val msg = "subscribe test concat $it"
                        Logs.d(msg)
                        adapterContext?.targetAs2<IAddLog>()?.addLog(msg)
                    }
                }
                else -> {

                }
            }
        }
    }

    private fun createTestList(): List<Observable<Int>> {
        val list = mutableListOf<Observable<Int>>()
        for (i in 0..10) {
            list.add(Observable.create {
                delayJob((10 - i) * 100L) {
                    val msg = "task test rx concat $i"
                    Logs.d(msg)
                    adapterContext?.targetAs2<IAddLog>()?.addLog(msg)
                    it.onNext(i)
                    it.onComplete()
                }
            })
        }
        return list
    }

    override fun convert(data: String) {
        itemView.apply {
            tv.text = data
        }
    }

    override fun itemSize(): Int {
        return (2.5 * Resources.getSystem().displayMetrics.density).toInt()
    }
}

interface IAddLog {
    fun addLog(data: Any)
}

class RxDl : BaseDataLoader() {
    override fun loadData(result: (List<*>) -> Unit, exception: (Exception) -> Unit) {
        val list = mutableListOf<Any>()
        list.add("concat")
        list.add("rxDownload")
        delayJob(100) {
            result(list)
        }
    }
}