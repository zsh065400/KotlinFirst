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
 * Created on 2017/7/12 17:56.
 */
class GuideAdapter(val content: Context, val datas: IntArray) : PagerAdapter() {

    val mViews: MutableList<View> = mutableListOf()

    init {
        datas.forEach {
            val inflate = LayoutInflater.from(content).inflate(R.layout.guide_imageview, null, false)
            val imageView = inflate.findViewById<ImageView>(R.id.id_iv_guide)
            imageView.setBackgroundResource(it)
            mViews.add(inflate)
        }
    }

    override fun destroyItem(container: ViewGroup?, position: Int, `object`: Any?) {
        container?.removeView(mViews[position])
    }

    override fun instantiateItem(container: ViewGroup?, position: Int): Any {
        val view = mViews[position]
        container?.addView(view)
        return view
    }

    override fun isViewFromObject(view: View?, `object`: Any?): Boolean {
        return view === `object`
    }

    override fun getCount(): Int {
        return mViews.size
    }
}
