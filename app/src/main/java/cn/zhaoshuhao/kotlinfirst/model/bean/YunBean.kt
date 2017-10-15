package cn.zhaoshuhao.kotlinfirst.model.bean

import cn.bmob.v3.BmobObject
import cn.bmob.v3.BmobUser
import cn.bmob.v3.datatype.BmobFile

/**
 * Created by Scout
 * Created on 2017/9/29 18:02.
 */
data class User(val head: BmobFile? = null, val nick: String = "",
                val sex: String = "", val address: String = "",
                val age: String) : BmobUser()

/*订单表，商品列表使用ShoppingCart*/
data class Order(val user: User,
                 val order_id: String,
                 val price: String,
                 val products: String,
                 val num: String,
                 var status: String) : BmobObject("Order")

/*收藏表，从数据来源提取公共字段，最好可以建立表关联*/
data class Star(val user: User,
                val imgUrl: String = "",
                val title: String = "",
                val price: String = "",
                val url: String = "")
    : BmobObject("Star")
