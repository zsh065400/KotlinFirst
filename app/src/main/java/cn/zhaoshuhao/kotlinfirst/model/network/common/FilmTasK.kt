package cn.zhaoshuhao.kotlinfirst.model.network.common

import cn.zhaoshuhao.kotlinfirst.model.network.FILM
import cn.zhaoshuhao.kotlinfirst.model.network.Forward
import cn.zhaoshuhao.kotlinfirst.model.network.MY_API
import cn.zhaoshuhao.kotlinfirst.model.network.NetTask
import cn.zhaoshuhao.kotlinfirst.model.network.entity.Film
import retrofit2.Call
import retrofit2.http.GET

/**
 * Created by Scout
 * Created on 2017/8/14 22:53.
 */
object GetFilmData : NetTask<ArrayList<Film>> {
    override fun execute(loadListener: LoadListener<ArrayList<Film>>) {
        val retrofit = obtainClient(MY_API)
        val bannerService = retrofit.create(FilmService::class.java)
        val call = bannerService.getFilm()
        call.enqueue(Forward<ArrayList<Film>>(loadListener))
    }
}

interface FilmService {
    @GET(FILM)
    fun getFilm(): Call<ArrayList<Film>>
}
