package cn.zhaoshuhao.kotlinfirst.contract

import android.content.Context
import android.text.TextUtils
import cn.zhaoshuhao.kotlinfirst.model.network.common.GetProductDetail
import cn.zhaoshuhao.kotlinfirst.model.network.common.LoadListener
import cn.zhaoshuhao.kotlinfirst.model.network.entity.ProductDetail
import cn.zhaoshuhao.kotlinfirst.utils.KCache
import cn.zhaoshuhao.kotlinfirst.utils.anyToJson
import cn.zhaoshuhao.kotlinfirst.utils.jsonToAny
import cn.zhaoshuhao.kotlinfirst.utils.obtainNetStatus

/**
 * Created by Scout
 * Created on 2017/8/25 17:22.
 */
class DetailPresent(private val context: Context) : Detail.Present {
    private lateinit var view: Detail.View

    private var goodsId: Int = 0
    private val netWorkAvailable: Boolean by lazy {
        context.obtainNetStatus()
    }

    override fun setView(view: Detail.View) {
        this.view = view
    }

    override fun addParams(goodsId: Int) {
        this.goodsId = goodsId
    }

    override fun onStart() {
        if (!netWorkAvailable) loadLocalData()
        else loadDetailInfo()
    }

    override fun loadDetailInfo() {
        GetProductDetail.getInstance(goodsId).execute(object : LoadListener<ProductDetail> {
            override fun onSuccess(data: ProductDetail?) {
                if (data != null) {
                    view.showDeatilInfo(data)
                    var json = anyToJson(data)
                    KCache<String>(context, goodsId.toString()).putValue(json)
                } else view.loadError()
            }

            override fun onLoadLocalData() {
                loadLocalData()
            }
        })
    }

    override fun loadLocalData() {
        val detailJson: String = KCache<String>(context, goodsId.toString()).getValue("")
        if (!TextUtils.isEmpty(detailJson)) {
            val productDetail = jsonToAny<ProductDetail>(detailJson)
            view.showDeatilInfo(productDetail)
        }
    }
}

interface Detail {
    interface Present : Base.Present<Detail.View> {
        fun addParams(goodsId: Int)
        fun loadDetailInfo()
        fun loadLocalData()
    }

    interface View : Base.View<Detail.Present> {
        fun showDeatilInfo(data: ProductDetail)

        fun loadError()
    }
}
