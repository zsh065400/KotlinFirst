package cn.zhaoshuhao.kotlinfirst.utils

import android.util.SparseArray
import android.view.View

/**
 * Created by Scout
 * Created on 2017/7/28 19:53.
 */
/*
* 利用接收者是同一对象的原理，扩展获取其保存的值并作单独缓存
* */
fun <T : View> View.findViewOften(viewId: Int): T {
    var viewHolder: SparseArray<View> = tag as? SparseArray<View> ?: SparseArray()
    tag = viewHolder
    var childView: View? = viewHolder.get(viewId)
    if (null == childView) {
        childView = findViewById(viewId)
        viewHolder.put(viewId, childView)
    }
    return childView as T
}
