package com.cf.holder

import android.app.Activity
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.cf.annotation.Holder
import com.lau.holder.R
import com.lau.holder.FooterHolderBuilder

open class QuickAdapter : RecyclerView.Adapter<BaseHolder<*>> {
    private var mActivity: Activity? = null
    private var mFragment: Fragment? = null
    private val mFooterCount: Int = 1
    private var mHolderBuilderMap: HashMap<Int, IHolderBuilder<*>> = hashMapOf()
    private var mData: MutableList<*> = mutableListOf<Any>()
    val mFooterData: FooterHolder.Data = FooterHolder.Data()

    constructor(a: Activity) {
        mActivity = a
    }

    constructor(f: Fragment) {
        mFragment = f
    }

    init {
        addHolder(FooterHolderBuilder())
    }

    fun addData(data: List<*>?) {
        data?.let {
            mData.addAll(data as List<Nothing>)
            clearBadData()
            notifyDataSetChanged()
        }
        mFooterData.isLoading = false
    }

    fun setData(data: List<*>?) {
        data?.let {
            mData.clear()
            mData.addAll(data as List<Nothing>)
            clearBadData()
            notifyDataSetChanged()
        }
        mFooterData.isLoading = false
    }

    private fun clearBadData() {
        mData.iterator().apply {
            while (hasNext()) {
                if (next() == null) remove()
            }
        }
    }

    fun <H : IHolderBuilder<*>> addHolder(holder: H, key: Int? = null) {
        mHolderBuilderMap[key ?: holder.itemType()] = holder
    }


    override fun onBindViewHolder(holder: BaseHolder<*>, position: Int) {
        val item = getItem(position)
        item?.let {
            holder.mAdapterCount = getRealItemCount()
            holder.setData(item)
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseHolder<*> {
        if (mHolderBuilderMap.containsKey(viewType)) {
            var holderClass = mHolderBuilderMap[viewType]
            holderClass?.let {
                return holderClass.createVH(parent).also { holder ->
                    holder.mPageContext = mActivity ?: mFragment
                }
            }
        }
        // If error return empty holder
        return BaseHolder.getEmptyHolder(parent)
    }

    override fun getItemCount(): Int {
        return mData.size + mFooterCount
    }

    open fun getRealItemCount(): Int {
        return itemCount - mFooterCount
    }

    override fun getItemViewType(position: Int): Int {
        if (position == itemCount - 1) {
            return mFooterData.javaClass.hashCode()
        }
        val item = getItem(position)
        return if (item is MultipleData) {
            item.itemType()
        } else {
            item?.javaClass?.hashCode() ?: -1
        }
    }


    fun getItem(position: Int): Any? {
        return if (position >= 0 && position < mData.size) mData[position] else mFooterData
    }
}

@Holder
open class FooterHolder @JvmOverloads constructor(parent: ViewGroup?, layoutId: Int = R.layout.item_footer) : BaseHolder<FooterHolder.Data>(parent, layoutId) {
    var height = 0

    init {
        height = itemView.layoutParams.height
    }

    override fun convert(data: Data) {
        observe(data)
        itemView.layoutParams.height = if (data.isLoading) height else 0
        itemView.layoutParams = itemView.layoutParams
    }

    private var hasObserve = false

    private fun observe(data: Data) {
        if (!hasObserve) {
            hasObserve = true
            mLifecycle?.let {
                data.liveData.observe(object : LifecycleOwner {
                    override fun getLifecycle(): Lifecycle {
                        return it
                    }
                }, androidx.lifecycle.Observer {
                    refresh()
                })
            }
        }
    }

    override fun itemSize(): Int {
        return 0
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        mData?.liveData
    }

    class Data {
        var isLoading: Boolean = false
            set(value) {
                field = value
                liveData.value = this
            }

        var text: String? = ""
            set(value) {
                field = value
                liveData.value = this
            }
        val liveData: MutableLiveData<Data> = MutableLiveData()
    }
}

interface MultipleData {
    fun itemType(): Int
}