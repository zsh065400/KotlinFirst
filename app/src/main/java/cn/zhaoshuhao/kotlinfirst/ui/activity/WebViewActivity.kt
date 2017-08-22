package cn.zhaoshuhao.kotlinfirst.ui.activity

import android.os.Bundle
import android.support.v7.widget.Toolbar
import cn.zhaoshuhao.kotlinfirst.R
import cn.zhaoshuhao.kotlinfirst.base.BaseActivity
import cn.zhaoshuhao.kotlinfirst.model.bean.WebViewInfo
import cn.zhaoshuhao.kotlinfirst.utils.show
import kotlinx.android.synthetic.main.webview.*

class WebViewActivity : BaseActivity() {

    override fun obtainLayoutID(): Int = R.layout.webview

    private lateinit var mInfo: WebViewInfo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun beforeSetContentView() {
        val bundle = intent.extras
        mInfo = bundle.getParcelable<WebViewInfo>("webinfo")
    }

    override fun initViews() {
        super.initViews()
        id_web_detail.show(mInfo, id_progress_load)
    }

    override fun initToolbar(): Unit = with(id_toolbar as Toolbar) {
        title = mInfo.title/*在设置supportActionBar之前设置标题*/
        setSupportActionBar(this)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setNavigationOnClickListener { finish() }
    }

}
