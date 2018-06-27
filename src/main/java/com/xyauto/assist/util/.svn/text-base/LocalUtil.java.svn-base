package com.xyauto.assist.util;

/**
 * Created by shiqm on 2017-07-24.
 */
public class LocalUtil {

    private static final ThreadLocal<Object> LOCAL_MEMORY = new ThreadLocal<Object>();


    public static Object get() {
        return LOCAL_MEMORY.get();
    }

    public static void set(Object value) {
        LOCAL_MEMORY.set(value);
    }

    public static void remove() {
        LOCAL_MEMORY.remove();
    }



}
