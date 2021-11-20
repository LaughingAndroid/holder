package com.cf.sample

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.PersistableBundle
import android.view.ViewGroup
import click
import com.cf.annotation.Holder
import com.cf.extensions.delayJob
import com.cf.extensions.showToast
import com.cf.holder.BaseHolder
import com.cf.holder.QuickAdapter
import com.cf.holder.list.BaseDataLoader
import com.cf.holder.list.ListActivity
import kotlinx.android.synthetic.main.item_task.view.*
import zlc.season.rxdownload4.utils.log
import java.lang.ref.WeakReference

/**
 *
 * @ClassName: HandlerTestActivity
 * @Description:
 * @Author: Laughing
 * @CreateDate: 2021/5/25 15:49
 * @Version:
 */
class HandlerTestActivity : ListActivity<HandlerDataLoader>() {
    override fun bindHolder(adapter: QuickAdapter) {
        adapter.addHolder(RunnableHolderBuilder())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setLoadMoreEnable(false)
    }

    override fun layoutId(): Int = R.layout.base_list
}

@Holder
class RunnableHolder @JvmOverloads constructor(parent: ViewGroup?, layoutId: Int = R.layout.item_task) : BaseHolder<RunnableData>(parent, layoutId) {
    init {
        itemView.click {
            "RunnableHolder click".log()
            mData?.task?.run()
        }
    }

    override fun convert(data: RunnableData) {
        itemView.titleTv.text = data.name
    }
}

class RunnableData(var name: String, var task: Runnable)

class HandlerDataLoader : BaseDataLoader() {
    override fun loadData(result: (List<*>) -> Unit, exception: (Exception) -> Unit) {
        val list = mutableListOf<Any>()
        list.add(RunnableData("Test Thread Handler", Runnable {
            Thread {
                Handler()
                "Thread Handler end".log()
            }.start()
        }))

        list.add(RunnableData("ReferenceQueue", Runnable {
        }))
        delayJob(100) {
            result(list)
        }
    }
}