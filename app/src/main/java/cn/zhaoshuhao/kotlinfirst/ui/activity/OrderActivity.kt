package cn.zhaoshuhao.kotlinfirst.ui.activity

import android.app.Activity
import android.content.Intent
import android.support.v7.widget.Toolbar
import c.b.BP
import c.b.PListener
import c.b.QListener
import cn.bmob.v3.BmobUser
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.SaveListener
import cn.bmob.v3.listener.UpdateListener
import cn.zhaoshuhao.kotlinfirst.R
import cn.zhaoshuhao.kotlinfirst.adapter.OrderAdapter
import cn.zhaoshuhao.kotlinfirst.base.BaseActivity
import cn.zhaoshuhao.kotlinfirst.base.toActivityForResult
import cn.zhaoshuhao.kotlinfirst.base.toast
import cn.zhaoshuhao.kotlinfirst.model.bean.Address
import cn.zhaoshuhao.kotlinfirst.model.bean.Order
import cn.zhaoshuhao.kotlinfirst.model.bean.ShoppingCart
import cn.zhaoshuhao.kotlinfirst.model.bean.User
import cn.zhaoshuhao.kotlinfirst.model.db.KAddressDao
import cn.zhaoshuhao.kotlinfirst.utils.*
import kotlinx.android.synthetic.main.activity_order.*

class OrderActivity : BaseActivity() {
    private var datas: ArrayList<ShoppingCart> = arrayListOf()
    private val addrDao = KAddressDao.get(this)

    override fun obtainLayoutID(): Int = R.layout.activity_order

    override fun beforeInitViews() {
        val extras = intent.extras
        datas = extras.getParcelableArrayList<ShoppingCart>("products")
    }

    override fun initToolbar() {
        defaultToolbarOptions(id_toolbar as Toolbar, "确认订单") { finish() }
    }

    override fun initViews() {
        /*列表*/
        id_order_rv_product.vertical(this)
        id_order_rv_product.adapter = OrderAdapter(this, datas, null)

        /*计算价钱*/
        val sum = datas?.map { (it.price.s2n() * it.num.s2n()) }?.sum()
        id_order_tv_count_price.text = "￥$sum"

        /*默认收货地址*/
        val default = addrDao.queryAll()?.toList<Address>()
        default.filter { it.default == "true" }.apply {
            if (this.isNotEmpty()) {
                val address = default[0]
                updateAddress(address)
            }
        }

        /*收货地址单击*/
        id_order_rl_address.setOnClickListener {
            toActivityForResult<AddressActivity>(0x123)
        }

        /*提交订单按钮*/
        id_order_btn_buy.setOnClickListener {
            with(id_order_btn_buy) {
                isEnabled = false
                var orderId = ""
                var order: Order? = null
                val user = BmobUser.getCurrentUser(User::class.java)
                val price = datas?.map { (it.price.s2n() * it.num.s2n()) }?.sum().toString()
                val products = anyToJson(datas)
                val num = datas.size.toString()
                var id = ""
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
                            /*在此处回调中向数据库写入订单信息*/
                            override fun fail(p0: Int, p1: String?) {
                                //TODO 订单号在此处获得，支付失败，返回订单号和错误码
                            }

                            override fun succeed(p0: String?) {
                                //TODO 订单号在此处获得，确认支付成功
                                //查询成功(并不是说支付成功),返回的status有NOTPAY和SUCCESS两种可能
                                if (order == null) order = Order(user, p0!!, price, products, num, "已付款")
                                else {
                                    order!!.status = "已付款"
                                    order!!.update(id!!, object : UpdateListener() {
                                        override fun done(p0: BmobException?) {
                                            toast("付款成功")
                                            /*可在此处跳转到另一页面*/
                                        }
                                    })
                                }
                            }

                        })
                        isEnabled = true
                    }

                    override fun orderId(p0: String?) {
                        //TODO 订单号在此处获得，用于我的页面订单处理，未付款和已付款
                        orderId = p0 ?: ""
                        isEnabled = true
                        order = Order(user, p0!!, price, products, num, "未付款")
                        order!!.save(object : SaveListener<String>() {
                            override fun done(objectId: String?, e: BmobException?) {
                                if (e == null) id = objectId!!
                            }
                        })
                    }
                })
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 0x123 && resultCode == Activity.RESULT_OK) {
            val address = data?.extras?.getParcelable<Address>("address")
            if (address != null) {
                updateAddress(address)
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun updateAddress(address: Address) {
        id_order_tv_person.text = "收货人：${address.name}"
        id_order_tv_phone.text = address.phone
        id_order_tv_address.text = "收货地址：${address.part.trim()}${address.detail.trim()}"
    }
}
