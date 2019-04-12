package com.ivianuu.listprefs

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.ivianuu.list.ItemViewHolder

/**
 * Divider decoration for preferences
 */
class PreferenceDividerDecoration(context: Context) : RecyclerView.ItemDecoration() {

    var style = Style.CATEGORIES

    var divider: Drawable? = null
        set(value) {
            field = value
            dividerHeight = divider?.intrinsicHeight ?: 0
        }

    var dividerHeight = 0

    init {
        val attrs =
            context.obtainStyledAttributes(intArrayOf(android.R.attr.dividerVertical))
        divider = attrs.getDrawable(0)
        attrs.recycle()
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val divider = divider ?: return

        val childCount = parent.childCount
        val width = parent.width

        for (childViewIndex in 0 until childCount) {
            val view = parent.getChildAt(childViewIndex)
            if (shouldDrawDivider(view, parent)) {
                val top = view.y.toInt() + view.height
                divider.setBounds(0, top, width, top + dividerHeight)
                divider.draw(c)
            }
        }
    }

    override fun getItemOffsets(
        outRect: Rect, view: View, parent: RecyclerView,
        state: RecyclerView.State
    ) {
        if (shouldDrawDivider(view, parent)) {
            outRect.bottom = dividerHeight
        }
    }

    private fun shouldDrawDivider(view: View, parent: RecyclerView) = when (style) {
        Style.CATEGORIES -> shouldDrawDividerCategories(view, parent)
        Style.ITEMS -> shouldDrawDividerItems(view, parent)
    }

    private fun shouldDrawDividerCategories(view: View, parent: RecyclerView): Boolean {
        val index = parent.indexOfChild(view)

        val isNextCategory = if (index < parent.childCount - 1) {
            val nextHolder = parent.getChildViewHolder(parent.getChildAt(index + 1))
            nextHolder is ItemViewHolder && nextHolder.item is CategoryPreferenceItem
        } else {
            false
        }

        if (isNextCategory) {
            return true
        }

        return false
    }

    private fun shouldDrawDividerItems(view: View, parent: RecyclerView): Boolean {
        val holder = parent.getChildViewHolder(view)

        val isCategory =
            holder is ItemViewHolder && holder.item is CategoryPreferenceItem

        if (isCategory) return false

        val index = parent.indexOfChild(view)

        val isNextCategory = if (index < parent.childCount - 1) {
            val nextHolder = parent.getChildViewHolder(parent.getChildAt(index + 1))
            nextHolder is ItemViewHolder && nextHolder.item is CategoryPreferenceItem
        } else {
            false
        }

        return !isNextCategory
    }

    enum class Style {
        CATEGORIES, ITEMS
    }
}