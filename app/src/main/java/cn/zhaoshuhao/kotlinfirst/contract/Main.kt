package cn.zhaoshuhao.kotlinfirst.contract

import android.content.Context
import android.text.TextUtils
import android.util.Log
import cn.zhaoshuhao.kotlinfirst.fragment.MainFragment
import cn.zhaoshuhao.kotlinfirst.model.network.common.GetBannerData
import cn.zhaoshuhao.kotlinfirst.model.network.common.GetFilmData
import cn.zhaoshuhao.kotlinfirst.model.network.common.GetYourLike
import cn.zhaoshuhao.kotlinfirst.model.network.common.LoadListener
import cn.zhaoshuhao.kotlinfirst.model.network.entity.Banner
import cn.zhaoshuhao.kotlinfirst.model.network.entity.Film
import cn.zhaoshuhao.kotlinfirst.model.network.entity.GuessYouLike
import cn.zhaoshuhao.kotlinfirst.utils.*

/**
 * Created by Scout
 * Created on 2017/8/14 21:00.
 */
class MainPresent(private val context: Context) : Main.Present {
    lateinit var mainView: Main.View

    private var bannerJson: String by SPExt.SpDelegate(context, "banners", "")

    private var filmJson: String by SPExt.SpDelegate(context, "films", "")
    private var youLikeJson: String by SPExt.SpDelegate(context, "youlikes", "")

    private val netWorkAvailable: Boolean by lazy {
        context.obtainNetStatus()
    }

    private var refreshCount: Int = 0

    override fun onStart() {
        refreshCount = 0
        if (!netWorkAvailable) {
            for (i in 0..3) loadLocalData(i)
        } else {
            loadBanner()
            loadFilm()
            loadYouLike()
            loadGridType()
        }
        async {
            while (true){
                if (refreshCount == 4) {
                    (mainView as MainFragment).activity.runOnUiThread {
                        mainView.onRefreshComplete()
                    }
                    break
                }
            }
        }
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
                    Log.d("Banner Data", bannerJson)
                } else {
                    mainView.loadError(ErrorType.BANNER.code)
                }
                refreshCount++
            }

            override fun onLoadLocalData() {
                loadLocalData(0)
            }
        })
    }

    override fun loadFilm() {
        GetFilmData.execute(object : LoadListener<ArrayList<Film>> {
            override fun onSuccess(data: ArrayList<Film>?) {
                if (data != null && data.size != 0) {
                    mainView.initFilm(data)
                    filmJson = listToJson(data)
                } else {
                    mainView.loadError(ErrorType.FILM.code)
                }
                refreshCount++
            }

            override fun onLoadLocalData() {
                loadLocalData(1)
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
                refreshCount++
            }

            override fun onLoadLocalData() {
                loadLocalData(2)
            }
        })
    }

    override fun loadGridType() {
        mainView.initItemGrid()
        refreshCount++
    }

    /**
     * 加载本地数据，若存在
     *0 -> 本地Banner数据， 1 -> 本地Film数据， 2 -> 本地YouLike数据
     * */
    override fun loadLocalData(code: Int) {
        when (code) {
            0 -> {
                if (!TextUtils.isEmpty(bannerJson)) {
                    val banners: ArrayList<Banner> = jsonToList<Banner>(bannerJson)
                    mainView.initBanner(banners)
                    refreshCount++
                }
            }
            1 -> {
                if (!TextUtils.isEmpty(filmJson)) {
                    val films = jsonToList<Film>(filmJson)
                    mainView.initFilm(films)
                    refreshCount++
                }
            }
            2 -> {
                if (!TextUtils.isEmpty(youLikeJson)) {
                    val youlikes = jsonToList<GuessYouLike>(youLikeJson)
                    mainView.initYouLike(youlikes)
                    refreshCount++
                }
            }
            3 -> {
                mainView.initItemGrid()
                refreshCount++
            }
        }
    }

}

interface Main {
    interface Present : Base.Present<Main.View> {
        fun loadBanner()

        fun loadFilm()

        fun loadYouLike()

        fun loadLocalData(code: Int)

        fun loadGridType()
    }

    interface View : Base.View<Main.Present> {
        fun initBanner(banners: ArrayList<Banner>)

        fun initFilm(films: ArrayList<Film>)

        fun initYouLike(youlikes: ArrayList<GuessYouLike>)

        fun loadError(code: Int)

        fun initItemGrid()

        fun onRefreshComplete()
    }
}

enum class ErrorType(val code: Int) {
    BANNER(1), FILM(2), YOU_LIKE(3)
}
