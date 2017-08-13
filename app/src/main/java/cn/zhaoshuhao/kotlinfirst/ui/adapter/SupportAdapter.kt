package cn.zhaoshuhao.kotlinfirst.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import cn.zhaoshuhao.kotlinfirst.R
import cn.zhaoshuhao.kotlinfirst.model.bean.FilmItem
import cn.zhaoshuhao.kotlinfirst.utils.findViewOften

/**
 * Created by Scout
 * Created on 2017/8/1 21:49.
 */
class FilmAdapter(context: Context, datas: ArrayList<FilmItem>, listener: OnItemClickListener<FilmItem>? = null) : BaseSupportAdapter<FilmItem>(context, datas, listener) {
    override fun onCreateViewWithId(): Int = R.layout.film_item

    override fun onBindViewHolder(holder: SupportViewHolder?, position: Int) {
        val film: FilmItem = datas[position] as FilmItem
        with(film) {
            holder?.findView<ImageView>(R.id.id_iv_film_thumb)?.setImageResource(R.mipmap.ic_launcher)
            holder?.findView <TextView>(R.id.id_tv_film_name)?.text = name
            holder?.findView <TextView>(R.id.id_tv_film_cost)?.text = cost
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
