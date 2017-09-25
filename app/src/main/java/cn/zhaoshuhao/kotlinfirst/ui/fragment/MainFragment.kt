package cn.zhaoshuhao.kotlinfirst.fragment

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.support.v4.view.ViewPager
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.widget.GridView
import android.widget.Toast
import cn.zhaoshuhao.kotlinfirst.R
import cn.zhaoshuhao.kotlinfirst.adapter.*
import cn.zhaoshuhao.kotlinfirst.base.BaseFragment
import cn.zhaoshuhao.kotlinfirst.base.toActivity
import cn.zhaoshuhao.kotlinfirst.contract.Main
import cn.zhaoshuhao.kotlinfirst.model.bean.TypeInfo
import cn.zhaoshuhao.kotlinfirst.model.network.entity.Banner
import cn.zhaoshuhao.kotlinfirst.model.network.entity.Film
import cn.zhaoshuhao.kotlinfirst.model.network.entity.GuessYouLike
import cn.zhaoshuhao.kotlinfirst.ui.activity.IRefreshListener
import cn.zhaoshuhao.kotlinfirst.ui.activity.ProductDetailActivity
import cn.zhaoshuhao.kotlinfirst.ui.view.Indicator
import kotlinx.android.synthetic.main.fragment_main.*

/**
 * Created by Scout
 * Created on 2017/7/19 18:42.
 */
class MainFragment : BaseFragment(), Main.View {

    private lateinit var mainPresent: Main.Present
    private lateinit var refreshListener: IRefreshListener

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        refreshListener = context as IRefreshListener
    }

    override fun setPresent(present: Main.Present) {
        this.mainPresent = present
    }

    override fun obtainLayoutID(): Int = R.layout.fragment_main

    override fun initView(view: View?, savedBundle: Bundle?) {
        mainPresent.onStart()
        initRefreshLayout()
    }

    private fun initRefreshLayout() {
        id_main_refresh.setColorSchemeResources(R.color.colorPrimary, R.color.colorAccent)
        id_main_refresh.setOnRefreshListener {
            mainPresent.onStart()
            refreshListener.isRefreshing()
        }
    }

    override fun onRefreshComplete() {
        id_main_refresh.isRefreshing = false
        refreshListener.refreshComplete()
    }

    val mCallback = Handler()

    override fun initBanner(banners: ArrayList<Banner>) {
        id_vp_banner.adapter = BannerAdapter(context, banners)
        id_banner_indicator.mNumbers = banners.size
        id_vp_banner.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                id_banner_indicator.setOffset(position, positionOffset)
            }

            override fun onPageSelected(position: Int) {}

            override fun onPageScrollStateChanged(state: Int) {}
        })
    }

    override fun initItemGrid() {
        val list = mutableListOf<View>()
        val page1 = mutableListOf<TypeInfo>()
        val page2 = mutableListOf<TypeInfo>()
        val stringArray = resources.getStringArray(R.array.home_bar_labels)
        val typedArray = resources.obtainTypedArray(R.array.home_bar_icon)

        var i = 0
        while (i < 16) {
            if (i < 8) {
                val itemInfo = TypeInfo(stringArray[i], typedArray.getResourceId(i, 0))
                page1.add(itemInfo)
            } else {
                val itemInfo = TypeInfo(stringArray[i], typedArray.getResourceId(i, 0))
                page2.add(itemInfo)
            }
            i++
        }

        val view1 = LayoutInflater.from(this.context).inflate(R.layout.main_type_grid, null, false)
        val grid1 = view1?.findViewById<GridView>(R.id.id_grid_item)
        grid1?.adapter = GridAdapter(this.context, page1)

        val view2 = LayoutInflater.from(this.context).inflate(R.layout.main_type_grid, null, false)
        val grid2 = view2?.findViewById<GridView>(R.id.id_grid_item)
        grid2?.adapter = GridAdapter(this.context, page2)

        list.add(view1)
        list.add(view2)

        id_vp_item.adapter = ItemAdapter(this.context, list)
        id_vp_item.addOnPageChangeListener(IndicatorChangeListener(id_type_indicator))
    }

    override fun initFilm(films: ArrayList<Film>) {
        id_recv_film.layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)
//        id_recv_film.addItemDecoration(DividerItemDecoration(this.context, LinearLayoutManager.HORIZONTAL))

        id_recv_film.adapter = FilmAdapter(this.context, films, object : BaseSupportAdapter.OnItemClickListener<Film> {
            override fun onClick(view: View, data: Film, position: Int) {
                Toast.makeText(this@MainFragment.context, data.filmName, Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun initYouLike(youlikes: ArrayList<GuessYouLike>) {
//        val paddingBottom = arguments.getInt("height")
//        id_recv_you_like.setPadding(0,0,0, paddingBottom)
        id_recv_you_like.layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)
        id_recv_you_like.isNestedScrollingEnabled = false
        id_recv_you_like.adapter = YourLikeAdapter(this.context, youlikes, object : BaseSupportAdapter.OnItemClickListener<GuessYouLike> {
            override fun onClick(view: View, data: GuessYouLike, position: Int) {
                val bundle = Bundle()
                bundle.putSerializable("product", data)
                activity.toActivity<ProductDetailActivity>(bundle)
            }
        })
    }

    /*加载失败*/
    override fun loadError(code: Int) {
        when (code) {
            1 -> {
            }
            2 -> {
            }
            3 -> {
            }
        }
    }

    override fun onResume() {
        super.onResume()
        mCallback.postDelayed(mAutoScroll, 3000)
    }

    override fun onStop() {
        super.onStop()
        mCallback.removeCallbacks(mAutoScroll)
    }

    override fun onDetach() {
        super.onDetach()
    }

    private val mAutoScroll = AutoScroll()

    inner class AutoScroll : Runnable {
        override fun run() {
            id_vp_banner.currentItem = id_vp_banner.currentItem + 1
            mCallback.postDelayed(this, 3000)
        }
    }
}

class IndicatorChangeListener(private val indicator: Indicator) : ViewPager.OnPageChangeListener {
    override fun onPageScrollStateChanged(state: Int) {}

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        indicator.setOffset(position, positionOffset)
    }

    override fun onPageSelected(position: Int) {}

}


