package cn.zhaoshuhao.kotlinfirst.ui.activity

import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.Toolbar
import android.view.View
import cn.bmob.v3.BmobQuery
import cn.bmob.v3.BmobUser
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.FindListener
import cn.bmob.v3.listener.UpdateListener
import cn.zhaoshuhao.kotlinfirst.R
import cn.zhaoshuhao.kotlinfirst.adapter.BaseSupportAdapter
import cn.zhaoshuhao.kotlinfirst.base.BaseActivity
import cn.zhaoshuhao.kotlinfirst.base.toast
import cn.zhaoshuhao.kotlinfirst.model.bean.Star
import cn.zhaoshuhao.kotlinfirst.model.bean.User
import cn.zhaoshuhao.kotlinfirst.ui.adapter.StarAdapter
import cn.zhaoshuhao.kotlinfirst.utils.defaultToolbarOptions
import cn.zhaoshuhao.kotlinfirst.utils.toArrayList
import kotlinx.android.synthetic.main.activity_star.*
import org.jetbrains.anko.alert

class StarActivity : BaseActivity() {
    override fun obtainLayoutID(): Int = R.layout.activity_star

    override fun beforeInitViews() {
        val query = BmobQuery<Star>()
        query.findObjects(object : FindListener<Star>() {
            override fun done(list: MutableList<Star>?, e: BmobException?) {
                if (e != null || list?.size == 0)
                    toast("暂无数据")
                else
                    initDatas(list)
            }
        })
    }

    private lateinit var datas: ArrayList<Star>

    private var adapter: StarAdapter? = null

    private fun initDatas(list: MutableList<Star>?) {
        this.datas = list?.toArrayList() ?: arrayListOf()
        adapter = StarAdapter(this,
                datas,
                object : BaseSupportAdapter.OnItemClickListener<Star> {
                    override fun onClick(view: View, data: Star, position: Int) {
                        toast(data.url)
                    }
                })
        adapter!!.setOnItmeLongClickListener(object : BaseSupportAdapter.OnItemLongClickListener<Star> {
            override fun onLongClick(view: View, data: Star, position: Int) {
                with(alert("是否取消收藏", "取消收藏")) {
                    positiveButton("确定") {
                        val star = Star(BmobUser.getCurrentUser(User::class.java))
                        star.objectId = data.objectId
                        star.delete(object : UpdateListener() {
                            override fun done(e: BmobException?) {
                                if (e == null) {
                                    toast("取消收藏成功")
                                    reset(data)
                                } else toast("操作失败")
                            }
                        })
                    }
                    negativeButton("取消") {}
                    show()
                }
            }
        })
        id_star_rv_list.adapter = adapter
    }

    private fun reset(data: Star) {
        datas.remove(data)
        val arrayList = datas.toArrayList()
        adapter!!.reset(arrayList)
    }

    override fun initViews() {
        id_star_rv_list.layoutManager = GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false)
    }

    override fun initToolbar() {
        defaultToolbarOptions(id_star_toolbar as Toolbar, "我的收藏") { onBackPressed() }
    }

}
