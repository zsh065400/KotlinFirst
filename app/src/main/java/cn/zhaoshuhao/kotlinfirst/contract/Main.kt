package cn.zhaoshuhao.kotlinfirst.contract

import cn.zhaoshuhao.kotlinfirst.model.network.common.GetBannerData
import cn.zhaoshuhao.kotlinfirst.model.network.common.GetFilmData
import cn.zhaoshuhao.kotlinfirst.model.network.common.GetYourLike
import cn.zhaoshuhao.kotlinfirst.model.network.common.LoadListener
import cn.zhaoshuhao.kotlinfirst.model.network.entity.Banner
import cn.zhaoshuhao.kotlinfirst.model.network.entity.Film
import cn.zhaoshuhao.kotlinfirst.model.network.entity.GuessYouLike

/**
 * Created by Scout
 * Created on 2017/8/14 21:00.
 */
class MainPresent : Main.Present {
    lateinit var mainView: Main.View


    override fun start() {
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
                if (data != null && data.size != 0) mainView.initBanner(data)
            }
        })
    }

    override fun loadFilm() {
        GetFilmData.execute(object : LoadListener<ArrayList<Film>> {
            override fun onSuccess(data: ArrayList<Film>?) {
                if (data != null && data.size != 0) mainView.initFilm(data)
            }
        })
    }

    override fun loadYouLike() {
        GetYourLike.execute(object : LoadListener<ArrayList<GuessYouLike>> {
            override fun onSuccess(data: ArrayList<GuessYouLike>?) {
                if (data != null && data.size != 0) mainView.initYouLike(data)
                else System.out.println("内容为空")
            }
        })
    }

}

interface Main {
    interface Present : Base.Present<Main.View> {
        fun start()

        fun loadBanner()

        fun loadFilm()

        fun loadYouLike()
    }

    interface View : Base.View<Main.Present> {
        fun initBanner(banners: ArrayList<Banner>)

        fun initFilm(films: ArrayList<Film>)

        fun initYouLike(youlikes: ArrayList<GuessYouLike>)
    }
}
