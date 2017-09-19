package cn.zhaoshuhao.kotlinfirst.model.bean

import java.io.Serializable

/**
 * Created by Scout
 * Created on 2017/8/12 16:14.
 */
data class TypeInfo(val text: String, val icon: Int)

data class WebViewInfo(val title: String, val url: String) : Serializable

data class ShoppingCart(val id: String, val name: String, val img: String,
                        val price: String, val value: String, var num: String,
                        var checked: Boolean)
