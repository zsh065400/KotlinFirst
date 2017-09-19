package cn.zhaoshuhao.kotlinfirst.widget

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import cn.zhaoshuhao.kotlinfirst.R

/**
 * Created by Scout
 * Created on 2017/9/15 10:00.
 */
class CartNumberChanged : LinearLayout, View.OnClickListener, TextWatcher {
    private lateinit var inflate: LayoutInflater
    private lateinit var btnAdd: Button
    private lateinit var btnSub: Button
    private lateinit var tvValue: TextView

    var addCallBack: ((View, Int) -> Unit)? = null
    var subCallBack: ((View, Int) -> Unit)? = null
    var changedCallBack: ((View, Int) -> Unit)? = null

    var value = 1
        set(value) {
            if (value in min..max) {
                field = value
                setTextValue()
            }
        }
        get() = field

    var max = 1
        set(value) {
            field = if (field < 1) 1 else value
        }
        get() = field
    var min = 1
        set(value) {
            field = if (value < 0) 1 else value
        }
        get() = field

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0) {
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : this(context, attrs, defStyleAttr, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        inflate = LayoutInflater.from(context)
        initView()
        if (attrs != null) {
            val typedArray = context.obtainStyledAttributes(attrs, R.styleable.CartNumberChanged, defStyleAttr, defStyleRes)
            max = typedArray.getInt(R.styleable.CartNumberChanged_max, 1)
            min = typedArray.getInt(R.styleable.CartNumberChanged_min, 1)
            value = typedArray.getInt(R.styleable.CartNumberChanged_value, 1)
            typedArray.recycle()
        }
    }

    private fun initView() {
        val view = inflate.inflate(R.layout.button_add_sub, this, true)
        btnAdd = view.findViewById<Button>(R.id.id_btn_add)
        btnSub = view.findViewById<Button>(R.id.id_btn_sub)
        tvValue = view.findViewById<TextView>(R.id.id_tv_value)
        btnAdd.setOnClickListener(this)
        btnSub.setOnClickListener(this)
        tvValue.addTextChangedListener(this)
    }

    override fun onClick(btn: View?) {
        when (btn?.id) {
            R.id.id_btn_add -> add()
            R.id.id_btn_sub -> sub()
        }
    }

    override fun afterTextChanged(p0: Editable?) {

    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        changedCallBack?.invoke(tvValue, p0.toString().toInt())
    }

    private fun add() {
        value += 1
        setTextValue()
        addCallBack?.invoke(btnAdd, value)
    }

    private fun sub() {
        value -= 1
        setTextValue()
        subCallBack?.invoke(btnAdd, value)
    }

    private fun setTextValue() {
        tvValue.text = "$value"
    }

    fun getTextValue(): String = tvValue?.text.toString()
}


