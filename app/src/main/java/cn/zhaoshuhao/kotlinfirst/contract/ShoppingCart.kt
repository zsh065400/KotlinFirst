package cn.zhaoshuhao.kotlinfirst.contract

import android.content.Context
import cn.bmob.v3.BmobUser
import cn.zhaoshuhao.kotlinfirst.model.bean.User
import cn.zhaoshuhao.kotlinfirst.model.db.KCartDao
import cn.zhaoshuhao.kotlinfirst.utils.*
import org.jetbrains.anko.toast
import cn.zhaoshuhao.kotlinfirst.model.bean.ShoppingCart as CartData

/**
 * Created by Scout
 * Created on 2017/9/16 11:45.
 */
class CartPresent(val context: Context) : ShoppingCart.Present {
    private lateinit var cartView: ShoppingCart.View

    private var cartData: ArrayList<CartData>? = null
    private var cartJson: String by SPExt.SpDelegate(context, "cart", "")
    private val cartDao: KCartDao = KCartDao.get(context)
    override fun setView(view: ShoppingCart.View) {
        this.cartView = view
    }

    override fun onStart() {
        loadCartData()
    }

    override fun loadCartData() {
        val cursor = cartDao.queryAll()
        cartData = if (cursor != null && cursor.count != 0) {
            cursor.toList<cn.zhaoshuhao.kotlinfirst.model.bean.ShoppingCart>()
        } else {
            arrayListOf()
        }
        cartView.onLoadComplete(cartData!!)
        asyncWithDelayInUiThread(1000) { cartView.onRefreshComplete() }
        doCalcMoney()
    }

    override fun removeCartData() {
        if (cartData == null || cartData?.size == 0) return
        else {
            cartData!!.filter { it.checked }.forEachIndexed { index, shoppingCart ->
                println(shoppingCart.toString())
                cartDao.deleteForAbs("name", shoppingCart.name)
            }.let {
                cartView.onRemoveComplete()
                loadCartData()
            }
        }
    }

    override fun updateCartData(id: Int, num: Int) {
    }

    override fun doBuyAction() {
        if (BmobUser.getCurrentUser(User::class.java) == null) {
            context.toast("请先登录")
            return
        }
        if (cartData != null && cartData!!.size > 0) {
            val filter = cartData!!.filter { it.checked }.toArrayList()
            val size = filter.size
            if (size == 0) return
            else {
                cartView.commitOrder(filter)
            }
        }
    }

    override fun doCalcMoney() {
        if (cartData == null || cartData?.size == 0) cartView.onCalcDone(0.0, false)
        else {
            val allChecked = cartData!!.all { it.checked }

            cartData!!.filter { it.checked }.map { it.num.s2n() * it.price.s2n() }.sum().let { cartView.onCalcDone(it, allChecked) }
        }
    }

    override fun onStop() {
        cartData?.forEach {
            it.checked = false
            cartDao.updateForAbs("name", it.name, Pair("json", anyToJson(it)))
        }
    }

}

interface ShoppingCart {
    interface View : Base.View<Present> {
        fun onLoadComplete(datas: ArrayList<CartData>)

        /*底层删除后需要更新Adapter的数据集*/
        fun onRemoveComplete()

        fun onBuyComplete()

        fun onRefreshComplete()

        fun onCalcDone(moeny: Double, isAllChecked: Boolean)

        fun commitOrder(datas: ArrayList<CartData>)
    }

    interface Present : Base.Present<View> {
        fun loadCartData()

        fun removeCartData()

        fun updateCartData(id: Int, num: Int)

        fun doCalcMoney()

        fun doBuyAction()
    }
}
