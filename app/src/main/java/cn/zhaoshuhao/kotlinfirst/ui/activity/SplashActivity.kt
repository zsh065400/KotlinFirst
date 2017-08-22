package cn.zhaoshuhao.kotlinfirst.ui.activity

import android.content.Context
import android.os.Bundle
import android.os.Handler
import cn.zhaoshuhao.kotlinfirst.R
import cn.zhaoshuhao.kotlinfirst.base.BaseActivity
import cn.zhaoshuhao.kotlinfirst.base.startActivity

class SplashActivity : BaseActivity() {

    private val mHandler: Handler = Handler()
    private var mIsFirst = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mHandler.postDelayed({
            val sp = getSharedPreferences("config", Context.MODE_PRIVATE)
            mIsFirst = sp.getBoolean("mIsFirst", true)
            if (mIsFirst) {
                sp.edit().putBoolean("mIsFirst", false).commit()
                startActivity<GuideActivity>()
            } else startActivity<MainActivity>()
            finish()
        }, 3000)
    }

    override fun obtainLayoutID(): Int = R.layout.activity_splash
}

