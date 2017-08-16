package com.ttkey.service.mixers.executor.util;

public class PropertyManager {

    public static final String FORWARD_SLASH = "/";
    public static final String RESOURCE_NAME_SUFFIX = ".jar";
    public static final String FETCH_EXECUTABLE_PREFIX = FORWARD_SLASH+"function" +FORWARD_SLASH+ "executable";


    public static String getProperty(String key, String defValue) {
        return System.getProperty(key, defValue);
    }
}
