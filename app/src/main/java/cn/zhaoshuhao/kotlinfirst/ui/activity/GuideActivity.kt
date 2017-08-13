package cn.zhaoshuhao.kotlinfirst.ui.activity

import android.os.Bundle
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.animation.AnimationUtils
import cn.zhaoshuhao.kotlinfirst.R
import cn.zhaoshuhao.kotlinfirst.adapter.GuideAdapter
import kotlinx.android.synthetic.main.activity_guide.*

class GuideActivity : AppCompatActivity() {

    val mImageRes by lazy {
        intArrayOf(R.mipmap.guide_1, R.mipmap.guide_2, R.mipmap.guide_3, R.mipmap.guide_4)
    }

    val mAdapter: GuideAdapter by lazy {
        GuideAdapter(this, mImageRes)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /*设置全屏*/
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        setContentView(R.layout.activity_guide)

        id_vp_guide.adapter = mAdapter
        id_vp_guide.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
//                when(position){
//                    3 ->{id_btn_start.alpha = positionOffset}
//                    4 ->{id_btn_start.alpha = positionOffset}
//                }
            }

            override fun onPageSelected(position: Int) {
                if (position == mImageRes.size - 1) {
                    id_btn_start.visibility = View.VISIBLE
                    val loadAnimation = AnimationUtils.loadAnimation(this@GuideActivity, R.anim.abc_fade_in)
                    id_btn_start.startAnimation(loadAnimation)
                } else {
                    id_btn_start.visibility = View.GONE
                    //退出时不能使用动画
                }
            }

            override fun onPageScrollStateChanged(state: Int) {

            }

        })
        id_btn_start.setOnClickListener { startActivity(MainActivity::class.java) }
    }
}
