package cn.zhaoshuhao.kotlinfirst.model.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase

/**
 * Created by Scout
 * Created on 2017/9/6 19:01.
 */
class KSearchDao(context: Context) : BaseDao(context) {
    companion object {
        private var instance: KSearchDao? = null

        fun get(context: Context): KSearchDao {
            if (instance == null) instance = KSearchDao(context)
            return instance!!
        }
    }

    override val tableName: String
        get() = "search"

    fun insert(vararg bundle: Pair<String, String>) {
        val contentValues = ContentValues()
        for ((key, value) in bundle) {
            contentValues.put(key, value)
            if (contentValues.size() == 2) {
                database?.insert(tableName, null, contentValues)
                contentValues.clear()
            }
        }
    }

    fun queryForLike(whereArgs: String): Cursor? {
        return database?.query(tableName, null, "data like ?", arrayOf("%$whereArgs%"), null, null, null)
    }
}

class KCartDao(context: Context) : BaseDao(context) {
    companion object {
        private var instance: KCartDao? = null

        fun get(context: Context): KCartDao {
            if (instance == null) instance = KCartDao(context)
            return instance!!
        }
    }

    override val tableName: String
        get() = "cart"

    fun insert(vararg bundle: Pair<String, String>) {
        val contentValues = ContentValues()
        for ((key, value) in bundle) {
            contentValues.put(key, value)
            if (contentValues.size() == 2) {
                database?.insert(tableName, null, contentValues)
                contentValues.clear()
            }
        }
    }

    fun queryForLike(column: String, whereArgs: String): Cursor? {
        return database?.query(tableName, null, "$column = ?", arrayOf("$whereArgs"), null, null, null)
    }

    fun updateForLike(column: String, whereArgs: String, new: Pair<String, String>) {
        val contentValues = ContentValues()
        contentValues.put(new.first, new.second)
        database?.update(tableName, contentValues, "$column = ?", arrayOf("$whereArgs"))
    }

    fun deleteForLike(column: String, whereArgs: String) {
        database?.delete(tableName, "$column = ?", arrayOf("$whereArgs"))
    }
}

abstract class BaseDao(val context: Context) {
    open val tableName: String = ""

    init {
        helper
        database
    }

    open val helper: KDbHelper?
        get() {
            return KDbHelper.getHelper(context)
        }

    open val database: SQLiteDatabase?
        get() {
            return helper?.readableDatabase
        }

    open fun onDestroy() {
        if (database!!.isOpen) database!!.close()
    }

    open fun queryAll(): Cursor = database!!.query(tableName, null, null, null, null, null, null)
}

