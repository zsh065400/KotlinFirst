package cn.zhaoshuhao.kotlinfirst.mvp.view

import cn.zhaoshuhao.kotlinfirst.mvp.present.BasePresent

/**
 * Created by Scout
 * Created on 2017/8/12 23:55.
 */
interface BaseView<out Present : BasePresent<BaseView<Present>>> {
    val present: Present


    //TODO: /*完成View和Present的基本功能定义*/
}
