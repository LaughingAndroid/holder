package com.cf.sample

import android.view.ViewGroup
import com.cf.annotation.Holder
import com.cf.holder.BaseHolder
import com.cf.holder.QuickAdapter
import com.cf.holder.list.BaseDataLoader
import com.cf.holder.list.ListActivity

/**
 *
 * @ClassName: TestListActivity
 * @Description:
 * @Author: Laughing
 * @CreateDate: 2019/10/16 14:37
 * @Version: 1.7.0
 */
class TestListActivity : ListActivity<TestListDL>() {
    override fun layoutId(): Int = R.layout.test_list_activity

    override fun bindHolder(adapter: QuickAdapter) {
        adapter.addHolder(TestListHolderBuilder())
    }

}

@Holder
class TestListHolder @JvmOverloads constructor(parent: ViewGroup?, layoutId: Int = R.layout.item_test) : BaseHolder<String>(parent, layoutId) {
    override fun convert(data: String) {
        // 业务代码
    }
}

class TestListDL : BaseDataLoader() {
    override fun loadData(result: (List<*>) -> Unit, exception: (Exception) -> Unit) {
        val list = mutableListOf<Any>()
        result(list)
    }
}