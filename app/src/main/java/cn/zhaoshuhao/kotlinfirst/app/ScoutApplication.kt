package cn.zhaoshuhao.kotlinfirst.app

import android.app.Application
import c.b.BP

/**
 * Created by Scout
 * Created on 2017/8/12 15:13.
 */
class ScoutApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        //支付服务
        BP.init(BMOB_ID)
        //短信服务
//        BmobSMS.initialize(this, BMOB_ID);
    }
}
