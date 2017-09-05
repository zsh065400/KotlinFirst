package cn.zhaoshuhao.kotlinfirst.utils

import android.content.Context
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * Created by Scout
 * Created on 2017/8/24 11:09.
 */
class KSharedPreference<T>(private val context: Context,
                           private val name: String,
                           private val default: T) : ReadWriteProperty<Any?, T> {
    private val prefs by lazy { context.getSharedPreferences("KotlinFirst", Context.MODE_PRIVATE) }

    override fun getValue(thisRef: Any?, property: KProperty<*>): T {
        return findPreference(name, default)
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        putPreference(name, value)
    }

    private fun findPreference(name: String, default: T): T = with(prefs) {
        val res = when (default) {
            is Long -> getLong(name, default)
            is Int -> getInt(name, default)
            is Float -> getFloat(name, default)
            is String -> getString(name, default)
            is Boolean -> getBoolean(name, default)
            else -> throw IllegalArgumentException("This type can be saved into Preferences")
        }
        res as T
    }

    private fun putPreference(name: String, value: T) = with(prefs.edit()) {
        val res = when (value) {
            is Long -> putLong(name, value)
            is Int -> putInt(name, value)
            is Float -> putFloat(name, value)
            is String -> putString(name, value)
            is Boolean -> putBoolean(name, value)
            else -> throw IllegalArgumentException("This type can be saved into Preferences")
        }.apply()
    }

    companion object {
        val TRUE = true
        val FALSE = false

        val ZERO = 0

        val EMPTY = ""

        val ZERO_DOT_ZERO = 0.0

        val ZERO_LONG = 0L
    }
}

object SPExt {
    fun <T : Any> SpDelegate(context: Context, name: String, default: T): KSharedPreference<T> = KSharedPreference(context, name, default)

}
