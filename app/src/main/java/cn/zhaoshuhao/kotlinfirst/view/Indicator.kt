package cn.zhaoshuhao.kotlinfirst.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import cn.zhaoshuhao.kotlinfirst.R

/**
 * Created by Scout
 * Created on 2017/7/21 10:47.
 */
class Indicator : View {

    constructor(context: Context) : this(context, null) {
        initPaint()
    }

    constructor(context: Context, attributeSet: AttributeSet?) : super(context, attributeSet) {
        if (attributeSet != null) {
            val typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.Indicator)
            mNumbers = typedArray.getInteger(R.styleable.Indicator_numbers, mNumbers)
            mRadius = typedArray.getFloat(R.styleable.Indicator_radius, mRadius)
            //获取颜色
            val forceColorResId = typedArray.getResourceId(R.styleable.Indicator_forceColor, 0)
            mForceColor = if (forceColorResId != 0) resources.getColor(forceColorResId) else
                typedArray.getColor(R.styleable.Indicator_forceColor, mForceColor)

            val backColorResId = typedArray.getResourceId(R.styleable.Indicator_backColor, 0)
            mBackColor = if (backColorResId != 0) resources.getColor(backColorResId) else
                typedArray.getColor(R.styleable.Indicator_backColor, mBackColor)
            typedArray.recycle()
        }

        initPaint()
    }

    val mForcePaint = Paint()
    val mBackPaint = Paint()
    var mOffset: Float = 0f

    var mNumbers = 3
    var mRadius: Float = 20f
    var mForceColor = Color.GRAY
    var mBackColor = Color.GRAY
    var mPaddingLeft = 20
    var mPaddingRight = 20
    var dpiX: Float = 0f

    fun setOffset(position: Int, positionOffset: Float): Unit {
        val index = position % mNumbers
        var offset = positionOffset
        val last = mNumbers - 1
        if (index == last && positionOffset > 0) {
            offset = 0f
        }

        mOffset = (index * mRadius * 3) + (offset * mRadius * 3)
        invalidate()
    }

    private fun initPaint(): Unit {
        mForcePaint.strokeWidth = 2.0f
        mForcePaint.style = Paint.Style.FILL
        mForcePaint.color = mForceColor
        mForcePaint.isAntiAlias = true

        mBackPaint.strokeWidth = 2.0f
        mBackPaint.style = Paint.Style.STROKE
        mBackPaint.color = mBackColor
        mBackPaint.isAntiAlias = true
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val widthSpec = MeasureSpec.getSize(widthMeasureSpec)
        val heightSpec = MeasureSpec.getSize(heightMeasureSpec)

        var width = widthSpec
        var height = heightSpec

        dpiX = (resources.displayMetrics.densityDpi / 160).toFloat()

        if (widthMode == MeasureSpec.AT_MOST) {
            width = ((mPaddingLeft * 2) + (mNumbers * mRadius * 2) + ((mNumbers - 1) * mRadius)).toInt()
        }
        if (heightMode == MeasureSpec.AT_MOST) {
            height = (mRadius * 2 * dpiX + 0.5).toInt()
        }
        setMeasuredDimension(width, height)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        var i = 0
        val y: Float = (mRadius * dpiX + 0.5).toFloat()
        while (i < mNumbers) {
            canvas?.drawCircle(mPaddingLeft + mRadius + i * mRadius * 3f, y, mRadius, mBackPaint)
            i++
        }
        canvas?.drawCircle(mPaddingLeft + mRadius + mOffset, y, mRadius, mForcePaint)
    }
}
