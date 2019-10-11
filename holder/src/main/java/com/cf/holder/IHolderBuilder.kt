package com.cf.holder

import android.view.ViewGroup

interface IHolderBuilder<VH : BaseHolder<*>> {
    fun createVH(parent: ViewGroup?): VH
    fun itemType(): Int
}