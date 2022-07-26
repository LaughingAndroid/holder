package com.cf.holder.divider

import android.graphics.Color
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.cf.holder.*
import com.lau.holder.R
import java.util.*

class DefaultItemDecoration {
    companion object {
        @JvmStatic
        fun createBuilder(color:Int = 0xFFF4F5F7.toInt()): HorizontalDividerItemDecoration.Builder {
            val mSizeMap = HashMap<String, Int>()
            val marginLeftMap = HashMap<String, Int>()
            val marginRightMap = HashMap<String, Int>()
            return HorizontalDividerItemDecoration.Builder(HolderManager.application)
                    .colorProvider(FlexibleDividerDecoration.ColorProvider { position, parent ->
                        val manager = parent.layoutManager
                        if (null != manager) {
                            val view = manager.findViewByPosition(position)
                            if (null != view && getHolder(view) !is BaseHolder<*>) {
                                return@ColorProvider Color.TRANSPARENT
                            }
                        }
                        return@ColorProvider color

                    })
                    .marginProvider(object : HorizontalDividerItemDecoration.MarginProvider {
                        override fun dividerLeftMargin(position: Int, parent: RecyclerView): Int {
                            return getMarginLeft(position, parent, marginLeftMap)
                        }

                        override fun dividerRightMargin(position: Int, parent: RecyclerView): Int {
                            return getMarginRight(position, parent, marginRightMap)
                        }
                    })
                    .sizeProvider { position, parent ->
                        val size = getSize(position, parent, mSizeMap)
                        size
                    }
                    .showLastDivider()
        }

        @JvmStatic
        fun create(color:Int = 0xFFF4F5F7.toInt()): HorizontalDividerItemDecoration {
            return createBuilder(color).build()
        }

        private fun getSize(position: Int, parent: RecyclerView, sizeMap: MutableMap<String, Int>): Int {
            val manager = parent.layoutManager
            if (null != manager) {
                var size = 0
                val view = manager.findViewByPosition(position)
                if (null != view) {
                    if (getHolder(view) is ItemSize) {
                        size = (getHolder(view) as ItemSize).itemSize()
                        sizeMap["" + position] = size
                    }
                } else {
                    val temp = sizeMap["" + position]
                    size = temp ?: 0
                }
                return size
            }
            return 0
        }

        private fun getMarginLeft(position: Int, parent: RecyclerView, sizeMap: MutableMap<String, Int>): Int {
            val manager = parent.layoutManager
            if (null != manager) {
                var size = 0
                val view = manager.findViewByPosition(position)
                if (null != view) {
                    if (getHolder(view) is ItemLeftMargin) {
                        size = (getHolder(view) as ItemLeftMargin).marginLeft()
                        sizeMap["" + position] = size
                    }
                } else {
                    val temp = sizeMap["" + position]
                    size = temp ?: 0
                }
                return size
            }
            return 0
        }

        private fun getHolder(view: View?): Any? {
            return view?.getTag(R.id.holder)
        }

        private fun getMarginRight(position: Int, parent: RecyclerView, sizeMap: MutableMap<String, Int>): Int {
            val manager = parent.layoutManager
            if (null != manager) {
                var size = 0
                val view = manager.findViewByPosition(position)
                if (null != view) {
                    if (getHolder(view) is ItemRightMargin) {
                        size = (getHolder(view) as ItemRightMargin).marginRight()
                        sizeMap["" + position] = size
                    }
                } else {
                    val temp = sizeMap["" + position]
                    size = temp ?: 0
                }
                return size
            }
            return 0
        }
    }
}
