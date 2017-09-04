package cn.zhaoshuhao.kotlinfirst.utils

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.net.ConnectivityManager
import android.util.Log
import android.view.WindowManager

/**
 * Created by Scout
 * Created on 2017/8/12 16:19.
 */

enum class LogType {
    DEBUG, ERROR, WARNING, INFO;
}

fun log(type: LogType, tag: String, msg: Any) {
    val message = msg.toString()
    when (type) {
        LogType.DEBUG -> Log.d(tag, message)
        LogType.ERROR -> Log.e(tag, message)
        LogType.WARNING -> Log.w(tag, message)
        LogType.INFO -> Log.i(tag, message)
    }
}

fun Activity.transparentStatusBar() {
    /*功能分别为：取消标题栏、
    设置透明状态栏（实际为半透明），
    清除透明状态栏和导航栏，
    设置系统UI模式，
    使状态栏可重新绘制，
    设置状态栏透明，
    设置导航栏颜色，
    设置导航栏透明
    实际为了兼容，状态栏透明设置其颜色即可，系统flag包含阴影，对于部分版本需要兼容，故需要如此多的设置
    */
//        window.requestFeature(Window.FEATURE_NO_TITLE);
//        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS or WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
//        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
//        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
//        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
    window.statusBarColor = Color.TRANSPARENT
//        window.navigationBarColor = resources.getColor(R.color.colorPrimary)
}

/*
* 此处逗逼
* */
enum class PermissionType(val code: Int) {
    READ_CALENDAR(0x111), WRITE_CALENDAR(0x112),
    CAMERA(0x113),
    READ_CONTACTS(0x114), WRITE_CONTACTS(0X115), GET_ACCOUNTS(0X116),
    ACCESS_FINE_LOCATION(0X117), ACCESS_COARSE_LOCATION(0X118),
    RECORD_AUDIO(0X119),
    READ_PHONE_STATE(0X120), CALL_PHONE(0X121), READ_CALL_LOG(0X122), WRITE_CALL_LOG(0X123), ADD_VOICEMAIL(0X124), USE_SIP(0X125), PROCESS_OUTGOING_CALLS(0X126),
    BODY_SENSORS(0X127),
    SEND_SMS(0X128), RECEIVE_SMS(0X129), READ_SMS(0X130), RECEIVE_WAP_PUSH(0X131), RECEIVE_MMS(0X132), READ_CELL_BROADCASTS(0X133),
    READ_EXTERNAL_STORAGE(0X134), WRITE_EXTERNAL_STORAGE(0X135)
}

fun async(block: () -> Unit) {
    Thread(block).start()
}

fun Context.obtainNetStatus(): Boolean {
    val connService: ConnectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    return connService?.activeNetworkInfo == null || connService?.activeNetworkInfo.isAvailable
}
