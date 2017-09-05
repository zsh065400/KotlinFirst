package cn.zhaoshuhao.kotlinfirst.model.network

import cn.zhaoshuhao.kotlinfirst.model.network.common.LoadListener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by Scout
 * Created on 2017/8/13 16:34.
 */
const val BASE_API = "http://7xij5m.com1.z0.glb.clouddn.com/"

const val INFER_YOUR_LIKE = "spRecommend_new.txt"

const val YOUR_LIKE_DETAIL = "{goods_id}.txt"

const val HOT_FILM = "filmHot_refresh.txt"

const val MY_API = "http://192.168.1.111:11761/"

const val BANNER = "LaShouO2O/banner.do"

const val FILM = "LaShouO2O/film.do"

const val YOU_LIKE = "LaShouO2O/youLike.do"


interface NetTask<out T> {
    fun execute(loadListener: LoadListener<T>)

    fun obtainClient(baseUrl: String): Retrofit = Retrofit.Builder().baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create()).build()
}

class DefaultLoadListener<T>(private val loadListener: LoadListener<T>) : Callback<T> {
    override fun onFailure(call: Call<T>?, t: Throwable?) {
        loadListener.onFailed(t)
        loadListener.onLoadLocalData()
    }

    override fun onResponse(call: Call<T>?, response: Response<T>?) {
        loadListener.onSuccess(response?.body())
    }
}
