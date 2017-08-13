package cn.zhaoshuhao.kotlinfirst.fragment

import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridView
import android.widget.Toast
import cn.zhaoshuhao.kotlinfirst.R
import cn.zhaoshuhao.kotlinfirst.adapter.*
import cn.zhaoshuhao.kotlinfirst.model.bean.FilmItem
import cn.zhaoshuhao.kotlinfirst.model.bean.ItemInfo
import kotlinx.android.synthetic.main.fragment_main.*

/**
 * Created by Scout
 * Created on 2017/7/19 18:42.
 */
class MainFragment : Fragment() {

    val mBannerRes = intArrayOf(R.mipmap.banner01, R.mipmap.banner02, R.mipmap.banner03)
    val mCallback = Handler()

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val inflate = inflater?.inflate(R.layout.fragment_main, container, false)
        return inflate
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        id_vp_banner.adapter = BannerAdapter(context, mBannerRes)
        id_vp_banner.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                id_indicator.setOffset(position, positionOffset)
            }

            override fun onPageSelected(position: Int) {}

            override fun onPageScrollStateChanged(state: Int) {}
        })

        initItemGrid()
        initFilmRecv()
    }

    fun initItemGrid() {
        val list = mutableListOf<View>()
        val page1 = mutableListOf<ItemInfo>()
        val page2 = mutableListOf<ItemInfo>()
        val stringArray = resources.getStringArray(R.array.home_bar_labels)
        val typedArray = resources.obtainTypedArray(R.array.home_bar_icon)

        var i = 0
        while (i < 16) {
            if (i < 8) {
                val itemInfo = ItemInfo(stringArray[i], typedArray.getResourceId(i, 0))
                page1.add(itemInfo)
            } else {
                val itemInfo = ItemInfo(stringArray[i], typedArray.getResourceId(i, 0))
                page2.add(itemInfo)
            }
            i++
        }

        val view1 = LayoutInflater.from(this.context).inflate(R.layout.grid_view, null, false)
        val grid1 = view1?.findViewById<GridView>(R.id.id_grid_item)
        grid1?.adapter = GridAdapter(this.context, page1)

        val view2 = LayoutInflater.from(this.context).inflate(R.layout.grid_view, null, false)
        val grid2 = view2?.findViewById<GridView>(R.id.id_grid_item)
        grid2?.adapter = GridAdapter(this.context, page2)

        list.add(view1)
        list.add(view2)

        id_vp_item.adapter = ItemAdapter(this.context, list)
    }

    private fun initFilmRecv() {
        id_recv_film.layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)
//        id_recv_film.addItemDecoration(DividerItemDecoration(this.context, LinearLayoutManager.HORIZONTAL))
        val datas = ArrayList<FilmItem>()
        val film1 = FilmItem("", "战狼2", "25.0")
        val film2 = FilmItem("", "战狼", "20.0")
        val film3 = FilmItem("", "建军大业", "35.0")
        val film4 = FilmItem("", "三生三世十里桃花", "25.0")
        val film5 = FilmItem("", "鬼吹灯", "35.0")
        datas.add(film1)
        datas.add(film2)
        datas.add(film3)
        datas.add(film4)
        datas.add(film5)
        id_recv_film.adapter = FilmAdapter(this.context, datas, object : BaseSupportAdapter.OnItemClickListener<FilmItem> {
            override fun onClick(data: FilmItem, position: Int) {
                Toast.makeText(this@MainFragment.context, data.name, Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onResume() {
        super.onResume()
        mCallback.postDelayed(mAutoScroll, 3000)
        checkout(activity as CheckoutToolbar)
    }

    override fun onStop() {
        super.onStop()
        mCallback.removeCallbacks(mAutoScroll)
    }

    val mAutoScroll = AutoScroll()

    inner class AutoScroll : Runnable {
        override fun run() {
            id_vp_banner.currentItem = id_vp_banner.currentItem + 1
            mCallback.postDelayed(this, 3000)
        }
    }
}

interface CheckoutToolbar {
    fun toTarget(fragment: Fragment)
}

fun Fragment.checkout(callback: CheckoutToolbar) {
    callback.toTarget(this)
}


