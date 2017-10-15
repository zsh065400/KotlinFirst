package cn.zhaoshuhao.kotlinfirst.ui.activity

import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.Toolbar
import android.view.View
import cn.zhaoshuhao.kotlinfirst.R
import cn.zhaoshuhao.kotlinfirst.adapter.BaseSupportAdapter
import cn.zhaoshuhao.kotlinfirst.base.BaseActivity
import cn.zhaoshuhao.kotlinfirst.base.toast
import cn.zhaoshuhao.kotlinfirst.model.bean.History
import cn.zhaoshuhao.kotlinfirst.model.db.KHistoryDao
import cn.zhaoshuhao.kotlinfirst.ui.adapter.HistoryAdapter
import cn.zhaoshuhao.kotlinfirst.utils.defaultToolbarOptions
import cn.zhaoshuhao.kotlinfirst.utils.toList
import kotlinx.android.synthetic.main.activity_history.*

class HistoryActivity : BaseActivity() {
    private val historyDao: KHistoryDao
        get() = KHistoryDao.get(this)

    override fun obtainLayoutID(): Int = R.layout.activity_history

    private var histories: ArrayList<History>? = null

    override fun beforeInitViews() {
        histories = historyDao.queryAll().toList<History>()
        histories!!.reverse()
    }

    override fun initToolbar() {
        defaultToolbarOptions(id_history_toolbar as Toolbar, "最近浏览") { onBackPressed() }
    }

    override fun initViews() {
        id_history_rv_list.layoutManager = GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false)
        id_history_rv_list.adapter = HistoryAdapter(this, histories ?: arrayListOf(), object : BaseSupportAdapter.OnItemClickListener<History> {
            override fun onClick(view: View, data: History, position: Int) {
                toast("${data.url}")
            }
        })
    }
}
