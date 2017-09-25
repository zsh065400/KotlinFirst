package cn.zhaoshuhao.kotlinfirst.adapter

import android.content.Context
import android.graphics.Paint
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.ImageView
import android.widget.TextView
import cn.zhaoshuhao.kotlinfirst.R
import cn.zhaoshuhao.kotlinfirst.model.bean.Address
import cn.zhaoshuhao.kotlinfirst.model.bean.ShoppingCart
import cn.zhaoshuhao.kotlinfirst.model.network.entity.Around
import cn.zhaoshuhao.kotlinfirst.model.network.entity.Film
import cn.zhaoshuhao.kotlinfirst.model.network.entity.GuessYouLike
import cn.zhaoshuhao.kotlinfirst.utils.findViewOften
import cn.zhaoshuhao.kotlinfirst.utils.load
import cn.zhaoshuhao.kotlinfirst.utils.obtainDefault
import cn.zhaoshuhao.kotlinfirst.widget.CartNumberChanged
import com.bumptech.glide.Glide

/**
 * Created by Scout
 * Created on 2017/8/1 21:49.
 */
class AddressAdapter(context: Context, datas: ArrayList<Address>, listener: BaseSupportAdapter.OnItemClickListener<Address>?) : BaseSupportAdapter<Address>(context, datas, listener) {

    override fun onCreateViewWithId(): Int = R.layout.item_address

    override fun onBindViewHolder(holder: SupportViewHolder?, position: Int) {
        if (holder != null) {
            with(holder) {
                val addr = datas[position]
                val person = findView<TextView>(R.id.id_address_tv_person)
                val phone = findView<TextView>(R.id.id_address_tv_phone)
                val address = findView<TextView>(R.id.id_address_tv_address)

                person.text = "收货人：${addr.name}"
                phone.text = addr.phone
                address.text = "地址：${addr.part.trim()}${addr.detail.trim()}"
            }
        }
    }

}

class OrderAdapter(context: Context, datas: ArrayList<ShoppingCart>, listener: BaseSupportAdapter.OnItemClickListener<ShoppingCart>?) : BaseSupportAdapter<ShoppingCart>(context, datas, listener) {
    override fun onCreateViewWithId(): Int = R.layout.item_order_product

    override fun onBindViewHolder(holder: SupportViewHolder?, position: Int) {
        if (holder != null) {
            with(holder) {
                val cart = datas[position]
                val icon = findView<ImageView>(R.id.id_order_iv_icon)
                val name = findView<TextView>(R.id.id_order_tv_name)
                val price = findView<TextView>(R.id.id_order_tv_price)
                val num = findView<TextView>(R.id.id_order_tv_num)

                icon.load(context, cart.img)
                name.text = cart.name
                price.text = cart.price
                num.text = "x${cart.num}"
            }
        }
    }
}

class CartAdapter(context: Context, datas: ArrayList<ShoppingCart>, listener: BaseSupportAdapter.OnItemClickListener<ShoppingCart>?) : BaseSupportAdapter<ShoppingCart>(context, datas, listener) {

    override fun onCreateViewWithId(): Int = R.layout.item_cart

    override fun onBindViewHolder(holder: SupportViewHolder?, position: Int) {
        if (holder == null) return
        else
            with(holder) {
                val cart = datas[position]
                val icon = findView<ImageView>(R.id.id_cart_iv_icon)
                val name = findView<TextView>(R.id.id_cart_tv_name)
                val price = findView<TextView>(R.id.id_cart_tv_price)
                val value = findView<TextView>(R.id.id_cart_tv_value)
                val num = findView<CartNumberChanged>(R.id.id_cart_num)
                val checkbox = findView<CheckBox>(R.id.id_cart_cb_choice)
                checkbox.isChecked = false

                checkbox.setOnCheckedChangeListener { btn, checked ->
                    checkedChange?.invoke(btn, checked, cart)
                }
                num.changedCallBack = { view, value ->
                    changedCallback?.invoke(view, value, cart)
                }

                icon.load(context, cart.img)
                name.text = cart.name
                price.text = cart.price
                value.text = cart.value
                value.paint.flags = Paint.STRIKE_THRU_TEXT_FLAG
                num.value = cart.num.toInt()
            }
    }

    private var checkedChange: ((CompoundButton, Boolean, ShoppingCart) -> Unit)? = null
    private var changedCallback: ((View, Int, ShoppingCart) -> Unit)? = null

    fun setItemChoiceListener(block: (CompoundButton, Boolean, ShoppingCart) -> Unit) {
        checkedChange = block
    }

    fun setNumChangedListener(block: (View, Int, ShoppingCart) -> Unit) {
        changedCallback = block
    }
}

