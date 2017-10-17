package cn.zhaoshuhao.kotlinfirst.fragment

import android.content.Context
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.AdapterView
import cn.zhaoshuhao.kotlinfirst.R
import cn.zhaoshuhao.kotlinfirst.adapter.AroundInfoAdapter
import cn.zhaoshuhao.kotlinfirst.adapter.BaseSupportAdapter
import cn.zhaoshuhao.kotlinfirst.base.BaseFragment
import cn.zhaoshuhao.kotlinfirst.base.toast
import cn.zhaoshuhao.kotlinfirst.contract.Around
import cn.zhaoshuhao.kotlinfirst.ui.activity.IRefreshListener
import cn.zhaoshuhao.kotlinfirst.utils.default
import kotlinx.android.synthetic.main.fragment_around.*

/**
 * Created by Scout
 * Created on 2017/7/19 18:42.
 */
class ARoundFragment : BaseFragment(), Around.View {
    private lateinit var aroundPresent: Around.Present
    private lateinit var refreshListener: IRefreshListener
    private lateinit var adapter: AroundInfoAdapter

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        refreshListener = context as IRefreshListener
    }

    private val tk: Array<String> by lazy {
        arrayOf("傻了吧，什么都没有", "叫你点，气死你", "就是让你看看，真想买啊", "想要的话给我打电话", "烦不烦啊，别点了", "就是什么都没有，你这人真没劲啊")
    }

    override fun setPresent(present: Around.Present) {
        this.aroundPresent = present
    }

    override fun initAroundInfo(info: ArrayList<cn.zhaoshuhao.kotlinfirst.model.network.entity.Around>) {
        println("调用")
        id_around_rv_content.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        id_around_rv_content.isNestedScrollingEnabled = false
        adapter = AroundInfoAdapter(context, info, object : BaseSupportAdapter.OnItemClickListener<cn.zhaoshuhao.kotlinfirst.model.network.entity.Around> {
            override fun onClick(view: View, data: cn.zhaoshuhao.kotlinfirst.model.network.entity.Around, position: Int) {
                context.toast(tk[(Math.random() * 6).toInt()])
            }
        })
        id_around_rv_content.adapter = adapter
        id_around_sp_type.setSelection(0)
        id_around_sp_sort.setSelection(0)
        id_around_sp_exercise.setSelection(0)
    }

    override fun onRefreshComplete() {
        id_around_refresh.isRefreshing = false
        refreshListener.refreshComplete()
    }

    override fun onDataChanged(after: List<cn.zhaoshuhao.kotlinfirst.model.network.entity.Around>) {
        adapter.reset(after)
    }

    override fun loadError() {

    }

    override fun initView(view: View?, savedBundle: Bundle?) {
        aroundPresent.onStart()
        initFilter()
        initRefreshLayout()
    }

    private fun initFilter() {
        id_around_sp_type.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                val term = id_around_sp_type.selectedItem.toString()
                if (term != "全部分类") {
                    aroundPresent.filterData(0, term)
                }
            }
        }

        id_around_sp_sort.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                val term = id_around_sp_sort.selectedItem.toString()
                if (term != "综合排序") {
                    aroundPresent.filterData(1, term)
                }
            }
        }

        id_around_sp_exercise.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                val term = id_around_sp_exercise.selectedItem.toString()
                if (term != "优惠活动") {
                    aroundPresent.filterData(2, term)
                }
            }
        }
    }

    private fun initRefreshLayout() {
        id_around_refresh.default(SwipeRefreshLayout.OnRefreshListener {
            aroundPresent.onStart()
            refreshListener.isRefreshing()
        })
    }

    override fun obtainLayoutID(): Int = R.layout.fragment_around
}
