package com.cf.holder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import java.lang.reflect.ParameterizedType


inline fun <reified T> getHolderBuilder(): IHolderBuilder<*> {
    val className = T::class.java.simpleName + "Builder"
    return Class.forName(className).newInstance() as IHolderBuilder<*>
}

inline fun <reified T> QuickAdapter.bindHolder(key: Int? = null) {
    addHolder(getHolderBuilder<T>(), key)
}

fun BaseHolder<*>.getViewBindingClass(): Class<*>? {
    try {
        val type = javaClass.genericSuperclass as? ParameterizedType
        val clz = type?.actualTypeArguments?.get(1)
        return clz as Class<*>
    } catch (e: Exception) {
        return null
    }
}

fun BaseHolder<*>.createViewBinding(): Any? {
    val method = getViewBindingClass()?.getMethod(
        "inflate", LayoutInflater::class.java, ViewGroup::class.java, Boolean::class.java
    )
    val inflater = LayoutInflater.from(HolderManager.application)
    val binding = method?.invoke(null, inflater, null, false)
    return binding
}

fun Any.rootView(): View {
    val rootMethod = javaClass.getMethod("getRoot")
    return rootMethod.invoke(this) as View
}

