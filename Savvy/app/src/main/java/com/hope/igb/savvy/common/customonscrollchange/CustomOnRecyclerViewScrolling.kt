package com.hope.igb.savvy.common.customonscrollchange

import androidx.recyclerview.widget.RecyclerView
import kotlin.math.abs


class CustomOnRecyclerViewScrolling(private val listener: Listener): RecyclerView.OnScrollListener() {
    interface Listener {
        fun onRecyclerViewScrolledUp()
        fun onRecyclerViewScrolledDown()
    }

    private var previousScrollPosition = 0
    private val scrollThreshold = 50


    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        val currentScrollPosition = recyclerView.computeVerticalScrollOffset()

        val scrollDifference = abs(currentScrollPosition - previousScrollPosition)

        if (scrollDifference > scrollThreshold) {
            if (currentScrollPosition > previousScrollPosition) {
                // scrolling down
                listener.onRecyclerViewScrolledDown()
            } else {
                // scrolling up
                listener.onRecyclerViewScrolledUp()
            }
            previousScrollPosition = currentScrollPosition
        }
    }
}