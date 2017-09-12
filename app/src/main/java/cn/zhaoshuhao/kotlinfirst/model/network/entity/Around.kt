package cn.zhaoshuhao.kotlinfirst.model.network.entity

import java.io.Serializable

/**
 * Created by Scout
 * Created on 2017/9/10 16:03.
 */
data class Around(var id: String,
                  var name: String,
                  var `type`: String,
                  var price: Int,
                  var origin: Int,
                  var img: String,
                  var peisong: String,
                  var createTime: String,
                  var modifyTime: String,
                  var exercise: String,
                  var delivery: Int,
                  var typeflag: String) : Serializable
