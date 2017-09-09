package cn.zhaoshuhao.kotlinfirst.model.db

import android.content.Context
import cn.zhaoshuhao.kotlinfirst.utils.LogType
import cn.zhaoshuhao.kotlinfirst.utils.log
import com.j256.ormlite.dao.Dao
import com.j256.ormlite.field.DatabaseField
import com.j256.ormlite.table.DatabaseTable
import java.io.IOException

/**
 * Created by Scout
 * Created on 2017/9/5 15:43.
 */
@DatabaseTable(tableName = "kotlin_search_data")
data class SearchData(
        @DatabaseField(generatedId = true, columnName = "_id")
        val _id: Int,
        @DatabaseField(columnName = "origin")
        val origin: String,
        @DatabaseField(columnName = "data")
        val data: String
)

class SearchDao(val context: Context) {
    private lateinit var dao: Dao<SearchData, Integer>
    private lateinit var helper: DbHelper

    init {
        try {
            helper = DbHelper.getHelper(context)
            dao = helper.getDao(SearchDao::class.java) as Dao<SearchData, Integer>
        } catch (e: IOException) {
            log(LogType.ERROR, "Search Dao", e.localizedMessage)
        }
    }

    fun add(data: SearchData) {
        try {
            dao.create(data)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
    /*
    * 其余增删改查的dao
    * */
}
