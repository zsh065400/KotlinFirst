package cn.zhaoshuhao.kotlinfirst.ui.activity

import android.Manifest
import android.os.Bundle
import android.os.Handler
import cn.bmob.v3.Bmob
import cn.zhaoshuhao.kotlinfirst.R
import cn.zhaoshuhao.kotlinfirst.app.BMOB_ID
import cn.zhaoshuhao.kotlinfirst.base.BaseActivity
import cn.zhaoshuhao.kotlinfirst.base.toActivity
import cn.zhaoshuhao.kotlinfirst.base.toast
import cn.zhaoshuhao.kotlinfirst.utils.KPermission
import cn.zhaoshuhao.kotlinfirst.utils.SPExt

class SplashActivity : BaseActivity() {

    private val mHandler: Handler = Handler()
    private var mIsFirst by SPExt.SpDelegate(this, "mIsFirst", true)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Bmob.initialize(this, BMOB_ID)
        KPermission.requestOfLambda(Manifest.permission.READ_PHONE_STATE, activity = this) { g, d, n ->
            mHandler.postDelayed({
                if (mIsFirst) {
                    toActivity<GuideActivity>()
                    mIsFirst = false
                } else toActivity<MainActivity>()
                finish()
            }, 3000)
            if (d.size != 0 || n.size != 0) {
                toast("软件运行需要该权限")
            }
        }
    }

    override fun obtainLayoutID(): Int = R.layout.activity_splash
}

