package cn.zhaoshuhao.kotlinfirst.model.network.common

import cn.zhaoshuhao.kotlinfirst.model.network.BASE_API
import cn.zhaoshuhao.kotlinfirst.model.network.Forward
import cn.zhaoshuhao.kotlinfirst.model.network.NetTask
import cn.zhaoshuhao.kotlinfirst.model.network.entity.ProductDetail
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Created by Scout
 * Created on 2017/8/24 17:45.
 */

class GetProductDetail(private val goodsId: Int) : NetTask<ProductDetail> {

    companion object {
        fun getInstance(goodsId: Int): GetProductDetail = GetProductDetail(goodsId)
    }

    override fun execute(loadListener: LoadListener<ProductDetail>) {
        val service = obtainClient(BASE_API).create(ProductDetailService::class.java)
        val productDetail = service.getProductDetail(goodsId)
        productDetail.enqueue(Forward<ProductDetail>(loadListener))
    }
}

interface ProductDetailService {

    @GET("{id}.txt")
    fun getProductDetail(@Path("id") goodsId: Int): Call<ProductDetail>
}
