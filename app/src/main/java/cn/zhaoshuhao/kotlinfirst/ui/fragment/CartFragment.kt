package cn.zhaoshuhao.kotlinfirst.fragment

import android.animation.ObjectAnimator
import android.content.Context
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.view.View
import android.widget.CheckBox
import cn.zhaoshuhao.kotlinfirst.R
import cn.zhaoshuhao.kotlinfirst.adapter.BaseSupportAdapter
import cn.zhaoshuhao.kotlinfirst.adapter.CartAdapter
import cn.zhaoshuhao.kotlinfirst.base.BaseFragment
import cn.zhaoshuhao.kotlinfirst.contract.ShoppingCart
import cn.zhaoshuhao.kotlinfirst.ui.activity.IRefreshListener
import cn.zhaoshuhao.kotlinfirst.utils.default
import cn.zhaoshuhao.kotlinfirst.utils.dp2px
import cn.zhaoshuhao.kotlinfirst.utils.vertical
import kotlinx.android.synthetic.main.fragment_cart.*
import org.jetbrains.anko.support.v4.alert

/**
 * Created by Scout
 * Created on 2017/7/19 18:42.
 */
class CartFragment : BaseFragment(), ShoppingCart.View {
    private lateinit var cartPresent: ShoppingCart.Present

    private var refreshListener: IRefreshListener? = null
    private var adapter: CartAdapter? = null

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        refreshListener = context as IRefreshListener
    }

    override fun setPresent(present: ShoppingCart.Present) {
        this.cartPresent = present
    }

    override fun onLoadComplete(datas: ArrayList<cn.zhaoshuhao.kotlinfirst.model.bean.ShoppingCart>) {
        if (adapter == null) {
            adapter = CartAdapter(context, datas, object : BaseSupportAdapter.OnItemClickListener<cn.zhaoshuhao.kotlinfirst.model.bean.ShoppingCart> {
                override fun onClick(view: View, data: cn.zhaoshuhao.kotlinfirst.model.bean.ShoppingCart, position: Int) {
                    val checkBox = view.findViewById<CheckBox>(R.id.id_cart_cb_choice)
                    checkBox.isChecked = !checkBox.isChecked
                }
            })
            adapter!!.setItemChoiceListener { btn, checked, cart ->
                cart.checked = !cart.checked
                cartPresent.doCalcMoney()
                println(cart.toString())
            }
            adapter!!.setNumChangedListener { view, value, cart ->
                cart.num = "$value"
                cartPresent.doCalcMoney()
            }
            id_cart_rv_content.vertical(context)
            id_cart_rv_content.adapter = adapter
        } else {
            adapter!!.reset(datas)
        }
    }

    override fun onRemoveComplete() {
    }

    override fun onBuyComplete() {
    }

    override fun onCalcDone(money: Double, isAllChecked: Boolean) {
        id_cart_tv_final_price.text = "￥$money"
        id_cart_rb_all.isChecked = isAllChecked
    }

    override fun obtainLayoutID(): Int = R.layout.fragment_cart

    private var checked = false

    override fun initView(view: View?, savedBundle: Bundle?) {
        initRefreshLayout()
        id_cart_rb_all.setOnClickListener {
            if (!checked) {
                checked = true
                (0 until adapter!!.itemCount)
                        .map { id_cart_rv_content.layoutManager.findViewByPosition(it) }
                        .filter { !(it.findViewById<CheckBox>(R.id.id_cart_cb_choice).isChecked) }
                        .forEach { it.performClick() }
            } else {
                checked = false
                (0 until adapter!!.itemCount)
                        .map { id_cart_rv_content.layoutManager.findViewByPosition(it) }
                        .filter { (it.findViewById<CheckBox>(R.id.id_cart_cb_choice).isChecked) }
                        .forEach { it.performClick() }
            }
        }
    }

    private fun initRefreshLayout() {
        id_cart_refresh.default(SwipeRefreshLayout.OnRefreshListener {
            cartPresent.onStart()
            refreshListener!!.isRefreshing()
            changeBottomStatus(56)
        })
    }

    override fun onRefreshComplete() {
        id_cart_refresh.isRefreshing = false
        refreshListener!!.refreshComplete()
        changeBottomStatus(0)
        id_cart_rb_all.isChecked = false
    }

    private fun changeBottomStatus(margin: Int) {
//        val params = id_cart_bottom.layoutParams as ViewGroup.MarginLayoutParams
//        params.bottomMargin = context.dp2px(margin).toInt()
//        id_cart_bottom.layoutParams = params
        ObjectAnimator.ofFloat(id_cart_bottom, "translationY", context.dp2px(margin)).setDuration(300).start()
    }

    fun onRemoveData() {
        if (adapter != null && adapter!!.itemCount > 0)
            with(alert("是否删除这些商品", "提示")) {
                positiveButton("确认") { cartPresent.removeCartData() }
                negativeButton("取消") {}
                show()
            }
    }

    override fun onResume() {
        super.onResume()
        cartPresent.onStart()
    }

    override fun onStop() {
        super.onStop()
        cartPresent.onStop()
    }
}
