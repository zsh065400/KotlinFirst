package cn.zhaoshuhao.kotlinfirst.contract

/**
 * Created by Scout
 * Created on 2017/8/14 16:47.
 */
interface Base {
    interface Present<out T : Base.View<Present<T>>> {
        fun setView(view: @UnsafeVariance T)
    }

    interface View<out T : Present<View<T>>> {
        fun setPresent(present: @UnsafeVariance T)
    }
}
