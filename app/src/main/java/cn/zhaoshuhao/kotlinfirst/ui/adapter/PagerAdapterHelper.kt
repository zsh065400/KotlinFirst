package cn.zhaoshuhao.kotlinfirst.adapter

import android.content.Context
import android.os.Bundle
import android.support.v4.view.PagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import cn.zhaoshuhao.kotlinfirst.R
import cn.zhaoshuhao.kotlinfirst.base.startActivity
import cn.zhaoshuhao.kotlinfirst.model.bean.WebViewInfo
import cn.zhaoshuhao.kotlinfirst.model.network.entity.Banner
import cn.zhaoshuhao.kotlinfirst.ui.activity.MainActivity
import cn.zhaoshuhao.kotlinfirst.ui.activity.WebViewActivity
import cn.zhaoshuhao.kotlinfirst.utils.load
import cn.zhaoshuhao.kotlinfirst.utils.obtainDefault
import com.bumptech.glide.Glide


/**
 * Created by Scout
 * Created on 2017/7/19 18:31.
 */

class GalleryAdapter(context: Context, private val datas: List<String>) : BasePagerAdapter(context) {

    init {
        datas.forEach {
            val inflate = LayoutInflater.from(context).inflate(R.layout.gallery_imageview, null, false)
            val imageView = inflate.findViewById<ImageView>(R.id.id_iv_gallery)
            imageView.load(context, it)
            mViews.add(inflate)
        }
    }

    override fun getCount(): Int {
        return datas.size
    }
}

class ItemAdapter(context: Context, val grids: List<View>) : BasePagerAdapter(context) {
    init {
        grids.forEach {
            mViews.add(it)
        }
    }

    override fun getCount(): Int {
        return grids.size
    }

}

class GuideAdapter(context: Context, val datas: IntArray) : BasePagerAdapter(context) {

    init {
        datas.forEach {
            val inflate = LayoutInflater.from(context).inflate(R.layout.guide_imageview, null, false)
            val imageView = inflate.findViewById<ImageView>(R.id.id_iv_guide)
            imageView.setBackgroundResource(it)
            mViews.add(inflate)
        }
    }
//
//    override fun destroyItem(container: ViewGroup?, position: Int, `object`: Any?) {
//        container?.removeView(mViews[position])
//    }
}

class BannerAdapter(context: Context, datas: ArrayList<Banner>) : BasePagerAdapter(context) {

    init {
        datas.forEach { banner: Banner ->
            val inflate = LayoutInflater.from(context).inflate(R.layout.guide_imageview, null, false)
            val imageView = inflate.findViewById<ImageView>(R.id.id_iv_guide)
//            imageView?.setImageResource(it)
            with(Glide.with(context)) { load(banner.image_url).apply(obtainDefault()).into(imageView) }
            inflate.setOnClickListener {
                val info = WebViewInfo(banner.title, banner.to_url)
                val bundle = Bundle()
                bundle.putSerializable("webinfo", info)
                (context as MainActivity).startActivity<WebViewActivity>(bundle)
            }
            mViews.add(inflate)
        }
    }
}

open class BasePagerAdapter(val context: Context) : PagerAdapter() {
    val mViews: MutableList<View> = mutableListOf()

    override fun isViewFromObject(view: View?, `object`: Any?): Boolean {
        return view == `object`
    }

    override fun getCount(): Int {
        return Integer.MAX_VALUE
    }

    override fun destroyItem(container: ViewGroup?, position: Int, `object`: Any?) {
//        container?.removeView(mViews[position])
    }

    override fun instantiateItem(container: ViewGroup?, position: Int): Any {
        var index = position % mViews.size
        if (index < 0) {
            index += mViews.size
        }
        val view = mViews[index]
        val viewParent = view.parent
        if (viewParent != null) {
            (viewParent as ViewGroup).removeView(view)
        }
        container?.addView(view)
        return view
    }
}
