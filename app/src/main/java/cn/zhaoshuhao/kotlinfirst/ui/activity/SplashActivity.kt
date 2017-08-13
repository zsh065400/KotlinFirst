package cn.zhaoshuhao.kotlinfirst.ui.activity

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import cn.zhaoshuhao.kotlinfirst.R

class SplashActivity : AppCompatActivity() {

    val mHandler: Handler = Handler()
    var mIsFirst = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_splash)

        mHandler.postDelayed({
            val sp = getSharedPreferences("config", Context.MODE_PRIVATE)
            mIsFirst = sp.getBoolean("mIsFirst", true)
            if (mIsFirst) {
                sp.edit().putBoolean("mIsFirst", false).commit()
//                startActivity(GuideActivity::class.java)
                startActivity<GuideActivity>()
            } else {
                startActivity(MainActivity::class.java)
            }
        }, 3000)
    }

}

