package cn.zhaoshuhao.kotlinfirst.ui.activity

import android.app.Activity
import android.content.Intent
import android.support.v7.widget.Toolbar
import c.b.BP
import c.b.PListener
import c.b.QListener
import cn.zhaoshuhao.kotlinfirst.R
import cn.zhaoshuhao.kotlinfirst.adapter.OrderAdapter
import cn.zhaoshuhao.kotlinfirst.base.BaseActivity
import cn.zhaoshuhao.kotlinfirst.base.toActivityForResult
import cn.zhaoshuhao.kotlinfirst.base.toast
import cn.zhaoshuhao.kotlinfirst.model.bean.Address
import cn.zhaoshuhao.kotlinfirst.model.bean.ShoppingCart
import cn.zhaoshuhao.kotlinfirst.utils.defaultToolbarOptions
import cn.zhaoshuhao.kotlinfirst.utils.s2n
import cn.zhaoshuhao.kotlinfirst.utils.vertical
import kotlinx.android.synthetic.main.activity_order.*

class OrderActivity : BaseActivity() {
    private var datas: ArrayList<ShoppingCart> = arrayListOf()

    override fun obtainLayoutID(): Int = R.layout.activity_order

    override fun beforeInitViews() {
        val extras = intent.extras
        datas = extras.getParcelableArrayList<ShoppingCart>("products")
    }

    override fun initToolbar() {
        defaultToolbarOptions(id_toolbar as Toolbar, "确认订单") { finish() }
    }

    override fun initViews() {
        id_order_rv_product.vertical(this)
        id_order_rv_product.adapter = OrderAdapter(this, datas, null)

        val sum = datas?.map { (it.price.s2n() * it.num.s2n()) }?.sum()
        id_order_tv_count_price.text = "￥$sum"

        id_order_rl_address.setOnClickListener {
            toActivityForResult<AddressActivity>(0x123)
        }

        id_order_btn_buy.setOnClickListener {
            with(id_order_btn_buy) {
                isEnabled = false
                var orderId = ""
                BP.pay("天天爱生活", "这些是从仿拉手O2O购买的商品，此处仅为测试使用。多个商品状态下逻辑会有不同", 0.02, true, object : PListener {
                    override fun fail(p0: Int, p1: String?) {
                        isEnabled = true
                        toast("支付失败：用户取消或异常退出$p0, $p1")
                    }

                    override fun unknow() {
                        isEnabled = true
                        toast("因网络等原因，请稍后查询支付结果")
                    }

                    override fun succeed() {
                        BP.query(orderId, object : QListener {
                            override fun fail(p0: Int, p1: String?) {
                                //TODO 订单号在此处获得，支付失败，返回订单号和错误码

                            }

                            override fun succeed(p0: String?) {
                                //TODO 订单号在此处获得，确认支付成功
                                //查询成功(并不是说支付成功),返回的status有NOTPAY和SUCCESS两种可能
                            }

                        })
                        isEnabled = true
                    }

                    override fun orderId(p0: String?) {
                        //TODO 订单号在此处获得，用于我的页面订单处理，未付款和已付款
                        orderId = p0 ?: ""
                        isEnabled = true
                    }
                })
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 0x123 && resultCode == Activity.RESULT_OK) {
            val address = data?.extras?.getParcelable<Address>("address")
            if (address != null) {
                id_order_tv_person.text = "收货人：${address.name}"
                id_order_tv_phone.text = address.phone
                id_order_tv_address.text = "收货地址：${address.part.trim()}${address.detail.trim()}"
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}
