package cn.zhaoshuhao.kotlinfirst.utils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.util.ArrayList;

/**
 * Created by Scout
 * Created on 2017/8/26 11:35.
 */

public class GsonParse {
    private static volatile GsonParse sInstance;
    private static Gson mGson;

    private GsonParse() {
        mGson = new Gson();
    }

    public static GsonParse get() {
        if (sInstance == null) {
            sInstance = new GsonParse();
        }
        return sInstance;
    }

    public <T> String ListToJson(T json) {
        return mGson.toJson(json);
    }

    public <T> ArrayList<T> JsonToList(String json, Class<T> cls) {
        final ArrayList<T> list = new ArrayList<>();
        final JsonArray jsonArray = new JsonParser().parse(json).getAsJsonArray();
        for (JsonElement element : jsonArray) {
            list.add(mGson.fromJson(element, cls));
        }
        return list;
    }
}
