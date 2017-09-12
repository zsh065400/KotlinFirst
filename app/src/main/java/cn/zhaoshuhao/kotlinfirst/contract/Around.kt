package cn.zhaoshuhao.kotlinfirst.contract

import android.content.Context
import android.text.TextUtils
import cn.zhaoshuhao.kotlinfirst.model.network.common.GetAroundInfo
import cn.zhaoshuhao.kotlinfirst.model.network.common.LoadListener
import cn.zhaoshuhao.kotlinfirst.model.network.entity.Around
import cn.zhaoshuhao.kotlinfirst.utils.SPExt
import cn.zhaoshuhao.kotlinfirst.utils.jsonToList
import cn.zhaoshuhao.kotlinfirst.utils.listToJson

/**
 * Created by Scout
 * Created on 2017/9/10 16:12.
 */
class AroundPresent(val context: Context) : cn.zhaoshuhao.kotlinfirst.contract.Around.Present {
    private lateinit var aroundView: cn.zhaoshuhao.kotlinfirst.contract.Around.View
    private lateinit var aroundData: ArrayList<Around>
    private var aroundJson: String by SPExt.SpDelegate(context, "around", "")

    override fun onStart() {
        super.onStart()
        getAroundInfo()
    }

    override fun setView(view: cn.zhaoshuhao.kotlinfirst.contract.Around.View) {
        this.aroundView = view
    }

    override fun getAroundInfo() {
        GetAroundInfo.execute(object : LoadListener<ArrayList<Around>> {
            override fun onSuccess(data: ArrayList<Around>?) {
                if (data != null) {
                    aroundView.initAroundInfo(data)
                    println(data.toString())
                    aroundData = data
                    aroundJson = listToJson(aroundData)
                } else {
                    aroundView.loadError()
                }
                aroundView.onRefreshComplete()
            }

            override fun onLoadLocalData() {
                loadLocalData()
            }
        })
    }

    private fun loadLocalData() {
        aroundData = if (!TextUtils.isEmpty(aroundJson)) {
            val datas = jsonToList<Around>(aroundJson)
            aroundView.initAroundInfo(datas)
            datas
        } else {
            aroundView.loadError()
            arrayListOf()
        }
        aroundView.onRefreshComplete()
    }

    override fun filterData(group: Int, term: String) {
        if (aroundData != null && aroundData.isEmpty()) return
        if (term == "全部分类" || term == "综合排序" || term == "优惠活动") return
        else
            when (group) {
                0 -> {
                    aroundData.filter { it.type == term }.let { aroundView.onDataChanged(it) }
                }
                1 -> {
                    aroundData.sortedBy { it.delivery }.let { aroundView.onDataChanged(it) }
                }
                2 -> {
                    aroundData.filter { it.exercise == term }.let { aroundView.onDataChanged(it) }
                }
            }
    }

}

interface Around {
    interface Present : Base.Present<cn.zhaoshuhao.kotlinfirst.contract.Around.View> {
        fun getAroundInfo()

        fun filterData(group: Int, term: String)
    }

    interface View : Base.View<cn.zhaoshuhao.kotlinfirst.contract.Around.Present> {
        fun initAroundInfo(info: ArrayList<Around>)

        fun loadError()

        fun onRefreshComplete()

        fun onDataChanged(after: List<Around>)
    }
}
