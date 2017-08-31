package cn.zhaoshuhao.kotlinfirst.model.network.common

/**
 * Created by Scout
 * Created on 2017/8/14 21:29.
 */
interface LoadListener<in T> {
    fun onSuccess(data: T?)
    fun onFailed(t: Throwable?) = t?.printStackTrace()
    fun onLoadLocalData()
}



