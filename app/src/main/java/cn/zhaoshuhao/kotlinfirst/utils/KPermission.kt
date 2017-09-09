package cn.zhaoshuhao.kotlinfirst.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.support.v4.app.ActivityCompat


/**
 * Created by Scout
 * Created on 2017/8/23 15:41.
 */
object KPermission {
    private var handle: IPermissionResult? = null
    private var requestCode: Int? = null

    private lateinit var handleOfLambda: ((gradnet: List<String>, denied: List<String>, neverShow: List<String>) -> Unit);

    private var isGranted: List<String> = listOf()

    /*避免同一时间多次请求，可不指定请求代码*/

    fun checkSelf(permissions: List<String>, activity: Activity, callback: (List<out String>, List<String>) -> Unit) {
        val instance = PermissionRequest(activity, 0, permissions)
        instance.checkSelf(callback)
    }

    fun request(vararg permissions: String, activity: Activity, before: IShowRationable? = null, handle: IPermissionResult, requestCode: Int = 0x111) {
        this.handle = handle
        this.requestCode = requestCode
        checkSelf(permissions.asList(), activity) { g, d ->
            if (d.isNotEmpty()) {
                val instance = PermissionRequest(activity, requestCode, d)
                isGranted = g
                before?.onPrepareRequest(instance) ?: instance.proceed()
            } else {
                handle.onGranted(g)
                handle.onDenied(d)
                handle.onNeverShow(listOf())
            }
        }
    }

    fun requestOfLambda(vararg permissions: String, activity: Activity, before: IShowRationable? = null, requestCode: Int = 0x111, handleOfLambda: ((gradnet: List<out String>, denied: List<out String>, neverShow: List<out String>) -> Unit)) {
        val p = permissions.asList()
        this.handleOfLambda = handleOfLambda
        this.requestCode = requestCode
        checkSelf(p, activity) { g, d ->
            if (d.isNotEmpty()) {
                val instance = PermissionRequest(activity, requestCode, d)
                isGranted = g
                before?.onPrepareRequest(instance) ?: instance.proceed()
            } else {
                handleOfLambda(g, d, listOf())
            }
        }
    }

    fun handleResult(activity: Activity, requestCode: Int, permissions: List<String>, grantResults: IntArray) {
        if (this.requestCode != requestCode) return
        val granted = ArrayList<String>()
        val denied = ArrayList<String>()
        val neverShow = ArrayList<String>()
        for (i in 0 until permissions.size) {
            val permission = permissions[i]
            val result = grantResults[i]
            if (result == PackageManager.PERMISSION_GRANTED) granted.add(permission)
            else if (!ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) neverShow.add(permission)
            else denied.add(permission)
        }
        handle?.onGranted(granted.plus(isGranted))
        handle?.onDenied(denied)
        handle?.onNeverShow(neverShow)
    }

    fun handleResultOfLambda(activity: Activity, requestCode: Int, permissions: List<String>, grantResults: IntArray) {
        if (this.requestCode != requestCode) return
        val granted = ArrayList<String>()
        val denied = ArrayList<String>()
        val neverShow = ArrayList<String>()
        for (i in 0 until permissions.size) {
            val permission = permissions[i]
            val result = grantResults[i]
            if (result == PackageManager.PERMISSION_GRANTED) granted.add(permission)
            else if (!ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) neverShow.add(permission)
            else denied.add(permission)
        }
        handleOfLambda(granted.plus(isGranted), denied, neverShow)
    }

    /*跳转到权限设置*/
    private fun getAppDetailSettingIntent(context: Context) {
        val localIntent = Intent()
        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        if (Build.VERSION.SDK_INT >= 9) {
            localIntent.action = "android.settings.APPLICATION_DETAILS_SETTINGS"
            localIntent.data = Uri.fromParts("package", context.packageName, null)
        } else if (Build.VERSION.SDK_INT <= 8) {
            localIntent.action = Intent.ACTION_VIEW
            localIntent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails")
            localIntent.putExtra("com.android.settings.ApplicationPkgName", context.packageName)
        }
        context.startActivity(localIntent)
    }
}

data class PermissionRequest(private val activity: Activity,
                             private val requestCode: Int,
                             private val permissions: List<String>) {
    /**
     * 检查需要请求的权限中未授权的权限
     * */
    fun checkSelf(callback: (List<String>, List<String>) -> Unit) {
        val map = permissions.groupBy { ActivityCompat.checkSelfPermission(activity, it) == PackageManager.PERMISSION_GRANTED }
        val granted = map[true]
        val denied = map[false]
        callback(granted ?: ArrayList<String>(), denied ?: ArrayList<String>())
    }

    fun proceed() {
        ActivityCompat.requestPermissions(activity, permissions.toTypedArray(), requestCode)
        log(LogType.INFO, "KPermission : PermissionRequest, proceed -----> ", permissions.let { it.toString() })
    }

    fun cancel() {
        log(LogType.INFO, "KPermission : PermissionRequest, cancel -----> ", permissions.toString())
    }
}

interface IShowRationable {
    fun onPrepareRequest(requestBody: PermissionRequest)
}

interface IPermissionResult {
    fun onGranted(permission: List<String>){}

    fun onDenied(permission: List<String>){}

    fun onNeverShow(permission: List<String>){}
}
