package cn.zhaoshuhao.kotlinfirst.fragment

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import cn.zhaoshuhao.kotlinfirst.R
import cn.zhaoshuhao.kotlinfirst.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_around.*

/**
 * Created by Scout
 * Created on 2017/7/19 18:42.
 */
class ARoundFragment : BaseFragment() {
    override fun initView(view: View?, savedBundle: Bundle?) {
        id_around_sp_type.onItemSelectedListener = object :AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
            }

        }

        id_around_sp_sort.onItemSelectedListener = object :AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
            }

        }

        id_around_sp_exercise.onItemSelectedListener = object :AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
            }

        }
    }

    override fun obtainLayoutID(): Int = R.layout.fragment_around

    override fun onResume() {
        super.onResume()
        checkout(activity as CheckoutToolbar)
    }
}
