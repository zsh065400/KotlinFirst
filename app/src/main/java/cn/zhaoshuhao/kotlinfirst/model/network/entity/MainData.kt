package cn.zhaoshuhao.kotlinfirst.model.network.entity

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
                        var images: List<Images>) {
    data class Images(var width: Int,
                      var image: String)
}
