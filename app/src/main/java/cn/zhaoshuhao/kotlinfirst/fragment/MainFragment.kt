package cn.zhaoshuhao.kotlinfirst.fragment

import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cn.zhaoshuhao.kotlinfirst.Adapter.BannerAdapter
import cn.zhaoshuhao.kotlinfirst.R
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
    }

    override fun onResume() {
        super.onResume()
        mCallback.postDelayed(mAutoScroll, 3000)
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


