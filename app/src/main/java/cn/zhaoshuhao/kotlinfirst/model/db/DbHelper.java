package cn.zhaoshuhao.kotlinfirst.model.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Scout
 * Created on 2017/9/5 16:09.
 */

public class DbHelper extends OrmLiteSqliteOpenHelper {

    private volatile static DbHelper INSTANCE;

    public static DbHelper getHelper(Context context) {
        if (INSTANCE == null) {
            synchronized (DbHelper.class) {
                if (INSTANCE == null)
                    INSTANCE = new DbHelper(context);
            }
        }
        return INSTANCE;
    }

    public DbHelper(Context context) {
        super(context, "kotlin_o2o.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            TableUtils.clearTable(connectionSource, SearchData.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            TableUtils.dropTable(connectionSource, SearchData.class, true);
            onCreate(database, connectionSource);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Map<String, Dao> mDaos = new HashMap<>();

    public Dao getDao(Class clazz) throws SQLException {
        Dao dao = null;
        final String simpleName = clazz.getSimpleName();
        if (mDaos.containsKey(simpleName)) {
            dao = mDaos.get(simpleName);
        }
        if (dao == null) {
            dao = super.getDao(clazz);
            mDaos.put(simpleName, dao);
        }
        return dao;
    }

    public void close() {
        super.close();
        for (String s : mDaos.keySet()) {
            Dao dao = mDaos.get(s);
            dao = null;
        }
    }
}
