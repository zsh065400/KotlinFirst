package cn.zhaoshuhao.kotlinfirst.utils

import android.util.Log

/**
 * Created by Scout
 * Created on 2017/8/12 16:19.
 */

enum class LogType {
    DEBUG, ERROR, WARNING, INFO;
}

fun log(type: LogType, tag: String, msg: String) =
        when (type) {
            LogType.DEBUG -> Log.d(tag, msg)
            LogType.ERROR -> Log.e(tag, msg)
            LogType.WARNING -> Log.w(tag, msg)
            LogType.INFO -> Log.i(tag, msg)
        }
