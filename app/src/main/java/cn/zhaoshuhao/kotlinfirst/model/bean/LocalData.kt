package cn.zhaoshuhao.kotlinfirst.model.bean

import android.os.Parcel
import android.os.Parcelable
import java.io.Serializable

/**
 * Created by Scout
 * Created on 2017/8/12 16:14.
 */
class Address(val UUID: String, val name: String, val phone: String, val part: String, val detail: String) : Parcelable {
    constructor(source: Parcel) : this(
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(UUID)
        writeString(name)
        writeString(phone)
        writeString(part)
        writeString(detail)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Address> = object : Parcelable.Creator<Address> {
            override fun createFromParcel(source: Parcel): Address = Address(source)
            override fun newArray(size: Int): Array<Address?> = arrayOfNulls(size)
        }
    }
}

data class TypeInfo(val text: String, val icon: Int)

data class WebViewInfo(val title: String, val url: String) : Serializable

class ShoppingCart(val id: String, val name: String, val img: String,
                   val price: String, val value: String, var num: String,
                   var checked: Boolean) : Parcelable {
    constructor(source: Parcel) : this(
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            1 == source.readInt()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(id)
        writeString(name)
        writeString(img)
        writeString(price)
        writeString(value)
        writeString(num)
        writeInt((if (checked) 1 else 0))
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<ShoppingCart> = object : Parcelable.Creator<ShoppingCart> {
            override fun createFromParcel(source: Parcel): ShoppingCart = ShoppingCart(source)
            override fun newArray(size: Int): Array<ShoppingCart?> = arrayOfNulls(size)
        }
    }
}
