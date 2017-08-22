package cn.zhaoshuhao.kotlinfirst.adapter

import android.content.Context
import android.graphics.Paint
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import cn.zhaoshuhao.kotlinfirst.R
import cn.zhaoshuhao.kotlinfirst.model.network.entity.Film
import cn.zhaoshuhao.kotlinfirst.model.network.entity.GuessYouLike
import cn.zhaoshuhao.kotlinfirst.utils.findViewOften
import cn.zhaoshuhao.kotlinfirst.utils.obtainDefault
import com.bumptech.glide.Glide

/**
 * Created by Scout
 * Created on 2017/8/1 21:49.
 */
class YourLikeAdapter(context: Context, datas: ArrayList<GuessYouLike>, listener: BaseSupportAdapter.OnItemClickListener<GuessYouLike>?) : BaseSupportAdapter<GuessYouLike>(context, datas, listener) {
    override fun onCreateViewWithId(): Int = R.layout.main_you_like_item

    override fun onBindViewHolder(holder: SupportViewHolder?, position: Int) {
        with(holder!!) {
            val youLike = datas[position]
            val imageView = findView<ImageView>(R.id.id_ylike_image)
            val product = findView<TextView>(R.id.id_ylike_product)
            val title = findView<TextView>(R.id.id_ylike_title)
            val price = findView<TextView>(R.id.id_ylike_price)
            val value = findView<TextView>(R.id.id_ylike_value)
            val bought = findView<TextView>(R.id.id_ylike_bought)
            with(Glide.with(context)) {
                val images = youLike.images
                val imageUrl = images[2].image
                load(imageUrl).apply(obtainDefault()).into(imageView)
            }
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
    abstract fun onCreateViewWithId(): Int

    abstract override fun onBindViewHolder(holder: SupportViewHolder?, position: Int)

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): SupportViewHolder {
        val inflate = LayoutInflater.from(context).inflate(onCreateViewWithId(), parent, false)
        return SupportViewHolder(inflate)
    }

    override fun getItemCount(): Int = datas.size


    inner class SupportViewHolder(val root: View) : RecyclerView.ViewHolder(root) {
        init {
            if (listener != null) root.setOnClickListener {
                listener?.
                        onClick(datas[layoutPosition], layoutPosition)
            }
        }

        fun <T : View> findView(id: Int): T = root.findViewOften(id)
    }

    fun setOnItmeClickListener(listener: OnItemClickListener<T>) {
        this.listener = listener
    }

    interface OnItemClickListener<T> {
        fun onClick(data: T, position: Int)
    }
}
