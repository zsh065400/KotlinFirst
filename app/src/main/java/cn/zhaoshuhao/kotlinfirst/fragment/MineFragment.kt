package cn.zhaoshuhao.kotlinfirst.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cn.zhaoshuhao.kotlinfirst.R
import kotlinx.android.synthetic.main.fragment_blank.*

/**
 * Created by Scout
 * Created on 2017/7/19 18:42.
 */
class MineFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val inflate = inflater?.inflate(R.layout.fragment_blank, container, false)
        return inflate
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        id_tv_blank.text = "我的"
    }

}
