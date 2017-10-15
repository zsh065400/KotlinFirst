package cn.zhaoshuhao.kotlinfirst.ui.activity

import android.support.v7.widget.Toolbar
import android.view.View
import cn.zhaoshuhao.kotlinfirst.R
import cn.zhaoshuhao.kotlinfirst.adapter.ItemAdapter
import cn.zhaoshuhao.kotlinfirst.base.BaseActivity
import cn.zhaoshuhao.kotlinfirst.utils.defaultToolbarOptions
import kotlinx.android.synthetic.main.activity_order_detail.*

class OrderDetailActivity : BaseActivity() {
    override fun obtainLayoutID(): Int = R.layout.activity_order_detail

    private var index = 0

    override fun beforeInitViews() {
        /*向数据查询当前用户订单数据，并做数据筛选，展示到对应的fragment中，此处功能未开发*/
        index = intent.getIntExtra("index", 0)
    }

    override fun initViews() {
//        with(id_order_detail_tab) {
//            addTab(newTab().setText("全部"))
//            addTab(newTab().setText("已付款"))
//            addTab(newTab().setText("未付款"))
//            setTabTextColors(Color.WHITE, Color.GRAY)
//            setupWithViewPager(id_order_detail_vp)
//        }
        with(id_order_detail_vp) {
            val view = layoutInflater.inflate(R.layout.activity_blank, null, false)
            val view1 = layoutInflater.inflate(R.layout.activity_blank, null, false)
            val view2 = layoutInflater.inflate(R.layout.activity_blank, null, false)
            adapter = ItemAdapter(this@OrderDetailActivity, arrayListOf<View>(view, view1, view2), arrayListOf("全部", "已付款", "未付款"))
        }
        id_order_detail_tab.setupWithViewPager(id_order_detail_vp)
        id_order_detail_vp.currentItem = index
    }

    override fun initToolbar() {
        defaultToolbarOptions(id_order_detail_toolbar as Toolbar, "我的订单") { onBackPressed() }
    }
}