class AroundInfoAdapter(context: Context, datas: ArrayList<Around>, listener: BaseSupportAdapter.OnItemClickListener<Around>?) : BaseSupportAdapter<Around>(context, datas, listener) {
    override fun onCreateViewWithId(): Int = R.layout.around_item

    override fun onBindViewHolder(holder: SupportViewHolder?, position: Int) {
        if (holder == null) return
        else
            with(holder) {
                val around = datas[position]
                val title = findView<TextView>(R.id.id_around_title)
                val price = findView<TextView>(R.id.id_around_price)
                val origin = findView<TextView>(R.id.id_around_origin)
                val delivery = findView<TextView>(R.id.id_around_delivery)
                val image = findView<ImageView>(R.id.id_around_image)

                image.load(context, around.img)
                title.text = around.name
                price.text = around.price.toString()
                origin.text = around.origin.toString()
                origin.paint.flags = Paint.STRIKE_THRU_TEXT_FLAG
                delivery.text = "配送费：${around.delivery.toString()}"
            }
    }
}

class YourLikeAdapter(context: Context, datas: ArrayList<GuessYouLike>, listener: BaseSupportAdapter.OnItemClickListener<GuessYouLike>?) : BaseSupportAdapter<GuessYouLike>(context, datas, listener) {
    override fun onCreateViewWithId(): Int = R.layout.main_you_like_item

    override fun onBindViewHolder(holder: SupportViewHolder?, position: Int) {
        if (holder == null) return
        else
            with(holder) {
                val youLike = datas[position]
                val imageView = findView<ImageView>(R.id.id_ylike_image)
                val product = findView<TextView>(R.id.id_ylike_product)
                val title = findView<TextView>(R.id.id_ylike_title)
                val price = findView<TextView>(R.id.id_ylike_price)
                val value = findView<TextView>(R.id.id_ylike_value)
                val bought = findView<TextView>(R.id.id_ylike_bought)
                val imageUrl = youLike.images[2].image
                imageView.load(context, imageUrl)

                product.text = youLike.product
                title.text = youLike.title
                price.text = youLike.price
                value.text = youLike.value
                value.paint.flags = Paint.STRIKE_THRU_TEXT_FLAG/*中线*/
                bought.text = "已售${youLike.bought.toString()}份"
            }
    }
}

class FilmAdapter(context: Context, datas: ArrayList<Film>, listener: OnItemClickListener<Film>? = null) : BaseSupportAdapter<Film>(context, datas, listener) {
    override fun onCreateViewWithId(): Int = R.layout.main_film_item

    override fun onBindViewHolder(holder: SupportViewHolder?, position: Int) {
        val film: Film = datas[position] as Film
        with(film) {
            val imageView = holder?.findView<ImageView>(R.id.id_iv_film_thumb)
            with(Glide.with(context)) { load(imageUrl).apply(obtainDefault()).into(imageView) }
            holder?.findView<TextView>(R.id.id_tv_film_name)?.text = filmName
            holder?.findView<TextView>(R.id.id_tv_film_cost)?.text = grade
        }
    }

}

abstract class BaseSupportAdapter<T>(val context: Context, val datas: ArrayList<T>, var listener: OnItemClickListener<T>? = null) : RecyclerView.Adapter<BaseSupportAdapter<T>.SupportViewHolder>() {
    open var viewHolder: SupportViewHolder? = null

    abstract fun onCreateViewWithId(): Int

    abstract override fun onBindViewHolder(holder: SupportViewHolder?, position: Int)

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): SupportViewHolder {
        val inflate = LayoutInflater.from(context).inflate(onCreateViewWithId(), parent, false)
        viewHolder = SupportViewHolder(inflate)
        return viewHolder!!
    }

    override fun getItemCount(): Int = datas.size

    open fun reset(newDatas: List<T>) {
        datas.clear()
        datas.addAll(newDatas)
        this.notifyDataSetChanged()
    }

    inner class SupportViewHolder(val root: View) : RecyclerView.ViewHolder(root) {
        init {
            if (listener != null) root.setOnClickListener {
                listener?.
                        onClick(root, datas[layoutPosition], layoutPosition)
            }
            if (longListener != null) root.setOnLongClickListener {
                longListener?.onLongClick(root, datas[layoutPosition], layoutPosition)
                true
            }
        }

        fun <T : View> findView(id: Int): T = root.findViewOften(id)
    }

    fun setOnItmeClickListener(listener: OnItemClickListener<T>) {
        this.listener = listener
    }

    var longListener: OnItemLongClickListener<T>? = null

    fun setOnItmeLongClickListener(listener: OnItemLongClickListener<T>) {
        this.longListener = listener
    }

    interface OnItemClickListener<T> {
        fun onClick(view: View, data: T, position: Int)
    }

    interface OnItemLongClickListener<T> {
        fun onLongClick(view: View, data: T, position: Int)
    }
}
