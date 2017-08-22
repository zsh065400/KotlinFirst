package cn.zhaoshuhao.kotlinfirst.model.bean

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by Scout
 * Created on 2017/8/12 16:14.
 */
data class TypeInfo(val text: String, val icon: Int)

data class WebViewInfo(val title: String, val url: String) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString()) {
    }

    override fun writeToParcel(p0: Parcel?, p1: Int) {
        p0?.writeString(title)
        p0?.writeString(url)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<WebViewInfo> {
        override fun createFromParcel(parcel: Parcel): WebViewInfo {
            return WebViewInfo(parcel)
        }

        override fun newArray(size: Int): Array<WebViewInfo?> {
            return arrayOfNulls(size)
        }
    }
}
