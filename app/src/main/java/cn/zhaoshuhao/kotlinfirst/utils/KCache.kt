package cn.zhaoshuhao.kotlinfirst.utils

import android.content.Context
import android.util.LruCache
import com.google.gson.Gson
import com.google.gson.JsonParser

/**
 * Created by Scout
 * Created on 2017/8/25 8:57.
 */
inline fun <reified T : Any> anyToJson(t: T): String = Gson().toJson(t, T::class.java)

inline fun <reified T : Any> jsonToAny(json: String): T = Gson().fromJson(json, T::class.java)

inline fun <reified T : Any> listToJson(t: T): String = Gson().toJson(t)

inline fun <reified T : Any> jsonToList(json: String): ArrayList<T> {
    val list = ArrayList<T>()
    val gson = Gson()
    val jsonArray = JsonParser().parse(json).asJsonArray
    jsonArray.mapTo(list) { gson.fromJson(it, T::class.java) }
    return list
}

class KCache<V : Any>(private val context: Context, private val name: String) {

    fun putValue(value: V) {
        val delegate = SPExt.SpDelegate(context, name, value)
        delegate.setValue(context, KCache<V>::name, value)
    }

    fun <R> getValue(value: V): R {
        val delegate = SPExt.SpDelegate(context, name, value)
        return delegate.getValue(context, KCache<V>::name) as R
    }
}

class KInternalCache<V> {
    private var cache: LruCache<String, V>

    init {
        val maxKb: Int = (Runtime.getRuntime().maxMemory() / 1024 / 8).toInt()
        cache = LruCache(maxKb)
    }

    companion object {
        fun <V> get(): KInternalCache<V> = KInternalCache<V>()
    }

    internal fun put(key: String, value: V) = cache.put(key, value)

    internal fun get(key: String, default: V): V {
        var value = cache.get(key)
        if (value == null) value = default
        return value
    }
}
