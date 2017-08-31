package cn.zhaoshuhao.kotlinfirst.ui.activity

import android.os.Bundle
import android.support.v4.view.ViewPager
import cn.zhaoshuhao.kotlinfirst.R
import cn.zhaoshuhao.kotlinfirst.adapter.GalleryAdapter
import cn.zhaoshuhao.kotlinfirst.base.BaseActivity
import cn.zhaoshuhao.kotlinfirst.base.fullScreen
import cn.zhaoshuhao.kotlinfirst.model.network.entity.ProductDetail
import kotlinx.android.synthetic.main.activity_image_gallery.*

class ImageGalleryActivity : BaseActivity() {
    override fun obtainLayoutID(): Int = R.layout.activity_image_gallery

    private lateinit var product: ProductDetail.Result
    private  var size: Int = 0

    override fun beforeSetContentView() {
        fullScreen()
        product = intent.extras.getSerializable("images") as ProductDetail.Result
        size = product.detail_imags.size
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun initViews() {
        val galleryAdapter = GalleryAdapter(this, product.detail_imags)
        id_gallery_vp_images.adapter = galleryAdapter
        id_gallery_tv_product.text = product.product
        id_gallery_tv_size.text = "1/$size"
        id_gallery_vp_images.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {}

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

            override fun onPageSelected(position: Int) {
                id_gallery_tv_size.text = "${position + 1}/$size"
            }
        })
    }
}
