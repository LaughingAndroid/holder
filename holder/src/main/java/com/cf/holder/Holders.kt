package com.cf.holder


inline fun <reified T> getHolderBuilder(): IHolderBuilder<*> {
    val className = T::class.java.simpleName + "Builder"
    return Class.forName(className).newInstance() as IHolderBuilder<*>
}

inline fun <reified T> QuickAdapter.bindHolder(key: Int? = null) {
    addHolder(getHolderBuilder<T>(), key)
}

