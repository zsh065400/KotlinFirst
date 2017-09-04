package cn.zhaoshuhao.kotlinfirst.ui.activity

import android.os.Bundle
import android.support.v4.view.ViewPager
import android.view.View
import android.view.animation.AnimationUtils
import cn.zhaoshuhao.kotlinfirst.R
import cn.zhaoshuhao.kotlinfirst.adapter.GuideAdapter
import cn.zhaoshuhao.kotlinfirst.base.BaseActivity
import cn.zhaoshuhao.kotlinfirst.base.fullScreen
import cn.zhaoshuhao.kotlinfirst.base.startActivity
import kotlinx.android.synthetic.main.activity_guide.*

class GuideActivity : BaseActivity() {

    private val mImageRes by lazy {
        intArrayOf(R.mipmap.guide_1, R.mipmap.guide_2, R.mipmap.guide_3, R.mipmap.guide_4)
    }

    private val mAdapter: GuideAdapter by lazy {
        GuideAdapter(this, mImageRes)
    }


    override fun prepareInitUI() {
        /*设置全屏*/
        fullScreen()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        id_vp_guide.adapter = mAdapter
        id_vp_guide.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

            override fun onPageSelected(position: Int) {
                if (position == mImageRes.size - 1) {
                    id_btn_start.visibility = View.VISIBLE
                    val loadAnimation = AnimationUtils.loadAnimation(this@GuideActivity, R.anim.abc_fade_in)
                    id_btn_start.startAnimation(loadAnimation)
                } else {
                    id_btn_start.visibility = View.GONE
                }
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })

        id_btn_start.setOnClickListener { startActivity<MainActivity>();finish() }
    }

    override fun obtainLayoutID(): Int = R.layout.activity_guide
}
