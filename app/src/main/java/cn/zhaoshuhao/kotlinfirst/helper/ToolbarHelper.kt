package cn.zhaoshuhao.kotlinfirst.helper

import android.app.Activity
import android.support.v7.widget.Toolbar

/**
 * Created by Scout
 * Created on 2017/7/28 16:54.
 */
fun Activity.customToolbar(toolbar: Toolbar, custom: Toolbar.() -> Unit): Toolbar {
    toolbar.custom()
    return toolbar
}
