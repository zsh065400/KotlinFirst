package cn.zhaoshuhao.kotlinfirst.mvp.present

import cn.zhaoshuhao.kotlinfirst.mvp.view.BaseView

/**
 * Created by Scout
 * Created on 2017/8/12 23:54.
 */
interface BasePresent<out View : BaseView<BasePresent<View>>> {
    var view: @UnsafeVariance View
}
