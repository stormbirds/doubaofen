package com.tiangou.douxiaomi.utils;

import android.text.TextUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonPraise {
    private static Gson gson = new Gson();

    public static String objToJson(Object obj) {
        try {
            return gson.toJson(obj);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Object jsonToObj(String str, Class cls) {
        try {
            return gson.fromJson(str, cls);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Object jsonToObj(String str, TypeToken typeToken) {
        try {
            return gson.fromJson(str, typeToken.getType());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Map<String, Object> jsonToMapObj(String str) {
        return (Map) gson.fromJson(str, new TypeToken<Map<String, Object>>() {
        }.getType());
    }

    public static Map<String, String> jsonToMap(String str) {
        return (Map) gson.fromJson(str, new TypeToken<Map<String, String>>() {
        }.getType());
    }

    public static String optCode(String str) {
        String str2;
        try {
            str2 = new JSONObject(str).opt("code").toString();
        } catch (JSONException e) {
            e.printStackTrace();
            str2 = null;
        }
        return str2 == null ? "555" : str2;
    }

    public static <T> T parseJSON(String str, Type type) {
        return new Gson().fromJson(str, type);
    }

    public static String optCode(String str, String str2) {
        try {
            return new JSONObject(str).opt(str2).toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Map<String, Object> jsonToObjMap(String str) {
        return (Map) gson.fromJson(str, new TypeToken<Map<String, Object>>() {
        }.getType());
    }

    public static String opt701(String str) throws JSONException {
        JSONObject jSONObject = (JSONObject) new JSONObject(str).opt("data");
        Iterator<String> keys = jSONObject.keys();
        if (keys.hasNext()) {
            return jSONObject.optString(keys.next().toString());
        }
        return null;
    }

    public static Map<String, Object> opt001MapData(String str) throws JSONException {
        return jsonToObjMap(((JSONObject) new JSONObject(str).opt("result")).toString());
    }

    public static Object opt001ObjData(String str, Class cls, String str2) throws Exception {
        return optObj(((JSONObject) new JSONObject(str).opt(str2)).toString(), cls);
    }

    public static Object opt001ListData(JSONArray jSONArray, Type type) throws Exception {
        if (jSONArray != null) {
            return optObj(jSONArray.toString(), type);
        }
        return null;
    }

    public static Object opt001ListData(String str, Type type) {
        if (str != null) {
            try {
                return optObj(str, type);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static Object opt001ListData(String str, Type type, String str2) throws JSONException {
        JSONArray optJSONArray;
        if (!TextUtils.isEmpty(str) && (optJSONArray = new JSONObject(str).optJSONArray(str2)) != null) {
            return optObj(optJSONArray.toString(), type);
        }
        return null;
    }

    public static Object optObj(String str, Type type) {
        return gson.fromJson(str, type);
    }

    public static Object optObj(String str, Class cls) {
        return gson.fromJson(str, cls);
    }
}
