package cn.zhaoshuhao.kotlinfirst.model.db

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase

/**
 * Created by Scout
 * Created on 2017/9/6 19:01.
 */
class KSearchDao(val context: Context) {
    private val helper: KDbHelper?
        get() {
            return KDbHelper.getHelper(context)
        }

    private val database: SQLiteDatabase?
        get() {
            return helper?.readableDatabase
        }

    private val tableName = "search"

    fun queryAll(): Cursor = database!!.query(tableName, null, null, null, null, null, null)

    fun onCreate() {
        helper
        database
    }

    fun onDestroy() {
        if (database!!.isOpen) database!!.close()
    }

}
