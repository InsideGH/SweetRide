package com.sweetlab.sweetride.logger;

import android.util.Log;

/**
 * Single log message.
 */
public class LogMessage {
    /**
     * The tag.
     */
    private final String mTag;

    /**
     * The message tag.
     */
    private final String mMessageTag;

    /**
     * The message tag with space at end.
     */
    private final String mMessageTagWithSpace;

    /**
     * Constructor.
     *
     * @param tag        The tag.
     * @param messageTag The message tag
     */
    public LogMessage(String tag, Enum messageTag) {
        mTag = tag;
        mMessageTag = messageTag.toString();
        mMessageTagWithSpace = mMessageTag + " ";
    }

    /**
     * Get the message tag.
     *
     * @return The message tag.
     */
    public String getMessageTag() {
        return mMessageTag;
    }

    /**
     * Log.
     *
     * @param message The message.
     */
    public void log(String message) {
        Log.d(mTag, mMessageTagWithSpace + message);
    }
}
