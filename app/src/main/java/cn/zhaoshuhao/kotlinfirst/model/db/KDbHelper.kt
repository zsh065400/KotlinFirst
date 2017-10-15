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
    val table_cart = "cart"
    val table_address = "address"
    val table_history = "history"

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
        db?.execSQL("create table if not exists $table_cart(_id integer primary key autoincrement not null,name varchar, json varchar)")
        db?.execSQL("create table if not exists $table_address(_id integer primary key autoincrement not null,UUID varchar, json varchar)")
        db?.execSQL("create table if not exists $table_history(_id integer primary key autoincrement not null,name varchar, json varchar)")
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        db?.execSQL("drop table $table_search")
        db?.execSQL("drop table $table_cart")
        db?.execSQL("drop table $table_address")
        db?.execSQL("drop table $table_history")
        onCreate(db)
    }
}


