package cn.zhaoshuhao.kotlinfirst.base

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.ActivityOptionsCompat
import android.support.v7.app.AppCompatActivity
import android.view.Gravity
import android.view.Menu
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import cn.zhaoshuhao.kotlinfirst.R
import cn.zhaoshuhao.kotlinfirst.utils.KPermission
import cn.zhaoshuhao.kotlinfirst.utils.LogType
import cn.zhaoshuhao.kotlinfirst.utils.componet
import cn.zhaoshuhao.kotlinfirst.utils.log


/**
 * Created by Scout
 * Created on 2017/8/12 22:46.
 */

abstract class BaseActivity : AppCompatActivity() {
    val logd = (::log.componet())(LogType.DEBUG)(this::class.java.toString())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        prepareInitUI()
        setContentView(obtainLayoutID())
        beforeInitViews()
        initViews()
        initToolbar()
    }

    open fun beforeInitViews() {}

    open fun prepareInitUI() {}

    abstract fun obtainLayoutID(): Int

    open fun initViews() {}

    open fun initToolbar() {}

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if (obtainMenuRes() == 0) return false
        menuInflater.inflate(obtainMenuRes(), menu)
        initMenuAction(menu)
        return super.onCreateOptionsMenu(menu)
    }

    open fun obtainMenuRes(): Int {
        return 0
    }

    open fun initMenuAction(menu: Menu?) {}

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
//        KPermission.handleResult(this, requestCode, permissions, grantResults)
        KPermission.handleResultOfLambda(this, requestCode, permissions, grantResults)
    }
}

inline fun Context.toast(resId: Int, longTime: Boolean = false) = Toast.makeText(this, resId, if (longTime) Toast.LENGTH_LONG else Toast.LENGTH_SHORT).show()

inline fun Context.toast(msg: String, longTime: Boolean = false) = Toast.makeText(this, msg, if (longTime) Toast.LENGTH_LONG else Toast.LENGTH_SHORT).show()

inline fun Context.toast(msg: String, duration: Int = Toast.LENGTH_SHORT, gravity: Int = Gravity.CENTER, xOffset: Int = 0, yOffse: Int = 0) {
    var toast = Toast(this)
    toast.duration = duration
    toast.setText(msg)
    toast.setGravity(gravity, xOffset, yOffse)
    toast.show()
}

inline fun Activity.fullScreen() {
    requestWindowFeature(Window.FEATURE_NO_TITLE)
    window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
}

inline fun <T> Activity.startActivity(other: Class<T>, bundle: Bundle) {
    val target = Intent(this, other)
    target.putExtras(bundle)
//    startActivity(target)
    //设置进入和退出动画
//    overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
    val options = ActivityOptionsCompat.makeCustomAnimation(this,
            R.anim.fade_in, R.anim.fade_out)
    ActivityCompat.startActivity(this, target, options.toBundle())
}

inline fun <reified T : Context> Activity.startActivity() {
    startActivity(T::class.java, Bundle())
}

inline fun <reified T : Context> Activity.startActivity(bundle: Bundle) {
    startActivity(T::class.java, bundle)
}
