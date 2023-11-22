package com.neo.gpt_bot_test.containers.db.service;

import java.util.Locale;
import java.util.ResourceBundle;

public class LocalizationManager {
    private static final String BUNDLE_NAME = "messages";
    private static ResourceBundle resourceBundle;

    static {
        setLocale(Locale.getDefault());
    }

    public static void setLocale(Locale locale) {
        System.out.println("Set locale: " + locale);
        resourceBundle = ResourceBundle.getBundle(BUNDLE_NAME, locale);
    }

    /**
     * Method returns string from messages_{locale}.properties by key
     */
    public static String getString(String key) {
        return resourceBundle.getString(key);
    }
}
