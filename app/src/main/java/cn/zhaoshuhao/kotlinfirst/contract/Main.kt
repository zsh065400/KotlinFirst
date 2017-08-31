package cn.zhaoshuhao.kotlinfirst.contract

import android.content.Context
import android.text.TextUtils
import cn.zhaoshuhao.kotlinfirst.model.network.common.GetBannerData
import cn.zhaoshuhao.kotlinfirst.model.network.common.GetFilmData
import cn.zhaoshuhao.kotlinfirst.model.network.common.GetYourLike
import cn.zhaoshuhao.kotlinfirst.model.network.common.LoadListener
import cn.zhaoshuhao.kotlinfirst.model.network.entity.Banner
import cn.zhaoshuhao.kotlinfirst.model.network.entity.Film
import cn.zhaoshuhao.kotlinfirst.model.network.entity.GuessYouLike
import cn.zhaoshuhao.kotlinfirst.utils.SPExt
import cn.zhaoshuhao.kotlinfirst.utils.jsonToList
import cn.zhaoshuhao.kotlinfirst.utils.listToJson

/**
 * Created by Scout
 * Created on 2017/8/14 21:00.
 */
class MainPresent(val context: Context) : Main.Present {
    lateinit var mainView: Main.View

    var bannerJson: String by SPExt.SpDelegate(context, "banners", "")
    var filmJson: String by SPExt.SpDelegate(context, "films", "")
    var youLikeJson: String by SPExt.SpDelegate(context, "youlikes", "")

    override fun onStart() {
        loadBanner()
        loadFilm()
        loadYouLike()
    }

    override fun setView(view: Main.View) {
        this.mainView = view
    }

    override fun loadBanner() {
        GetBannerData.execute(object : LoadListener<ArrayList<Banner>> {
            override fun onSuccess(data: ArrayList<Banner>?) {
                if (data != null && data.size != 0) {
                    mainView.initBanner(data)
                    bannerJson = listToJson(data)
                } else {
                    mainView.loadError(ErrorType.BANNER.code)
                }
            }

            override fun onLoadLocalData() {
                if (!TextUtils.isEmpty(bannerJson)) {
                    val banners: ArrayList<Banner> = jsonToList<Banner>(bannerJson)
                    mainView.initBanner(banners)
                }
            }
        })
    }

    override fun loadFilm() {
        GetFilmData.execute(object : LoadListener<ArrayList<Film>> {
            override fun onSuccess(data: ArrayList<Film>?) {
                if (data != null && data.size != 0) {
                    mainView.initFilm(data)
                    bannerJson = listToJson(data)
                } else {
                    mainView.loadError(ErrorType.FILM.code)
                }
            }

            override fun onLoadLocalData() {
                if (!TextUtils.isEmpty(filmJson)) {
                    val films = jsonToList<Film>(filmJson)
                    mainView.initFilm(films)
                }
            }
        })
    }

    override fun loadYouLike() {
        GetYourLike.execute(object : LoadListener<ArrayList<GuessYouLike>> {
            override fun onSuccess(data: ArrayList<GuessYouLike>?) {
                if (data != null && data.size != 0) {
                    mainView.initYouLike(data)
                    youLikeJson = listToJson(data)
                } else {
                    mainView.loadError(ErrorType.YOU_LIKE.code)
                }
            }

            override fun onLoadLocalData() {
                if (!TextUtils.isEmpty(youLikeJson)) {
                    val youlikes = jsonToList<GuessYouLike>(youLikeJson)
                    mainView.initYouLike(youlikes)
                }
            }
        })
    }

}

interface Main {
    interface Present : Base.Present<Main.View> {
        fun loadBanner()

        fun loadFilm()

        fun loadYouLike()
    }

    interface View : Base.View<Main.Present> {
        fun initBanner(banners: ArrayList<Banner>)

        fun initFilm(films: ArrayList<Film>)

        fun initYouLike(youlikes: ArrayList<GuessYouLike>)

        fun loadError(code: Int)
    }
}

enum class ErrorType(val code: Int) {
    BANNER(1), FILM(2), YOU_LIKE(3)
}
