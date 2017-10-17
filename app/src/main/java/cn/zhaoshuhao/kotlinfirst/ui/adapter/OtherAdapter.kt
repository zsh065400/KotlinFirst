package cn.zhaoshuhao.kotlinfirst.ui.adapter

import android.content.Context
import android.widget.ImageView
import android.widget.TextView
import cn.zhaoshuhao.kotlinfirst.R
import cn.zhaoshuhao.kotlinfirst.adapter.BaseSupportAdapter
import cn.zhaoshuhao.kotlinfirst.model.bean.History
import cn.zhaoshuhao.kotlinfirst.model.bean.Star
import cn.zhaoshuhao.kotlinfirst.utils.loadUrl

/**
 * Created by Scout
 * Created on 2017/10/13 9:23.
 */
class StarAdapter(context: Context, datas: ArrayList<Star>, listener: BaseSupportAdapter.OnItemClickListener<Star>?) : BaseSupportAdapter<Star>(context, datas, listener) {
    override fun onCreateViewWithId(): Int = R.layout.layout_history_and_star

    override fun onBindViewHolder(holder: SupportViewHolder?, position: Int) {
        if (holder != null) {
            with(holder) {
                val history = datas[position]
                val title = findView<TextView>(R.id.id_tv_title)
                val price = findView<TextView>(R.id.id_tv_price)
                val thumb = findView<ImageView>(R.id.id_iv_thumb)

                title.text = history.title
                price.text = "￥${history.price}"
                thumb.loadUrl(context, history.imgUrl)
            }
        }
    }
}

class HistoryAdapter(context: Context, datas: ArrayList<History>, listener: BaseSupportAdapter.OnItemClickListener<History>?) : BaseSupportAdapter<History>(context, datas, listener) {
    override fun onCreateViewWithId(): Int = R.layout.layout_history_and_star

    override fun onBindViewHolder(holder: SupportViewHolder?, position: Int) {
        if (holder != null) {
            with(holder) {
                val history = datas[position]
                val title = findView<TextView>(R.id.id_tv_title)
                val price = findView<TextView>(R.id.id_tv_price)
                val thumb = findView<ImageView>(R.id.id_iv_thumb)

                title.text = history.title
                price.text = "￥${history.price}"
                thumb.loadUrl(context, history.imgUrl)
            }
        }
    }
}
