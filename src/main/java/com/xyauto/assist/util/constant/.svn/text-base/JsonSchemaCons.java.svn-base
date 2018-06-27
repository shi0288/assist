package com.xyauto.assist.util.constant;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by shiqm on 2018-01-19.
 */
public class JsonSchemaCons {

    private static String Disposable = "/schema/DisposablePlugin.json";
    private static String Interval = "/schema/IntervalPlugin.json";
    public static Map<String, String> map = new HashMap();

    static {
        init();
    }

    public static void init() {
        map.put("disposablePlugin", Disposable);
        map.put("intervalDayPlugin", Interval);
        map.put("intervalMonthPlugin", Interval);
        map.put("intervalWeekPlugin", Interval);
        map.put("intervalYearPlugin", Interval);
    }

    public static String get(String key) {
        return map.get(key);
    }


}
