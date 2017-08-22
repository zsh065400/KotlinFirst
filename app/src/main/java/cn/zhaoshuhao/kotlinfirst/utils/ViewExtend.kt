package cn.zhaoshuhao.kotlinfirst.utils

import android.util.SparseArray
import android.view.View
import android.widget.ProgressBar
import cn.zhaoshuhao.kotlinfirst.model.bean.WebViewInfo
import com.tencent.smtt.export.external.interfaces.WebResourceError
import com.tencent.smtt.export.external.interfaces.WebResourceRequest
import com.tencent.smtt.sdk.WebChromeClient
import com.tencent.smtt.sdk.WebView
import com.tencent.smtt.sdk.WebViewClient

/**
 * Created by Scout
 * Created on 2017/7/28 19:53.
 */
/*
* 利用接收者是同一对象的原理，扩展获取其保存的值并作单独缓存
* */
fun <T : View> View.findViewOften(viewId: Int): T {
    var viewHolder: SparseArray<View> = tag as? SparseArray<View> ?: SparseArray()
    tag = viewHolder
    var childView: View? = viewHolder.get(viewId)
    if (null == childView) {
        childView = findViewById(viewId)
        viewHolder.put(viewId, childView)
    }
    return childView as T
}

fun WebView.show(info: WebViewInfo, progress: ProgressBar) {
    this.loadUrl(info.url)
    with(this.settings) {
        javaScriptEnabled = true
        domStorageEnabled = true
    }
    progress.visibility = View.VISIBLE
    setWebChromeClient(object : WebChromeClient() {
        override fun onProgressChanged(view: WebView?, newProgress: Int) {
            if (newProgress == 100) progress.visibility = View.GONE else progress.progress = newProgress
            super.onProgressChanged(view, newProgress)
        }
    })
    setWebViewClient(object : WebViewClient() {
        override fun shouldOverrideUrlLoading(p0: WebView?, p1: String?): Boolean {
            p0?.loadUrl(p1)
            return true
        }

        override fun onPageFinished(p0: WebView?, p1: String?) {
            super.onPageFinished(p0, p1)
            progress.visibility = View.GONE
        }

        override fun onReceivedError(p0: WebView?, p1: WebResourceRequest?, p2: WebResourceError?) {
            super.onReceivedError(p0, p1, p2)
            progress.visibility = View.GONE
        }
    })
}
