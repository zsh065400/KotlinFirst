package cn.zhaoshuhao.kotlinfirst.model.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

/**
 * Created by Scout
 * Created on 2017/9/5 16:43.
 */
class KDbHelper(context: Context) : SQLiteOpenHelper(context, "kotlin_o2o.db", null, 1) {

    val table_search = "search"

    companion object {
        private var instance: KDbHelper? = null

        @Synchronized
        fun getHelper(context: Context): KDbHelper? {
            if (instance == null) instance = KDbHelper(context)
            return instance
        }
    }


    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("create table if not exists $table_search(_id integer primary key autoincrement not null, origin varchar, data varchar)")
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        db?.execSQL("drop table $table_search")
        onCreate(db)
    }
}


