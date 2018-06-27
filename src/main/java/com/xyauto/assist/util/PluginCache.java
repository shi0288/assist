package com.xyauto.assist.util;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by shiqm on 2017-07-18.
 */
public class PluginCache {

    private final static Map<String, Plugin> map = new HashMap();

    public static void add(String key, Plugin plugin) {
        map.put(key, plugin);
    }

    public static Plugin get(String key) {
        return map.get(key);
    }

    public static boolean containsKey(String key) {
        return map.containsKey(key);
    }

    public static String string() {
        return JSON.toJSONString(map);
    }


}
