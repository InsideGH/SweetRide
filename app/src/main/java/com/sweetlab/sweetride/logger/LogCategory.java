package com.sweetlab.sweetride.logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * A log category contains all logs.
 */
public class LogCategory {
    /**
     * Name of category.
     */
    private final String mName;

    /**
     * Maps enum tag to a controllable log.
     */
    private HashMap<Enum, ControllableLog> mMap = new HashMap<>();

    /**
     * Constructor.
     *
     * @param tag   The tag.
     * @param clazz The class describing the category.
     */
    public LogCategory(String tag, Class<? extends Enum> clazz) {
        mName = clazz.getSimpleName();
        for (Enum categoryTag : clazz.getEnumConstants()) {
            ControllableLog log = new ControllableLog(tag, categoryTag);
            mMap.put(categoryTag, log);
        }
    }

    /**
     * Log.
     *
     * @param categoryTag Enum tag.
     * @param message     Message to log.
     */
    public void log(Enum categoryTag, String message) {
        ControllableLog log = mMap.get(categoryTag);
        if (log != null) {
            log.log(message);
        }
    }

    /**
     * Get the category name.
     *
     * @return The name.
     */
    public String getName() {
        return mName;
    }

    /**
     * Get a list of all logs withing the category.
     *
     * @return Unmodifiable list of logs.
     */
    public List<ControllableLog> getLogs() {
        return Collections.unmodifiableList(new ArrayList<>(mMap.values()));
    }
}
