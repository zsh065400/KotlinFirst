package cn.zhaoshuhao.kotlinfirst.base

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * Created by Scout
 * Created on 2017/8/12 22:46.
 */
abstract class BaseFragment : Fragment() {
    abstract fun obtainLayoutID(): Int

    abstract fun initView(view: View?, savedBundle: Bundle?)

    final override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(obtainLayoutID(), container, false)
    }

    final override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view, savedInstanceState)
    }
}
