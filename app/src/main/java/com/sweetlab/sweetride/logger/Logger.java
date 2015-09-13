package com.sweetlab.sweetride.logger;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Logger class. Requires init. Add enum classes to be able to use.
 */
public final class Logger {
    /**
     * Default log tag.
     */
    private static final String DEFAULT_LOG_TAG = "Logger";

    /**
     * Maps enum class to logging category.
     */
    private static HashMap<Class<? extends Enum>, LogCategory> sMap = new HashMap<>();

    /**
     * Log tag.
     */
    private static String sTag = DEFAULT_LOG_TAG;

    /**
     * Default shared preferences.
     */
    private static SharedPreferences sPrefs;

    private static SharedPreferences.OnSharedPreferenceChangeListener sListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            Log.d("Peter100", "Logger.onSharedPreferenceChanged " + key);
        }
    };

    /**
     * No instantiation.
     */
    private Logger() {
    }

    /**
     * Init.
     *
     * @param tag     Tag to use.
     * @param context Android context.
     */
    public static void init(String tag, Context context) {
        sTag = tag;
        sPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        sPrefs.registerOnSharedPreferenceChangeListener(sListener);
    }

    /**
     * Add logging category.
     *
     * @param category The enum class describing the category.
     */
    public static void addCategory(Class<? extends Enum> category) {
        sMap.put(category, new LogCategory(sTag, category));
    }

    /**
     * Log to a specific category. If category is not added the message
     * is silently ignored.
     *
     * @param tag     Category tag.
     * @param message Message.
     */
    public static void log(Enum tag, String message) {
        LogCategory category = sMap.get(tag.getDeclaringClass());
        if (category != null) {
            category.log(tag, message);
        }
    }

    /**
     * Get list with all categories.
     *
     * @return Unmodifiable list of categories.
     */
    public static List<LogCategory> getCategories() {
        return Collections.unmodifiableList(new ArrayList<>(sMap.values()));
    }
}
