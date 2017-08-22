package cn.zhaoshuhao.kotlinfirst.model.network.common

import cn.zhaoshuhao.kotlinfirst.model.network.Forward
import cn.zhaoshuhao.kotlinfirst.model.network.MY_API
import cn.zhaoshuhao.kotlinfirst.model.network.NetTask
import cn.zhaoshuhao.kotlinfirst.model.network.YOU_LIKE
import cn.zhaoshuhao.kotlinfirst.model.network.entity.GuessYouLike
import retrofit2.Call
import retrofit2.http.GET

/**
 * Created by Scout
 * Created on 2017/8/21 14:58.
 */

object GetYourLike : NetTask<ArrayList<GuessYouLike>> {
    override fun execute(loadListener: LoadListener<ArrayList<GuessYouLike>>) {
        val youLikeService = obtainClient(MY_API).create(YouLikeService::class.java)
        val yourLikes = youLikeService.getYourLike()
        yourLikes.enqueue(Forward<ArrayList<GuessYouLike>>(loadListener))
    }

}

interface YouLikeService {
    @GET(YOU_LIKE)
    fun getYourLike(): Call<ArrayList<GuessYouLike>>
}
