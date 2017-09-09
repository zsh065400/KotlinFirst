package cn.zhaoshuhao.kotlinfirst.base

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cn.zhaoshuhao.kotlinfirst.utils.LogType
import cn.zhaoshuhao.kotlinfirst.utils.componet
import cn.zhaoshuhao.kotlinfirst.utils.log

/**
 * Created by Scout
 * Created on 2017/8/12 22:46.
 */
abstract class BaseFragment : Fragment() {
    val logd = (::log.componet())(LogType.DEBUG)(this::class.java.toString())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    abstract fun obtainLayoutID(): Int

    abstract fun initView(view: View?, savedBundle: Bundle?)

    final override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(obtainLayoutID(), container, false)
    }

    final override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view, savedInstanceState)
    }

    /*
    * 在这里进行一些初始化，第一时间调用
    * */
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (!resotreStateFromArguments()) {
            //如果不存在需要恢复的数据，则初始化
            onActivityComplete()
        }
    }

    open fun onActivityComplete() {}

    /*
    * 保存数据，未避免多种情况的发生，需要对Framgnet的多种可能发生情况进行处理
    * */
    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        saveStateToArguments()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        saveStateToArguments()
    }

    private var savedState: Bundle? = null

    private fun saveStateToArguments() {
        if (view != null) {
//            保存相应数据
            savedState = saveState()
        }
        if (savedState != null) {
//            保存到arguments中，保证数据恢复
            val arguments = arguments ?: Bundle()
            arguments?.putBundle("savedFragmentBundle0x12340987", savedState)
        }
    }

    /*将取得的数据恢复到成员变量中，并开始恢复*/
    private fun resotreStateFromArguments(): Boolean {
        val arguments = arguments
        val bundle = arguments?.getBundle("savedFragmentBundle0x12340987")
        if (bundle != null) {
            restoreState()
            return true
        }
        return false
    }

    private fun restoreState() {
        //在此处恢复具体数据
        if (savedState != null) {
            onRestoreState(savedState!!)
        }
    }

    /*
    * 可复写
    * */
    open fun onRestoreState(savedInstanceState: Bundle) {

    }

    private fun saveState(): Bundle {
        /*执行具体的保存操作，save和OnSave等成对操作
        前者是对流程的基础封装和处理，后者是开发者需要的具体操作*/
        savedState = Bundle()
        onSaveState(savedState!!)
        return savedState!!
    }

    /*在此处保存数据*/
    private fun onSaveState(savedInstanceState: Bundle) {

    }
}
