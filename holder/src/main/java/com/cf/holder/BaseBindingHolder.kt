package com.cf.holder

import android.view.View
import android.view.ViewGroup
import java.lang.reflect.ParameterizedType

abstract class BaseBindingHolder<T, VB>(parent: ViewGroup? = null, layoutId: Int) :
    BaseHolder<T>(parent, layoutId) {
    val binding: VB by lazy(mode = LazyThreadSafetyMode.NONE) {
        val type = javaClass.genericSuperclass
        val clazz = (type as ParameterizedType).actualTypeArguments[1] as Class<VB>
        val method = clazz.getMethod("bind", View::class.java)
        method.invoke(null, itemView) as VB
    }
}