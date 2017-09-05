package cn.zhaoshuhao.kotlinfirst.model.network.entity

import java.io.Serializable

/**
 * Created by Scout
 * Created on 2017/8/14 21:11.
 */
data class Banner(var id: Int,
                  var title: String,
                  var image_url: String,
                  var to_url: String)

data class Film(var filmId: String,
                var filmName: String,
                var brief: String,
                var short_brief: String,
                var dimensional: String,
                var imax: String,
                var releaseDate: String,
                var wekDate: String,
                var starCode: String,
                var grade: String,
                var duration: String,
                var status: Int,
                var showCinemasCount: Int,
                var showSchedulesCount: Int,
                var have_schedule: Int,
                var media: String,
                var imageUrl: String,
                var posterUrl: String)

data class GuessYouLike(var goods_id: String,
                        var product: String,
                        var title: String,
                        var short_title: String,
                        var value: String,
                        var price: String,
                        var bought: Int,
                        var is_new: String,
                        var is_appointment: Int,
                        var seven_refund: String,
                        var time_refund: Int,
                        var goods_type: String,
                        var is_sell_up: String,
                        var new_cat: String,
                        var is_voucher: String,
                        var left_time: Int,
                        var distance: String,
                        var l_display: Int,
                        var l_text: String,
                        var l_price: String,
                        var l_content: String,
                        var lat: String,
                        var lng: String,
                        var is_buyed: String,
                        var is_collected: String,
                        var images: List<Images>) : Serializable {
    data class Images(var width: Int,
                      var image: String) : Serializable
}

data class ProductDetail(var ret: Int,
                         var msg: String,
                         var result: Result,
                         var token: String) : Serializable {
    data class Result(var goods_id: String,
                      var goods_type: String,
                      var product: String,
                      var title: String,
                      var short_title: String,
                      var is_new: Int,
                      var value: String,
                      var price: String,
                      var is_appointment: Int,
                      var left_time: Int,
                      var goods_show_type: Int,
                      var if_join: Int,
                      var is_collected: Int,
                      var details: String,
                      var notice: String,
                      var comment: Comment,
                      var signiture: Signiture,
                      var pay_mobile: Int,
                      var `type`: String,
                      var is_self: String,
                      var address_id: Int,
                      var cinema_id: String,
                      var btn_disabled: Int,
                      var lashou_price: LashouPrice,
                      var images: List<Images>,
                      var detail_imags: List<String>,
                      var group_recommend: List<*>) : Serializable {
        data class Comment(var total_score: Int,
                           var count: Int,
                           var tags: Tags,
                           var items: List<*>) : Serializable {
            class Tags() : Serializable
        }

        class Signiture() : Serializable
        class LashouPrice() : Serializable
        data class Images(var width: Int,
                          var image: String) : Serializable
    }
}
