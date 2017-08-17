package com.ttkey.service.mixers.cache;

import java.util.WeakHashMap;

public class GatewayCache {

    private static WeakHashMap<String, Object> cache = new WeakHashMap();

    public static WeakHashMap getCache() {
        return cache;
    }
}
