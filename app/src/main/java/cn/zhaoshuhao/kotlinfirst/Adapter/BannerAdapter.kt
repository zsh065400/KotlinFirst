package cn.zhaoshuhao.kotlinfirst.Adapter

import android.content.Context
import android.support.v4.view.PagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import cn.zhaoshuhao.kotlinfirst.R

/**
 * Created by Scout
 * Created on 2017/7/19 18:31.
 */
class BannerAdapter(val context: Context, val datas: IntArray) : PagerAdapter() {
    val mViews: MutableList<View> = mutableListOf()

    init {
        datas.forEach {
            val inflate = LayoutInflater.from(context).inflate(R.layout.guide_imageview, null, false)
            val imageView = inflate.findViewById<ImageView>(R.id.id_iv_guide)
            imageView?.setImageResource(it)
            mViews.add(inflate)
        }
    }

    override fun destroyItem(container: ViewGroup?, position: Int, `object`: Any?) {
//        val index = position % mViews.size
//        container?.removeView(mViews[index])
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

    override fun isViewFromObject(view: View?, `object`: Any?): Boolean {
        return view == `object`
    }

    override fun getCount(): Int {
        return Integer.MAX_VALUE
    }
}
