package com.cf.holder

import android.view.View
import com.lau.holder.R

abstract class BaseBindingHolder<T, VB>(itemView: View) : BaseHolder<T>(itemView) {
    val binding: VB by lazy(mode = LazyThreadSafetyMode.NONE) {
        itemView.getTag(R.id.holder_bindng) as VB
    }
}