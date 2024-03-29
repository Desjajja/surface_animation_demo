package com.chenyangqi.nested.scroll.demo3

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView

/**
 * @describe
 * @author chenyangqi
 * @time 2021/7/23 11:13
 */
class Demo3HeaderBehavior<V : View>(context: Context, attributeSet: AttributeSet?) :
    CoordinatorLayout.Behavior<V>(context, attributeSet) {

    override fun onStartNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: V,
        directTargetChild: View,
        target: View,
        axes: Int,
        type: Int
    ): Boolean {
        return axes != 0 && ViewCompat.SCROLL_AXIS_VERTICAL != 0
    }

    override fun onNestedPreScroll(
        coordinatorLayout: CoordinatorLayout,
        child: V,
        target: View,
        dx: Int,
        dy: Int,
        consumed: IntArray,
        type: Int
    ) {
        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed, type)
        if (target !is RecyclerView) {
            return
        }
        val finalY = child.translationY - dy
        if (finalY > -child.height) {
            child.translationY = dy.toFloat()
        } else if (finalY > 0) {
            child.translationY = 0F
        }
    }

}