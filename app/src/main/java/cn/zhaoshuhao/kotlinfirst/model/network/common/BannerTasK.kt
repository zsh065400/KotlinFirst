package cn.zhaoshuhao.kotlinfirst.model.network.common

import cn.zhaoshuhao.kotlinfirst.model.network.BANNER
import cn.zhaoshuhao.kotlinfirst.model.network.DefaultLoadListener
import cn.zhaoshuhao.kotlinfirst.model.network.MY_API
import cn.zhaoshuhao.kotlinfirst.model.network.NetTask
import cn.zhaoshuhao.kotlinfirst.model.network.entity.Banner
import retrofit2.Call
import retrofit2.http.GET

/**
 * Created by Scout
 * Created on 2017/8/14 22:53.
 */
object GetBannerData : NetTask<ArrayList<Banner>> {
    override fun execute(loadListener: LoadListener<ArrayList<Banner>>) {
        val retrofit = obtainClient(MY_API)
        val bannerService = retrofit.create(BannerService::class.java)
        val call = bannerService.getBanner()
        call.enqueue(DefaultLoadListener<ArrayList<Banner>>(loadListener))
    }
}

interface BannerService {
    @GET(BANNER)
    fun getBanner(): Call<ArrayList<Banner>>
}
