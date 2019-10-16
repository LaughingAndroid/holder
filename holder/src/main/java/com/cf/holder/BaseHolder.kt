package com.cf.holder

import android.content.Context
import android.content.res.Resources
import android.util.Log
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.IdRes
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.cf.extensions.getTypeClass
import com.cf.holder.HolderManager.application
import com.cf.holder.divider.ItemSize
import com.lau.holder.BuildConfig
import com.lau.holder.R
import java.lang.reflect.ParameterizedType

@Suppress("LeakingThis")
abstract class BaseHolder<T> : RecyclerView.ViewHolder, DefaultLifecycleObserver, ItemSize {

    companion object {
        fun getContext(parent: View?): Context {
            return if (parent == null) application else parent.context
        }

        private fun createItemView(parent: ViewGroup?, layoutId: Int): View {
            return LayoutInflater.from(getContext(parent)).inflate(layoutId, parent, false)
        }

        fun getEmptyHolder(parent: ViewGroup?): BaseHolder<Any> {
            return object : BaseHolder<Any>(TextView(application)) {
                override fun convert(t: Any) {
                    if (BuildConfig.DEBUG) {
                        itemView.layoutParams?.height = 0
                    }
                }
            }
        }
    }

    var mData: T? = null
        set(value) {
            field = value
            value?.apply {
                convert(value)
            }
        }

    fun setData(d: Any?) {
        mData = d as T
    }


    var mLifecycle: Lifecycle? = null
        set(value) {
            field = value
            value?.addObserver(this)
        }

    constructor(parent: ViewGroup? = null, layoutId: Int) : super(
            createItemView(
                    parent,
                    layoutId
            )
    )

    constructor(view: View) : super(view)


    init {
        itemView.setTag(R.id.holder, this)
    }

    var adapterContext: AdapterContext? = null
        set(value) {
            field = value
            field?.apply {
                val owner: LifecycleOwner? = (target() as? LifecycleOwner)
                        ?: activity() as? LifecycleOwner
                mLifecycle = owner?.lifecycle
                onContextSet()
            }

        }
    var mAdapterCount: Int = 0
    private var views: SparseArray<View>? = null

    fun <T : View> findViewById(@IdRes viewId: Int): T? {
        var view: View? = views?.get(viewId)
        if (view == null) {
            view = itemView.findViewById(viewId)
            views?.put(viewId, view)
        }
        return view as? T
    }

    fun getContext(): Context {
        var context: Context? = adapterContext?.activity()
        // check
        if (context == null) {
            log("context == null")
        }
        return context ?: application!!
    }

    abstract fun convert(data: T)

    open fun onContextSet() {
    }

    fun refresh() {
        (mData as? T)?.apply {
            convert(this)
        }
    }

    fun log(msg: Any?) {
        if (BuildConfig.DEBUG) {
            Log.d(javaClass.simpleName + "@${hashCode()}", "$msg")
        }
    }

    override fun itemSize(): Int {
        return (0.5 * Resources.getSystem().displayMetrics.density).toInt()
    }

    override fun marginLeft(): Int {
        return 0
    }

    override fun marginRight(): Int {
        return 0
    }

    override fun onPause(owner: LifecycleOwner) {
        log("onPause")
    }

    override fun onResume(owner: LifecycleOwner) {
        log("onResume")
    }

    override fun onDestroy(owner: LifecycleOwner) {
        mLifecycle?.removeObserver(this)
        log("onDestroy")
    }

    fun clearViewsCache() {
        views?.clear()
    }
}


fun IHolderBuilder<*>.getItemTypeFromDataClass(): Int {
    return try {
        val builderType = javaClass.genericInterfaces[0] as ParameterizedType
        val holderClass = (builderType.actualTypeArguments[0] as Class<*>)
        val holderData = holderClass.getTypeClass(Any::class.java)
        holderData.hashCode()
    } catch (e: Exception) {
        e.printStackTrace()
        0
    }
}