package cn.zhaoshuhao.kotlinfirst.ui.activity

import android.graphics.Paint
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import cn.zhaoshuhao.kotlinfirst.R
import cn.zhaoshuhao.kotlinfirst.base.BaseActivity
import cn.zhaoshuhao.kotlinfirst.base.startActivity
import cn.zhaoshuhao.kotlinfirst.base.toast
import cn.zhaoshuhao.kotlinfirst.contract.Detail
import cn.zhaoshuhao.kotlinfirst.contract.DetailPresent
import cn.zhaoshuhao.kotlinfirst.model.network.BASE_API
import cn.zhaoshuhao.kotlinfirst.model.network.entity.GuessYouLike
import cn.zhaoshuhao.kotlinfirst.model.network.entity.ProductDetail
import cn.zhaoshuhao.kotlinfirst.utils.load
import cn.zhaoshuhao.kotlinfirst.utils.transparentStatusBar
import kotlinx.android.synthetic.main.activity_product_detail.*


class ProductDetailActivity : BaseActivity(), Detail.View {
    private lateinit var present: Detail.Present

    private lateinit var product: GuessYouLike

    override fun obtainLayoutID(): Int = R.layout.activity_product_detail

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        present.addParams(product.goods_id.toInt())
        present.onStart()
    }

    override fun beforeSetContentView() {
        transparentStatusBar()
        bindMvp()
        product = intent.extras.getSerializable("product") as GuessYouLike
    }

    private fun bindMvp() {
        setPresent(DetailPresent(this))
        present.setView(this)
    }

    override fun initToolbar() {
        with(id_detail_toolbar as Toolbar) {
            title = product.product

            setSupportActionBar(this)
            supportActionBar?.setHomeButtonEnabled(true)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)

            setOnMenuItemClickListener { false }
        }
    }

    override fun setPresent(present: Detail.Present) {
        this.present = present
    }

    override fun loadError() {
        toast("加载失败，请重试"); onBackPressed()
    }

    override fun showDeatilInfo(data: ProductDetail) {
        val result = data.result
        id_detail_iv_image.load(this@ProductDetailActivity, result.images[0].image)
        id_detail_iv_image.setOnClickListener {
            val bundle = Bundle()
            bundle.putSerializable("images", result)
            startActivity<ImageGalleryActivity>(bundle)
        }
        id_detail_tv_title.text = product.title ?: result.title
        id_detail_tv_bought.text = "已售${product.bought.toString()}份"
        id_detail_web_detail.loadDataWithBaseURL(BASE_API, result.details, "text/html", "UTF-8", null)
        id_detail_web_notices.loadDataWithBaseURL(BASE_API, result.notice, "text/html", "UTF-8", null)
        id_detail_tv_price.text = "￥${result.price}"
        id_detail_tv_value.text = "门市价：￥${result.value}"
        id_detail_tv_value.paint.flags = Paint.STRIKE_THRU_TEXT_FLAG
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == android.R.id.home) onBackPressed()
        return super.onOptionsItemSelected(item)
    }

    override fun obtainMenuRes(): Int = R.menu.product_detail_activity_toolbar

}
