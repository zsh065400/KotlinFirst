package cn.zhaoshuhao.kotlinfirst.utils

import android.text.TextUtils
import android.util.Base64


/**
 * Created by Scout
 * Created on 2017/9/29 18:21.
 */
/*正向加密*/
fun encrypt(s: String): String {
    if (TextUtils.isEmpty(s)) {
        return ""
    }
    return Base64.encodeToString(s.toByteArray(Charsets.UTF_8), Base64.DEFAULT);
}

fun String.doEncrypt(): String = encrypt(this)


