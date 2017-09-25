package cn.zhaoshuhao.kotlinfirst.app

import android.app.Application
import c.b.BP

/**
 * Created by Scout
 * Created on 2017/8/12 15:13.
 */
class ScoutApplication: Application(){

    override fun onCreate() {
        super.onCreate()
        BP.init("b80d94a6147a58e9e889e7c7c574a037")
    }
}
