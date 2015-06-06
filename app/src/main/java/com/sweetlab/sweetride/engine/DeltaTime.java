package com.sweetlab.sweetride.engine;

/**
 * Provides the delta time.
 */
public class DeltaTime {
    /**
     * Keep track if any updates have ran.
     */
    private boolean mIsStarted;

    /**
     * Time of last update.
     */
    private long mLastUpdateTime;

    /**
     * Calculate delta time since last call.
     *
     * @param currentTime
     * @return The delta time in same base as provided current time.
     */
    public long get(long currentTime) {
        long delta = 0;
        if (mIsStarted) {
            delta = currentTime - mLastUpdateTime;
        }
        mLastUpdateTime = currentTime;
        mIsStarted = true;
        return delta;
    }
}
