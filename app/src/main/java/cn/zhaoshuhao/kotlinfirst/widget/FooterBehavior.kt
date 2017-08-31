package cn.zhaoshuhao.kotlinfirst.widget

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.support.design.widget.CoordinatorLayout
import android.support.v4.view.ViewCompat
import android.support.v4.view.animation.FastOutSlowInInterpolator
import android.util.AttributeSet
import android.view.View
import android.view.ViewPropertyAnimator

/**
 * Created by Scout
 * Created on 2017/8/25 9:53.
 */
class FooterBehavior(private val context: Context, private val attributeSet: AttributeSet) : CoordinatorLayout.Behavior<View>(context, attributeSet) {
//    override fun layoutDependsOn(parent: CoordinatorLayout?, child: View?, dependency: View?): Boolean {
//        return dependency is AppBarLayout
//    }
//
//    override fun onDependentViewChanged(parent: CoordinatorLayout?, child: View?, dependency: View?): Boolean {
//        val translationY = Math.abs(dependency!!.y)
//        child?.translationY = translationY
////        log(LogType.DEBUG, "behavior", dependency!!.y.toString())
//        return true
//    }

    override fun onStartNestedScroll(coordinatorLayout: CoordinatorLayout?, child: View?, directTargetChild: View?, target: View?, nestedScrollAxes: Int): Boolean {
        return (nestedScrollAxes shl ViewCompat.SCROLL_AXIS_VERTICAL) != 0
    }

    private var transY = 0f

    override fun onNestedPreScroll(coordinatorLayout: CoordinatorLayout?, child: View?, target: View?, dx: Int, dy: Int, consumed: IntArray?) {
        val h: Float = (child!!.height + 100).toFloat()
        if (dy > 0 && transY <= h) {
            transY += dy
            if (transY > h) transY
            child!!.translationY = transY
        } else if (dy < 0) {
            transY += dy
            if (transY < 0) transY = 0f
            child!!.translationY = transY
        }
        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed)
    }

    private fun show(v: View) {
        val animator: ViewPropertyAnimator = v.animate().translationY(0f).setInterpolator(FastOutSlowInInterpolator()).setDuration(200)
        animator.setListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                v.visibility = View.VISIBLE
            }
        })
        animator.start()
    }

    private fun hide(v: View) {
        val animator: ViewPropertyAnimator = v.animate().translationY(v.height.toFloat()).setInterpolator(FastOutSlowInInterpolator()).setDuration(200)
        animator.setListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                v.visibility = View.GONE
            }
        })
        animator.start()
    }
}
