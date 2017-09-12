package cn.zhaoshuhao.kotlinfirst.model.network.common

import cn.zhaoshuhao.kotlinfirst.model.network.AROUND
import cn.zhaoshuhao.kotlinfirst.model.network.DefaultLoadListener
import cn.zhaoshuhao.kotlinfirst.model.network.MY_API
import cn.zhaoshuhao.kotlinfirst.model.network.NetTask
import cn.zhaoshuhao.kotlinfirst.model.network.entity.Around
import retrofit2.Call
import retrofit2.http.GET

/**
 * Created by Scout
 * Created on 2017/9/10 16:06.
 */
object GetAroundInfo : NetTask<ArrayList<Around>> {
    override fun execute(loadListener: LoadListener<ArrayList<Around>>) {
        val aroundService = obtainClient(MY_API).create(AroundService::class.java)
        val aroundInfo = aroundService.getAroundInfo()
        aroundInfo.enqueue(DefaultLoadListener<ArrayList<Around>>(loadListener))
    }
}

interface AroundService {
    @GET(AROUND)
    fun getAroundInfo(): Call<ArrayList<Around>>
}
