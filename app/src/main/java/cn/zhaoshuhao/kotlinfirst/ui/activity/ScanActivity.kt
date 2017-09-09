package cn.zhaoshuhao.kotlinfirst.ui.activity

import android.content.Context
import android.os.Vibrator
import android.support.v7.widget.Toolbar
import android.view.View
import cn.bingoogolapple.qrcode.core.QRCodeView
import cn.zhaoshuhao.kotlinfirst.R
import cn.zhaoshuhao.kotlinfirst.base.BaseActivity
import cn.zhaoshuhao.kotlinfirst.utils.KPermission
import cn.zhaoshuhao.kotlinfirst.utils.defaultToolbarOptions
import kotlinx.android.synthetic.main.activity_scan.*

/**
 * Created by Scout
 * Created on 2017/9/1 11:05.
 */
class ScanActivity : BaseActivity(), QRCodeView.Delegate {
    override fun obtainLayoutID(): Int = R.layout.activity_scan

    override fun initToolbar(): Unit = defaultToolbarOptions(id_scan_toolbar as Toolbar, "扫一扫", View.OnClickListener { finish() })

    override fun initViews() {
        id_scan_zbar.setDelegate(this)
    }

    override fun onStart() {
        super.onStart()
        id_scan_zbar.startCamera()
        id_scan_zbar.showScanRect()
    }

    override fun onStop() {
        id_scan_zbar.stopCamera()
        super.onStop()
    }

    override fun onDestroy() {
        id_scan_zbar.onDestroy()
        super.onDestroy()
    }

    private fun vibrate() = {
        val vibrator: Vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        vibrator.vibrate(200)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        KPermission.handleResultOfLambda(this, requestCode, permissions.asList(), grantResults)
    }

    override fun onScanQRCodeSuccess(result: String?) {
        logd("Scan Result -----> $result")
        vibrate()
        id_scan_zbar.startSpot()
    }

    override fun onScanQRCodeOpenCameraError() {
        logd("打开相机出错")
    }
}
