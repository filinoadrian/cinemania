package com.far_sstrwnt.cinemania.ui

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class MovieGridItemDecoration(private val padding: Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View,
                                parent: RecyclerView, state: RecyclerView.State) {
        outRect.left = padding
        outRect.right = padding
        outRect.top = padding
        outRect.bottom = padding
    }
}