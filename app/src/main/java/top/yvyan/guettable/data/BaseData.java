package top.yvyan.guettable.data;

import com.google.gson.Gson;
import com.tencent.mmkv.MMKV;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * List转码存储，用于存储课程列表等信息
 */
public class BaseData {
    public static <T> void set(String key, List<T> Beans) {
        String str = new Gson().toJson(Beans);
        MMKV mmkv = MMKV.defaultMMKV();
        mmkv.encode(key, str);
    }

    public static <T> List<T> get(String key, Type type) {
        List<T> list = null;
        MMKV mmkv = MMKV.defaultMMKV();
        try {
            String str = mmkv.decodeString(key);
            list = new Gson().fromJson(str, type);
        } catch (Exception e) {
            mmkv.remove(key);
        }
        if (list == null) {
            list = new ArrayList<>();
        }
        return list;
    }
}
