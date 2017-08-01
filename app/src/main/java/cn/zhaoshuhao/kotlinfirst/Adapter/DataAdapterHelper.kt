package cn.zhaoshuhao.kotlinfirst.Adapter

import android.content.Context
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import cn.zhaoshuhao.kotlinfirst.R
import cn.zhaoshuhao.kotlinfirst.utils.ItemInfo
import cn.zhaoshuhao.kotlinfirst.utils.findViewOften

/**
 * Created by Scout
 * Created on 2017/7/28 19:30.
 */
class GridAdapter(context: Context, datas: List<ItemInfo>) : BaseItemAdapter<ItemInfo>(context, datas) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var root: View = convertView ?: LayoutInflater.from(context).inflate(R.layout.grid_item, parent, false)
//        var viewholder: GridItemViewHolder? = null
//        if (root == null) {
//        root = LayoutInflater.from(context).inflate(R.layout.grid_item, parent, false)
//            viewholder = GridItemViewHolder(root)
//            root.tag = viewholder
//        } else viewholder = root?.tag as GridItemViewHolder?

        val data = datas[position]
        root.findViewOften<TextView>(R.id.id_tv_grid_text).text = data.text
        root.findViewOften<ImageView>(R.id.id_iv_grid_icon).setImageResource(data.icon)
//        viewholder?.setText(R.id.id_tv_grid_text, data.text)
//        viewholder?.setIcon(R.id.id_iv_grid_icon, data.icon)
        return root
    }

    /*
    * 尽可能使用Kotlin的语法糖，利用语言手势写代码
    * */
    inner class GridItemViewHolder(val root: View) {
        var tvText: TextView
        var ivIcon: ImageView
        val views = SparseArray<View>()

        init {
            tvText = getTextView(R.id.id_tv_grid_text)
            ivIcon = getImageView(R.id.id_iv_grid_icon)
        }

        fun <T : View> getView(id: Int): T {
            var view = views[id]
            if (view == null) {
                view = root.findViewById(id)
                views.put(id, view)
            }
            return view as T
        }

        fun getTextView(id: Int): TextView = getView(id)
        fun getImageView(id: Int): ImageView = getView(id)

        fun setText(id: Int, text: String) {
            getTextView(id).text = text
        }

        fun setIcon(id: Int, resId: Int) {
            getImageView(id).setImageResource(resId)
        }

    }
}


abstract class BaseItemAdapter<out T>(val context: Context, val datas: List<T>) : BaseAdapter() {
    abstract override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View

    override fun getItem(p0: Int): T {
        return datas[p0]
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getCount(): Int {
        return datas.size
    }

}
